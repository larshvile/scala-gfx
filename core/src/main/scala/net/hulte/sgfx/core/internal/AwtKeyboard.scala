package net.hulte.sgfx.core
package internal

import java.awt.{KeyboardFocusManager, KeyEventDispatcher}
import java.awt.event.KeyEvent

import scala.collection.mutable

private[core] object AwtKeyboard {
  
  def install = {
    val result = new AwtKeyboard
    KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(result)
    result
  }
}

private[core] final class AwtKeyboard extends KeyEventDispatcher with Keyboard {
  import Key._
  
  private val livePressedKeys = new mutable.HashSet[Key] with mutable.SynchronizedSet[Key]
  
  private var hit_ = Set[Key]()
  private var pressed_ = Set[Key]()
  
  override def hit = hit_
  override def pressed = pressed_
  
  def update() {
    val tmp = livePressedKeys.toSet
    hit_ = tmp -- pressed_
    pressed_ = tmp
  }
  
  override def dispatchKeyEvent(e: KeyEvent) = {
    key(e.getKeyCode()) foreach { k =>
      if (e.getID() == KeyEvent.KEY_PRESSED) livePressedKeys += k
      if (e.getID() == KeyEvent.KEY_RELEASED) livePressedKeys -= k
    }
    true
  }
  
  private def key(code: Int): Option[Key] = {
    code match {
      case KeyEvent.VK_LEFT => Some(Left)
      case KeyEvent.VK_RIGHT => Some(Right)
      case KeyEvent.VK_UP => Some(Up)
      case KeyEvent.VK_DOWN => Some(Down)
      case KeyEvent.VK_SPACE => Some(Space)
      case KeyEvent.VK_ENTER => Some(Enter)
      case KeyEvent.VK_ESCAPE => Some(Escape)
      case _ => None
    }
  }
}
