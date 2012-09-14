package net.hulte.sgfx
package graphics
package ui

import core.Renderable
import graphics.Alignment._

import java.awt._

object Label {

  val DefaultFontSize = 14
  val DefaultFontFace = "Verdana"
  val DefaultFont = new Font(DefaultFontFace, Font.PLAIN, DefaultFontSize)
  val DefaultColor = new Color(255, 255, 255)

  /**
   * Creates a new label using the default font/colors.
   */
  def create(text: String, align: Alignment): Label =
    new Label(text, DefaultColor, DefaultFont, align)

}

/**
 * Simple label used to display a line of text.
 */
final class Label(text: String, color: Color, font: Font,
    align: Alignment) extends Renderable {

  override def render(r: Graphics2D, size: Point) {
    val metrics = r.getFontMetrics(font)

    r.setFont(font)
    r.setColor(color)
    r.drawString(text, getX(metrics, size.x), getY(metrics, size.y))
  }

  private def getX(metrics: FontMetrics, width: Int) = {
    val textWidth = metrics.stringWidth(text)
    align._1 match {
      case Left() => 0
      case Center() => (width - textWidth) / 2
      case Right() => width - textWidth
    }
  }

  private def getY(metrics: FontMetrics, height: Int) = {
    val textHeight = metrics.getHeight()
    val textDescent = metrics.getDescent()
    align._2 match {
      case Top() => textHeight - textDescent // TODO what about the ascent??
      case Middle() => ((height + textHeight) / 2) - textDescent
      case Bottom() => height - textDescent
    }
  }
}
