package simulaatio
import scala.math._

class Birdbox(val width: Int, val height: Int) {

  var birds = Array[Bird]()
  
  var origin = new Vector(width/2.0, height/2.0)

  def newBird() = {

    birds = birds :+ new Bird(Bird.randomStartPos(width, height), Bird.randomStartVel())

  }

  def moveBirds() = {

    for (b <- birds) {

      Movement.update(b, this)

      b.move()
    }

  }

}

