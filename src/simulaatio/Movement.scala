package simulaatio

import scala.util.Random

import scala.math._

object Movement {

  def keepUp(b: Bird, birds: Array[Bird], visibility: Double): Option[Vector] = {

    var nrby = Array[Vector]()

    var v: Option[Vector] = None

    for (b2 <- birds) {
      if (Vector.sub(b.pos, b2.pos).length < visibility) {
        nrby = nrby :+ b2.vel
      }
    }

    if (nrby.size != 0) {
      v = Some(Vector.multp(Vector.addAll(nrby), Settings.closeByBirdsInfluence / nrby.size))
    }

    v

  }

  def centerOfMass(birds: Array[Bird]) = {

    var comVector = new Vector(0, 0)

    for (b <- birds) {
      comVector = Vector.add(comVector, b.pos)
    }

    if (birds.size != 0) {
      comVector = Vector.multp(comVector, 1.0 / birds.size)
    }

    comVector

  }

  def gravitateToFlockCenter(b: Bird, birds: Array[Bird]) = {

    Vector.multp(Vector.sub(centerOfMass(birds), b.pos), Settings.gravityTowardsCoM)

  }
  
  def gravitateToMiddle(b: Bird, area: Birdbox)={
    
    var rand = new Random()
    
    var r = Vector.sub(Vector.add(area.origin,new Vector(rand.nextInt(70), rand.nextInt(70))), b.pos)
    
    var offset= Vector.multp(r, 0.001)
    
    offset
    
  }

  def avoidCollision(b: Bird, birds: Array[Bird]) = {

    var p = b.pos

    var offset = new Vector(0, 0)

    var r = new Vector(0, 0)

    var div = 1

    for (b2 <- birds) {

      r = Vector.sub(p, b2.pos)

      if (b.vel.isNullVector || r.isNullVector) {

      } else if ((r.length < Settings.safeSpace) && (Vector.angle(b.vel, (Vector.multp(r, -1))).get < Bird.radarAngle)) {
        offset = Vector.multp(Vector.add(offset, Vector.sub(p, b2.pos)), 1.0 / (4 * pow(math.E, r.length)) + Settings.avoidance)
        div += 1
      }

    }

    if (div > 1) div -= 1

    offset = Vector.multp(offset, 1.0 / div)

    offset

  }

  def update(b: Bird, area: Birdbox) = {

    var birds = area.birds
    
    var infVector = keepUp(b, birds, Settings.visibility)

    var comVector = gravitateToFlockCenter(b, birds)

    var v = b.vel

    if (infVector != None) {
      v = Vector.add(Vector.add(v, infVector.get), comVector)
    } else {
      v = Vector.add(v, comVector)
    }

    var collisionVector = avoidCollision(b, birds)

    v = Vector.add(v, collisionVector)
    
    v= Vector.add(v, gravitateToMiddle(b, area))

    b.updateVel(v)

  }

}

object Settings {

  var visibility: Double = 400
  
  def setVisibility(d: Double)= visibility=d

  var gravityTowardsCoM: Double = 0.0001

  def setGravityCOM(d: Double) = gravityTowardsCoM = d
  
  var gravityTowardsOrigo = 0.0005
  
  def setGravityO(d: Double)= gravityTowardsOrigo=d

  var closeByBirdsInfluence: Double = 0.1

  def setInf(d: Double) = closeByBirdsInfluence = d

  var safeSpace = 80.0

  def setSafeSpace(d: Double) = safeSpace = d

  var avoidance = 0.1

  def setAvoidance(d: Double) = avoidance = d
  
  var drawInnerCircle = false
  
  def setDrawIn(b: Boolean)= drawInnerCircle=b
  
  var drawOuter = false
  
  def setDrawOut(b: Boolean)= drawOuter=b
  
  var drawVel = false
  
  def setDrawVel(b: Boolean)= drawVel=b
  
  

}
