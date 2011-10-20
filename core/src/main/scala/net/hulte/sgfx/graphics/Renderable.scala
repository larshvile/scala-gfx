package net.hulte.sgfx.graphics

import java.awt.{Graphics2D, Point}


/**
 * An entity that knows how to render itself using a <code>Graphics</code>.
 *
 * @author lars
 */
 trait Renderable {

   def render(renderer: Graphics2D, screenSize: Point)

}

