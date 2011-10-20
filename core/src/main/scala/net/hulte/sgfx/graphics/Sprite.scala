package net.hulte.sgfx.graphics

import java.awt._


/**
 * Trait representing a Sprite. A Sprite differs from a Renderable in that
 * it is stateful, and has a concept of position/size etc, and should produce
 * a stateless Renderable rather than being one.
 *
 * The generic type T contains the stateful data required for drawing, except
 * for the position.
 */
trait Sprite[T] {

  def pos: Point
  def size: Point

  /**
   * Returns an immutable Renderable which can be drawn.
   */
  def getRenderable(): Renderable = new Renderable() {
    val posNow = new Point(pos)
    val stateNow = getState()

    override def render(renderer: Graphics2D, screenSize: Point) = {
      draw(stateNow, posNow, renderer, screenSize)
    }
  }

  /**
   * Returns the internal state required for drawing the sprite.
   */
  protected def getState(): T

  /**
   * Draws the sprite.
   */
  protected def draw(state: T, pos: Point, renderer: Graphics2D,
      screenSize: Point): Unit

}

