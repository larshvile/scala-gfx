package net.hulte.sgfx
package graphics

import core.Renderable
import geom.Point
import java.awt.Graphics2D

object Sprite {
  
  implicit def asRenderable(s: Sprite): Renderable = new Renderable() {
    val center = s.center
    val size = s.size
    def render(renderer: Graphics2D, screenSize: java.awt.Point) = {
      s.draw(center, size, renderer, screenSize)
    }
  }
}

/**
 * A sprite differs from a {@link Renderable} in that it is stateful. It produces a stateless
 * {@link Renderable} instead of being one.
 */
trait Sprite {
  
  // TODO mutable cr@p .. not really ideal...
  var center = Point.Zero
  var size = Point.Zero

  protected def draw(center: Point, size: Point, renderer: Graphics2D, screenSize: Point)

}
