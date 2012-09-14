package net.hulte.sgfx.core

import java.awt.{Graphics2D, Point}

/**
 * An entity able to render itself onto the screen using a {@link Graphics2D}. A renderable can be invoked
 * at any time, and can therefore not rely on any mutable state to perform the rendering.
 */
trait Renderable {

   def render(renderer: Graphics2D, screenSize: Point)

}
