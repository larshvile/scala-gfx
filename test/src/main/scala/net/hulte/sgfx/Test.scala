package net.hulte.sgfx

import java.io.File
import java.awt._

import core._
import graphics._
import net.hulte.sgfx.graphics.Image


object Test extends scala.App {
  Application.create(800, 600, new Test())
}


private[this] class Test extends Game {

  val name = "Test"

  val img = new ImageSprite(Image.load(
    new File("/home/lars/tmp/Heroes_of_Might_and_Magic_II/CMBTHROB0006.png")),
    new Point(150, 150))
    
  var xDir = true
  var yDir = true


  def process(screen: Screen, timer: Timer, keyboard: Keyboard) {

    // clear the background
    screen.add(new Renderable with Ordered {
      val order = Int.MinValue
      override def render(r: Graphics2D, size: Point) {
        r.setColor(new Color(0, 0, 0))
        r.fillRect(0, 0, size.x, size.y)
      }
    })

    // TODO have some fun here please..
    img.pos.x += (if (xDir) 1 else -1)
    img.pos.y += (if (yDir) 1 else -1)

    if ((img.pos.x <= 0) || (img.pos.x >= (screen.size.x - img.size.x))) xDir = !xDir
    if ((img.pos.y <= 0) || (img.pos.y >= (screen.size.y - img.size.y))) yDir = !yDir

    screen.add(img)

    screen.add(new Renderable() {
      override def render(r: Graphics2D, size: Point) {
        r.setColor(new Color(255, 0, 0))
        r.drawLine(0, size.y / 2, size.x, size.y / 2)
        r.drawLine(size.x / 2, 0, size.x / 2, size.y)
        r.fillRect(50, 50, 100, 65)
      }
    })
  }
}

