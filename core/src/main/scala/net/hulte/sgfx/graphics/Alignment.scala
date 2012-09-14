package net.hulte.sgfx.graphics

object Alignment {

  abstract sealed class Horizontal
  case class Left() extends Horizontal
  case class Center() extends Horizontal
  case class Right() extends Horizontal

  abstract sealed class Vertical
  case class Top() extends Vertical
  case class Middle() extends Vertical
  case class Bottom() extends Vertical

  type Alignment = (Horizontal, Vertical)
}
