package net.hulte.sgfx
package graphics

import geom.Point

import java.awt.Graphics2D
import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage

/**
 * A static image.
 */
final class ImageSprite(private val img: BufferedImage) extends Sprite {

  size = Point(img.getWidth, img.getHeight)

  override protected def draw(center: Point, size: Point, r: Graphics2D, screenSize: Point) {
    val xForm = new AffineTransform(r.getTransform())
    xForm.translate(center.x - size.x / 2, center.y - size.y / 2)
    r.drawImage(img, xForm, null)
  }
}
