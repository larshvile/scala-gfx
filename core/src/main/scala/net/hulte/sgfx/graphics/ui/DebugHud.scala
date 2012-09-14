package net.hulte.sgfx
package graphics
package ui

import Alignment._
import core.Ordered._

/**
 * A simple heads-up-display containing debugging information.
 */
final class DebugHud(fps: Double) extends Composite(Front) {
  add(Label.create("FPS: " + "%.1f".format(fps), (Right(), Bottom())))
}
