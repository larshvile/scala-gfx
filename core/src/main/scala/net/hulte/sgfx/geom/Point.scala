package net.hulte.sgfx.geom

object Point {
  val Zero = Point(0, 0)
  
  implicit def fromCoords(c: (Double, Double)): Point = Point(c._1, c._2)
  implicit def fromAwtPoint(p: java.awt.Point) = Point(p.x, p.y)
}

case class Point(val x: Double, val y: Double) {
    def +(rhs: Point) = Point(x + rhs.x, y + rhs.y)
    def -(rhs: Point) = Point(x - rhs.x, y - rhs.y)
    def /(rhs: Double)  = Point(x / rhs, y / rhs)
}
