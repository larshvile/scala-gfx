package net.hulte.sgfx.core

/**
 * An entity that knows it's z-ordering.
 */
trait Ordered {
  def order: Int
}

object Ordered {
  
  val Front = Int.MaxValue
  val Back = Int.MinValue
  
  def orderOf(o: AnyRef): Int = {
    o match {
      case (o: Ordered) => o.order
      case (x) => 0
    }
  }
}
