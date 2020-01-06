package simulaatio
import scala.math._

class Vector(var i: Double, var j: Double) {

  def length = sqrt(pow(this.i, 2) + pow(this.j, 2))

  def isNullVector = i == 0 && j == 0

}

object Vector {

  def add(v1: Vector, v2: Vector) = {

    var i = v1.i + v2.i

    var j = v1.j + v2.j

    new Vector(i, j)

  }

  def addAll(vectors: Array[Vector]) = {

    var v = new Vector(0, 0)

    for (v2 <- vectors) {
      v = add(v, v2)
    }

    v

  }

  def multp(v: Vector, sc: Double) = {

    var i = v.i * sc

    var j = v.j * sc

    new Vector(i, j)

  }

  def sub(v1: Vector, v2: Vector) = {

    var i = v1.i - v2.i

    var j = v1.j - v2.j

    new Vector(i, j)

  }

  def dotP(v1: Vector, v2: Vector) = {

    v1.i * v2.i + v1.j * v2.j
  }

  def angle(v1: Vector, v2: Vector): Option[Double] = {

    if (!(v1.isNullVector || v2.isNullVector)) {
      Some(acos(abs(this.dotP(v1, v2)) / (v1.length * v2.length)))
    } else {
      None
    }

  }

  def limitBtw(v: Vector, min: Double, max: Double) = {

    var c: Double = 1

    if (v.length < min) {
      c = min / v.length
    } else if (v.length > max) {
      c = max / v.length
    }

    multp(v, c)

  }

}