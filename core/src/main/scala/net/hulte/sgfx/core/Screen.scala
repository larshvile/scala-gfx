package net.hulte.sgfx.core

import java.awt.Point

/**
 * The application's screen.
 */
trait Screen {

  /**
   * Returns the pixel-size of the screen as a {@link Point}.
   */
  def size: Point

  /**
   * Adds a {@link Renderable} to the screen.
   */
  def add(r: Renderable)
  
}
