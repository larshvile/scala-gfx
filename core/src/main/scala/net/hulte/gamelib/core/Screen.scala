package net.hulte.gamelib.core

import java.awt.Point
import net.hulte.gamelib.graphics.{Renderable, Sprite}


/**
 * Trait representing our drawable screen.
 *
 * @author lars
 */
trait Screen {

  /**
   * Returns the pixel-size of the scren as a <code>Point</code>
   */
  def size: Point

  /**
   * Adds a <code>Renderable</code> to the screen. Note that this Renderable
   * should be an immutable object, don't make any assumptions about which thread
   * will actually draw it, or when it will be drawn.
   */
  def add(r: Renderable)


  /**
   * Adds a <code>Renderable</code> to the screen, by extracting it from a <code>Sprite</code>.
   */
  def add(s: Sprite[_]): Unit = {
    add(s.getRenderable())
  }
}

