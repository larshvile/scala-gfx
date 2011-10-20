package net.hulte.sgfx.graphics


/**
 * Simple trait used to control the Z-order drawing of renderables. Rendereables
 * with lower order than those with higher order.
 */
trait Ordered {

  def order: Int

}


import java.awt.{Graphics2D, Point}

/**
 * Decorator for Renderable's which doesn't define an ordering.
 */
class OrderedRenderable(target: Renderable, private val zOrder: Int)
    extends Renderable with Ordered {

  val order = zOrder

  def this(target: Renderable) {
    this(target, 0)
  }

  def render(r: Graphics2D, s: Point) {
    target.render(r, s)
  }
}

