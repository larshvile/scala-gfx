package net.hulte.sgfx.graphics

import java.awt._

import Alignment._


/**
 * Companion object for Label with helper-methods for construction.
 */
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

  private var textWidth = -1
  private var textHeight = 0
  private var textDescent = 0

  override def render(r: Graphics2D, size: Point) {
    getFontMetrics(r)

    r.setFont(font)
    r.setColor(color)
    r.drawString(text, getX(size.x), getY(size.y))
  }

  private def getFontMetrics(r: Graphics2D) {
    if (textWidth == -1) { // might be an expensive op, so lets cache it
      val m = r.getFontMetrics(font)
      textWidth = m.stringWidth(text)
      textHeight = m.getHeight()
      textDescent = m.getDescent()
    }
  }

  private def getX(width: Int) = {
    align._1 match {
      case Left() => 0
      case Center() => (width - textWidth) / 2
      case Right() => width - textWidth
    }
  }

  private def getY(height: Int) = {
    align._2 match {
      case Top() => textHeight - textDescent
      case Middle() => ((height + textHeight) / 2) - textDescent
      case Bottom() => height - textDescent
    }
  }
  
  override def toString(): String = {
    return "Label(" + text + ")"
  }
}

