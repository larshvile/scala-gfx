package net.hulte.sgfx.core

/**
 * The classic, gaming logic-loop. Invoked by the framework to process a single frame.
 */
trait LogicLoop {

  def processFrame(screen: Screen, timer: Timer, keyboard: Keyboard)

}
