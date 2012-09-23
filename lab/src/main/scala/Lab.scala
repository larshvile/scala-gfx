import net.hulte.sgfx._
import core._
import geom._
import graphics._
import ui._
import Key._
import Ordered._

import java.io.File
import java.awt.{Color, Graphics2D}
import Color._

import scala.math._

object Lab extends scala.App {
  Application.create(800, 600, new Lab())
}

/**
 * Development sandbox...
 */
private[this] class Lab extends LogicLoop {
  override def toString = "The SGFX Laboratory"

  val horse = new ImageSprite(ImageIo.load(
    new File("/home/lars/tmp/Heroes_of_Might_and_Magic_II/CMBTHROB0006.png")))
  val halfHorse = horse.size / 2

  val meter = 50.
  val g = 9.8
  
  var v = Point.Zero    // TODO vectors...
  var a = Point.Zero
  
  def processFrame(screenSize: Point, timer: Timer, keyboard: Keyboard) = {
    
    if (keyboard.hit contains Escape) {
      Application.destroy()
    }
    
    // move that horse...
    if (grounded(horse)) {
      
      // sideways motion
      v = Point(displacement(keyboard.pressed).x, v.y)

      // friction, sort of.. 
      v = Point(v.x, 0)
      a = Point.Zero

      // jump
      if (keyboard.hit contains Space) {
        v = Point(v.x, -5)
      }
    } else {
      
      // sideways mid-air motion
      a = Point(displacement(keyboard.pressed).x * .3, a.y)
      
      // gravity
      a = Point(a.x, g)
    }
    
    // apply acceleration
    v = v + a * timer.frameSecond
  
    // apply velocity
    horse.center = horse.center + v * meter * timer.frameSecond
  
    // stay within bounds
    val x = max(halfHorse.x, min(horse.center.x, screenSize.x - halfHorse.x)) 
    val y = max(halfHorse.y, min(horse.center.y, screenSize.y - halfHorse.y))
    horse.center = (x, y)

    horse :: new DebugHud(timer.fps) :: new Background(BLACK) ::  Nil 
  }
  
  def displacement(keys: Set[Key]): Point = {
    (Point.Zero /: keys.map(displacement _))((a, b) => a + b)
  }
  
  def displacement(k: Key): Point = {
    k match {
      case Left => (-5 , 0)
      case Right => (5 , 0)
      case _ => Point.Zero
    }
  }
  
  def grounded(horse: Sprite) = {
    horse.center.y >= 600 - halfHorse.y // TODO 600, hacky
  }
}
