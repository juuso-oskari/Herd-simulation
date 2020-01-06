package simulaatio

import java.awt.Graphics2D


import javax.imageio.ImageIO
import scala.util.Random
import java.io.File
import java.awt.Toolkit;
import java.awt.geom.AffineTransform
import java.awt.{ Color, BasicStroke, Graphics2D, RenderingHints }
import java.awt.Shape._
import java.awt.geom.Ellipse2D

class Bird(var pos: Vector, var vel: Vector) {

  def move() = {

    pos = Vector.add(pos, Vector.multp(vel, 0.1))

  }

  def updateVel(infVector: Vector) = {

    vel = Vector.add(this.vel, infVector)

    vel = Vector.limitBtw(vel, Bird.minSpeed, Bird.maxSpeed)

  }

  def draw(g: Graphics2D) = {

    var r = 10

    var x = pos.i.toInt - r / 2
    
    var xS = pos.i - Settings.safeSpace/2
    
    var xV = pos.i - Settings.visibility/2

    var y = pos.j.toInt - r / 2
    
    var yS = pos.j - Settings.safeSpace/2
    
    var yV = pos.j - Settings.visibility/2

    g.setColor(Color.BLACK)
    g.fillOval(x, y, r, r)
    
    if(Settings.drawVel){
      g.drawLine(pos.i.toInt, pos.j.toInt, (x + vel.i.toInt), (y + vel.j.toInt))
    }
    
    
    
    if(Settings.drawInnerCircle){
      g.setColor(Color.RED)
      g.draw(new Ellipse2D.Double(xS, yS, Settings.safeSpace, Settings.safeSpace))
    }
    if(Settings.drawOuter){
      g.setColor(Color.GREEN)
      g.draw(new Ellipse2D.Double(xV, yV, Settings.visibility, Settings.visibility))
    }
   

  }

}

object Bird {

  var radarAngle: Double = 3 * math.Pi / 4.0

  var maxSpeed: Double = 14

  def setMaxSpeed(d: Double) = maxSpeed = d

  var minSpeed: Double = maxSpeed / 2

  def randomStartVel() = {

    var lowLimit = maxSpeed * (-1)

    var r = new Random()

    var i = r.nextDouble() * (maxSpeed - lowLimit) + lowLimit
    var j = r.nextDouble() * (maxSpeed - lowLimit) + lowLimit

    var v = new Vector(i, j)

    v = Vector.limitBtw(v, minSpeed, maxSpeed)

    v

  }

  def randomStartPos(width: Int, height: Int) = {

    new Vector(width / 2.0, height / 2.0)

  }

}




