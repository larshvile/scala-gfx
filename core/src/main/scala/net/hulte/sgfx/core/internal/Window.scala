package net.hulte.sgfx
package core
package internal

import core._
import java.awt.{Graphics2D, GraphicsConfiguration, GraphicsDevice, GraphicsEnvironment, Insets, Point}
import java.awt.event.{WindowAdapter, WindowEvent}
import java.awt.image.BufferStrategy
import javax.swing.{JFrame, JPanel}
import org.apache.log4j.Logger
import scala.actors.Actor
import java.awt.Dimension

/**
 * Messages which causes the window to close.
 */
case class CloseWindow()

/**
 * Message used to paint a new screen. The frameId is passed back to the client so it can keep track of the
 * rendering speed.
 */
case class PaintScreen(screenId: Int, screenContents: List[Renderable], respondTo: Actor)

/**
 * The response of {@link PaintScreen}, indicating that the screen has in fact been painted.
 */
case class ScreenPainted(screenId: Int)


/**
 * A window used for user-input and drawing.
 */
private[core] class Window(val size: Point, private val title: String) extends Actor {

  private val logger = Logger.getLogger(getClass())
  private val env: GraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment()
  private val device: GraphicsDevice = env.getDefaultScreenDevice()
  private val config: GraphicsConfiguration = device.getDefaultConfiguration()

  private val frame = createFrame()

  private val bufferStrategy = {
    frame.createBufferStrategy(2)
    val b: BufferStrategy = frame.getBufferStrategy()
    b
  }

  // make sure to exit properly when the frame is closed
  frame.addWindowListener(new WindowAdapter() {
    override def windowClosing(e: WindowEvent) {
      logger.debug("Window closing, shutting down.")
      Application.destroy()
    }
  })

  // start the actor
  start()


  def act() {
    while (true) {
      receive {
        case CloseWindow() => {
          close()
          exit()
        }

        case PaintScreen(screenId, screenContents, respondTo) => {
          drawScreen(screenContents)
          respondTo ! ScreenPainted(screenId)
        }
      }
    }
  }

  private def close() {
    frame.dispose()
    logger.info("Window closed.")
  }

  /**
   * Draws {@link Renderable}s to the screen by using double-buffering. The weird pattern here is taken
   * directly from the {@link BufferStrategy} javadoc.
   */
  private def drawScreen(screenContents: List[Renderable]) {
    val s = bufferStrategy;
    do {
      do {
        val renderer = s.getDrawGraphics().asInstanceOf[Graphics2D]
        try {
          screenContents.foreach { _.render(renderer, size) }
        } finally {
          renderer.dispose()
        }
      } while (s.contentsRestored())
      s.show()
    } while (s.contentsLost())
  }
  
  private def createFrame() = {
    val f = new JFrame(title, config)
    
    f.setResizable(false)
    f.setIgnoreRepaint(true)
    
    val p = new JPanel
    p.setPreferredSize(new Dimension(size.x, size.y))
    f.getContentPane add p
    
    f.pack
    
    val c = env.getCenterPoint
    f.setLocation(c.x - f.getWidth / 2, c.y - f.getHeight / 2)
    f.setVisible(true)
    
    f
  }
}
