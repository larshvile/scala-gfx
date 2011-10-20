package net.hulte.sgfx.core

import scala.actors.Actor._

import java.awt.Point

import internal.{Window, CloseWindow, DrawScreen, DrawScreenFinished}
import internal.DefaultTimer
import net.hulte.sgfx.graphics.{Renderable, Composite}
import net.hulte.sgfx.graphics.Label
import net.hulte.sgfx.graphics.Alignment._


/**
 * Entry-point for games. Stars and stops the application used to execute games. 
 * 
 * @author lars
 */
object Application {
  
  private var thread: GameThread = null
  
  /**
   * Creates and starts a new <code>GameApplication</code>.
   */
  def create(screenWidth: Int, screenHeight: Int, game: Game) {
    assert (thread == null)
    println("starting " + game.name)

    val window = new Window(new Point(screenWidth, screenHeight), game.name)
    startThread(new GameThread(new Application(game, window)))
  }


  private def startThread(t: GameThread) {
    thread = t;
    new Thread(thread).start()
  }
  
  
  /**
   * Stops and destroys the running <code>GameApplication</code>.
   */
  def destroy() {
    if (thread == null) {
      return
    } else {
      thread.destroy()
      thread = null
    }
  }
}

/**
 * Main application-object for creating games. Takes care of all boiler-plate stuff like creating the
 * drawing-window, executing the game-loop etc.
 * 
 * @author lars
 */
private[this] final class Application private (game: Game, window: Window) {
 
  val minTimePerFrame = 1000 / 100 // try to land at max ~100 fps 

  val timer = new DefaultTimer
  
  var currentFrameId: Long = 0
  @volatile var lastDrawnFrameId: Long = 0

  // an actor which is fed the id of the last drawn frame, from the Window
  val lastFrameActor = actor {
    while (true) {
      receive {
        case "close" => exit()
        case DrawScreenFinished(frameId) => lastDrawnFrameId = frameId
      }
    }
  }


  /**
   * Called upon application-exit to perform various cleanup tasks.
   */
  def destroy() {
    lastFrameActor ! "close"
    window ! CloseWindow()
  }
  
  
  /**
   * Processes application-logic for a single frame.
   */
  def process() {
    timer.update()
    updateGame()
    chillOut()
  }


  /**
   * Processes game-logic. Also wraps the game's screen in a renderable-composite,
   * which is handed to the real screen in one, atomic drawing-operation for the entire
   * frame.
   */
  def updateGame() {
    val screenContents = new Composite()
    screenContents.add(new DebugHud(timer.fps))

    val wrappedScreen = new Screen() {
      override def size: Point = window.size
      override def add(r: Renderable) {
        screenContents.add(r)
      }
    }

    game.process(wrappedScreen, timer, null)   // TODO Keyboard, could be this?
    drawScreen(screenContents)
  }


  /**
   * Handles screen-drawing, if Window is lagging the drawing is done
   * as a synchronous operation.
   */
  def drawScreen(content: Renderable) {
  val frameLag = (currentFrameId - lastDrawnFrameId)
    val drawSynched = (frameLag > 1)
    val msg = DrawScreen(content, currentFrameId, lastFrameActor, drawSynched)

    if (drawSynched) {
      println("(drawing synched, frameLag: " + frameLag) // TODO remove later
      window !? msg
    } else {
      window ! msg
    }
    currentFrameId += 1
  }

  
  /**
   * Don't stress the cpu more than necessary, try to keep the number of ms
   * used per frame greater than the lower limit.
   */
  def chillOut() {
    val spareTime = (minTimePerFrame - timer.elapsedMsSinceUpdate)
    if (spareTime > 0) {
      Thread.sleep(spareTime)
    }
  }
}


/**
 * Composite used to draw game-related debug-information on the screen.
 */
private[this] class DebugHud(fps: Double) extends Composite(Int.MaxValue) { // TODO Int.MaxValue seems like a bad abstraction =) Ordered.Front ?
  add(Label.create("FPS: " + "%.1f".format(fps), (Right(), Top())))
}


/**
 * Thread use to drive the game's logic-loop.
 */
import java.util.concurrent.atomic.AtomicBoolean
private[this] class GameThread(app: Application) extends Runnable {
  
  private val stopFlag: AtomicBoolean = new AtomicBoolean
  
  override def run() {
    try {
        while (!stopFlag.get()) {
            app.process()
        }
    } finally {
        app.destroy()
        println("stopped game-logic thread")
    }
  }

  
  def destroy() {
    stopFlag.set(true)
  }
}

