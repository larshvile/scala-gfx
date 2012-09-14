package net.hulte.sgfx
package graphics

import core.{Ordered, Renderable}
import Ordered._

import java.awt.{Graphics2D, Point}

/**
 * Simple composite of {@link Renderable}. If any of the children define the {@link Ordered} trait, their
 * ordering controls the Z-order drawing within the composite.
 */
class Composite(private val zOrder: Int) extends Renderable with Ordered {

  private var children = List[Renderable]()
  val order = zOrder

  def this() {
    this(0)
  }

  def add(renderable: Renderable): Composite = {
    children = renderable :: children
    this
  }

  def render(renderer: Graphics2D, screenSize: Point) {
    for (c <- children.sortBy(orderOf(_))) {
      c.render(renderer, screenSize)
    }
  }
}
