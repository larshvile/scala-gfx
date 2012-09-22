package net.hulte.sgfx
package core

import geom.Point

/**
 * The classic, gaming logic-loop. Invoked by the framework to process a single frame.
 */
trait LogicLoop {

  def processFrame(screenSize: Point, timer: Timer, keyboard: Keyboard): List[Renderable]

}
