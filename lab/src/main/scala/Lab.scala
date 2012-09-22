import net.hulte.sgfx._
import core._
import geom._
import graphics._
import ui._
import Ordered._

import java.io.File
import java.awt.{Color, Graphics2D}
import java.awt.Rectangle
import Color._

object Lab extends scala.App {
  Application.create(800, 600, new Lab())
}

/**
 * Development sandbox...
 */
private[this] class Lab extends LogicLoop {

  val horse = new ImageSprite(ImageIo.load(
    new File("/home/lars/tmp/Heroes_of_Might_and_Magic_II/CMBTHROB0006.png")))

  var xDir = true
  var yDir = true

  override def toString = "The SGFX Laboratory"

  def processFrame(screenSize: Point, timer: Timer, keyboard: Keyboard) = {
    
    // move that horse...
    val xMove = (if (xDir) 1 else -1) * 200 * timer.frameSecond
    val yMove = (if (yDir) 1 else -1) * 150 * timer.frameSecond
    horse.center = horse.center + (xMove, yMove)
    
    val size = horse.size / 2
    
    if ((horse.center - size).x <= 0) xDir = true
    if ((horse.center + size).x >= screenSize.x) xDir = false
    if ((horse.center - size).y <= 0) yDir = true
    if ((horse.center + size).y >= screenSize.y) yDir = false

    horse :: new DebugHud(timer.fps) :: new Background(BLACK) :: Nil 
  }
}