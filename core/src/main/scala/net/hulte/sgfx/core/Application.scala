package net.hulte.sgfx.core

import internal._
import Ordered._

import java.awt.Point
import java.util.concurrent.atomic.AtomicBoolean

import org.apache.log4j.Logger

import scala.actors.Actor._

/**
 * Starts and stops the container-application.
 */
object Application {

  private val logger = Logger.getLogger(classOf[Application])
  private val MinTimePerFrame = 1000 / 150 // stay somewhere in the area of ~150fps
  
  private[this] var runner: ApplicationRunner = null

  def create(screenWidth: Int, screenHeight: Int, logicLoop: LogicLoop): Unit = {
    assert (runner == null)
    logger.info("Starting " + logicLoop + ".")

    runner = new ApplicationRunner(
        new Application(logicLoop,
            new Window(new Point(screenWidth, screenHeight), logicLoop.toString)))
    start
  }

  def destroy() {
    if (runner != null) {
      runner.destroy()
      runner = null
    }
  }
  
  private def start {
    val t: Thread = new Thread(runner, "apprunner")
    t.setDaemon(true)
    t.start()
  }

  private class ApplicationRunner(app: Application) extends Runnable {
    private val stopFlag: AtomicBoolean = new AtomicBoolean

    override def run() {
      try {
        while (!stopFlag.get()) {
          app.update()
        }
      } finally {
        app.destroy()
      }
    }

    def destroy() {
      stopFlag.set(true)
    }
  }
}


private final class Application private (logicLoop: LogicLoop, window: Window) {
  import Application._

  val timer = new SystemNanoTimeTimer
  
  val paintMonitor = new Object
  var currentScreenId = 0
  @volatile var paintedScreenId = 0

  val screenPaintedActor = actor {
    while (true) {
      receive {
        case "close" => exit()
        case ScreenPainted(id) => {
            paintedScreenId = id
            paintMonitor.synchronized {
              paintMonitor.notifyAll
            }
        }
      }
    }
  }

  def destroy() {
    screenPaintedActor ! "close"
    window ! CloseWindow()
  }

  def update() {
    timer.tick()
    processFrame()
    chillOut()
  }

  private def processFrame() {
    val keyboard: Keyboard = null // TODO add me
    val screenContents = logicLoop.processFrame(window.size, timer, keyboard)
    paintScreen(screenContents.sortBy(orderOf(_)))
  }

  private def paintScreen(contents: List[Renderable]) {
    window ! PaintScreen(currentScreenId, contents, screenPaintedActor)

    // synchronize the painting in case screens are being produced faster than they can be painted
    if (currentScreenId - paintedScreenId > 1) {
      paintMonitor.synchronized {
        while (paintedScreenId != currentScreenId) {
          paintMonitor.wait
        }
      }
    }
    
    currentScreenId += 1
  }

  private def chillOut() {
    val spareTime = MinTimePerFrame - timer.elapsedMsSinceTick
    if (spareTime > 0) {
      Thread.sleep(spareTime)
    }
  }
}
