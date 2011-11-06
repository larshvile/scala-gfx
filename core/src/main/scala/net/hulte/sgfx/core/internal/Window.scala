package net.hulte.sgfx.core.internal

import scala.actors.Actor

import java.awt._
import java.awt.event._
import java.awt.image.BufferStrategy

import net.hulte.sgfx.core.{Application, Screen, Keyboard}
import net.hulte.sgfx.graphics.Renderable

import org.apache.log4j.Logger


/**
 * Messages which causes the window to close.
 */
case class CloseWindow()

/**
 * Message used to draw a new screen. The frameId is passed
 * back to the client so it can synchronize in case it's creating
 * screens faster than we can draw 'em.
 */
case class DrawScreen(screen: Renderable, frameId: Long, respondTo: Actor,
    synchronized: Boolean)

/**
 * Callback-message for DrawScreen indicating that a frame
 * has been drawn.
 */
case class DrawScreenFinished(frameId: Long)


/**
 * A window used for user-input and drawing.
 *
 * @author lars
 */
private[core] class Window(screenSize: Point, private val title: String) extends Actor {

  private val logger = Logger.getLogger(getClass())
  private val env:GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()
  private val device:GraphicsDevice = env.getDefaultScreenDevice()
  private val config:GraphicsConfiguration = device.getDefaultConfiguration()

  private val (frame, insets) = createFrame()

  private val (bufferStrategy) = {
    frame.createBufferStrategy(2)
    val b: BufferStrategy = frame.getBufferStrategy()
    b
  }

  // make sure to exit properly when the frame is closed
  frame.addWindowListener(new WindowAdapter() {
    override def windowClosing(e: WindowEvent) {
      logger.debug("window closing, shutting down")
      Application.destroy()
    }
  })

  // start the actor
  start()


  /**
   * Returns the screen-size as a <code>Point</code>.
   */
  def size: Point = screenSize


  /**
   * Creates the frame, returns a reference to it, and the frame's
   * insets (borders-sizes).
   */
  private def createFrame(): (Frame, Insets) = {
      val f = new Frame(title, config)

    //f.setUndecorated(true) // TODO add full-screen toggle later??
    f.setResizable(false)
    f.setIgnoreRepaint(true)
    f.pack()

    val insets = f.getInsets()
    val adjustedSize = new Point(screenSize.x + insets.left + insets.right,
      screenSize.y + insets.top + insets.bottom)
    val c = env.getCenterPoint()

    f.setSize(adjustedSize.x, adjustedSize.y);
    f.setLocation(c.x - adjustedSize.x / 2, c.y - adjustedSize.y /2)
    f.setVisible(true)
    
    (f, insets)
  }


  /**
   * Handles incoming messages.
   */
  def act() {
    while (true) {
      receive {
        case CloseWindow() => {
          close()
          exit()
        }

        case DrawScreen(screen, frameId, respondTo, synched) => {
          drawScreen(screen)
          respondTo ! DrawScreenFinished(frameId)
          if (synched) {
            reply {
              null
            }
          }
        }
      }
    }
  }


  private def close() {
    frame.setVisible(false)
    frame.dispose()
    logger.info("window closed")
  }


  /**
   * Draws a <code>Renderable</code> to the screen by using double-buffering.
   * The weird pattern here is taken directly from BufferStrategy's javadoc.
   */
  private def drawScreen(screen: Renderable) {
    val s = bufferStrategy;
    do {
      do {
        val renderer = s.getDrawGraphics().asInstanceOf[Graphics2D]
        try {
          adjustForFrameDecoration(renderer)
          screen.render(renderer, screenSize)
        } finally {
          renderer.dispose()
        }
      } while (s.contentsRestored())
      s.show()
    } while (s.contentsLost())
  }


  private def adjustForFrameDecoration(r: Graphics2D) {
    if (!frame.isUndecorated()) {
      r.translate(insets.left, insets.top)
    }
  }
}

