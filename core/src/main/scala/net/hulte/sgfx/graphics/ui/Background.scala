package net.hulte.sgfx
package graphics
package ui

import core.{Ordered, Renderable}
import Ordered._

import java.awt.{Color, Graphics2D, Point}

/**
 * Fills the background with a color.
 */
final class Background(val color: Color) extends Renderable with Ordered {
  
  def order = Back
  
  def render(r: Graphics2D, size: Point) {
      r.setColor(color)
      r.fillRect(0, 0, size.x, size.y)
  }
}
