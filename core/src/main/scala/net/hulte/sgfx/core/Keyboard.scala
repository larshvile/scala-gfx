package net.hulte.sgfx.core

object Key extends Enumeration {
  type Key = Value
  val Left, Right, Up, Down, Space, Enter, Escape = Value
}

/**
 * Provides keyboard-input from the user.
 */
trait Keyboard {
  import Key._

  /**
   * Returns the keys that have been pressed for the first time.
   */
  def hit: Set[Key]
  
  /**
   * Returns the keys that are currently pressed.
   */
  def pressed: Set[Key]
  
}
