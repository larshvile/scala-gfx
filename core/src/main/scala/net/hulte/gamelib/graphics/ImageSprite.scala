package net.hulte.gamelib.graphics

import java.awt._
import java.awt.image.BufferedImage


/**
 * Represents a static image.
 */
class ImageSprite(private val img: Image) extends Sprite[Unit] {

  val pos: Point = new Point(0, 0)

  def this(img: Image, pos: Point) = {
    this(img)
    this.pos.x = pos.x
    this.pos.y = pos.y
  }

  def size: Point = img.size

  protected def draw(state: Unit, pos: Point,
      renderer: Graphics2D, screenSize: Point) = {
    img.draw(renderer, (r: Graphics2D, i: BufferedImage) => {
      r.drawImage(i, pos.x, pos.y, null)
    })
  }

  protected def getState(): Unit = {}
}

