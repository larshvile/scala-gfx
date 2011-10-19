package net.hulte.gamelib.graphics

import java.awt.{Graphics2D, Point}


/**
 * Simple Renderable-composite, used as a container for other Renderables. If any of
 * the children define the Ordered trait, their ordering controls the Z-order drawing.
 * Renderables without the Ordered trait are considered to have an ordering of 0.
 *
 * @author lars
 */
class Composite(private val zOrder: Int) extends Renderable with Ordered {

  private var children = List[Renderable]()
  val order = zOrder

  def this() {
    this(0)
  }

  /**
   * Adds a new Renderable to this composite.
   */
  def add(renderable: Renderable): Composite = {
    children = renderable :: children
    this
  }

  def render(renderer: Graphics2D, screenSize: Point) {
    for (c <- children.sortBy(getOrder(_))) {
      c.render(renderer, screenSize)
    }
  }

  def getOrder(o: AnyRef): Int = {
    o match {
      case (o: Ordered) => o.order
      case (x) => 0
    }
  }
}

