package net.hulte.sgfx.core


/**
 * Basic contract for the individual games. Outlines the parts of games that the
 * framework needs to know about.
 * 
 * @author lars
 */
trait Game {

  /**
   * Tha name of tha game.
   */
  def name: String

  /**
   * Called once for each frame of the drawing-loop. This is the place
   * to implement all game-logic.
   */
  def process(screen: Screen, timer: Timer, keyboard: Keyboard)

}

