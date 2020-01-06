package gui

import simulaatio._

import scala.swing._
import java.awt.{ Color, Graphics2D, RenderingHints }
import java.awt.event.ActionListener
import javax.swing.event._
import o1.util._
import o1.gui._
import o1.gui.mutable._
import o1.gui.layout._
import scala.swing._
import scala.swing.event.ButtonClicked
import java.awt.Dimension
import javax.swing.JCheckBox
import scala.swing.CheckBox

object Flocksimulation extends SimpleSwingApplication {

  val width = 1500

  val height = 1080

  val birdCountMax = 40

  var birdCount = 0

  var startCounter: Double = 0

  def top = new MainFrame {

    var birdbox = new Birdbox(width, height)
    title = "Parvisimulaatio"
    resizable = false

    minimumSize = new Dimension(width + 420, height)
    preferredSize = new Dimension(width + 420, height)
    maximumSize = new Dimension(width + 420, height)

    val box = new Panel {

      minimumSize = new Dimension(width, height)
      preferredSize = new Dimension(width, height)
      maximumSize = new Dimension(width, height)

      override def paintComponent(g: Graphics2D) = {

        g.setColor(Color.white)
        g.fillRect(0, 0, width, height)

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        birdbox.birds.foreach(_.draw(g)) //käsketään jokaista lintuoliota piirtämään itsensä

      }

    }

    val startButton = new Button("Start Over") {

      minimumSize = new Dimension(150, 50)
      preferredSize = new Dimension(150, 50)
      maximumSize = new Dimension(150, 50)

    }
    
    val velVec = new CheckBox("Draw Velocity Vectors"){
      minimumSize = new Dimension(20, 20)
    }
    
    val innerCircle = new CheckBox("Draw Safespaces"){
      minimumSize = new Dimension(20, 20)
    }
    
    val outerCircle = new CheckBox("Draw Radars"){
      minimumSize = new Dimension(20, 20)
    }
    
    
    
    
    val buttonRow = new BoxPanel(Orientation.Vertical) { contents.append(startButton, innerCircle, outerCircle, velVec) }

    this.listenTo(startButton,innerCircle, outerCircle, velVec)
    this.reactions += {
      case ButtonClicked(`startButton`) => {
        birdbox = new Birdbox(width, height)
        birdCount = 0
      }
      case ButtonClicked(`innerCircle`) => {
        if(innerCircle.selected){
          Settings.setDrawIn(true)
        }else{
          Settings.setDrawIn(false)
        }
        
      }
      case ButtonClicked(`outerCircle`) => {
        if(outerCircle.selected){
          Settings.setDrawOut(true)
        }else{
          Settings.setDrawOut(false)
        }
        
      }
      case ButtonClicked(`velVec`) => {
        if(velVec.selected){
          Settings.setDrawVel(true)
        }else{
          Settings.setDrawVel(false)
        }
        
      }

    }

    val maxSpeedSlider: Setting = new Setting("Maximum Speed", 5, Bird.maxSpeed.toInt, 20, _ + "m/s") {
      override def onAdjust() = {
        Bird.setMaxSpeed(this.value)
      }
    }

    val gravitySlider: Setting = new Setting("Gravity Towards Flock Centre", 1, (Settings.gravityTowardsCoM* 10000).toInt, 10, _ + "") {
      override def onAdjust() = {
        Settings.setGravityCOM(this.value / 10000.0)
      }
    }

    val influenceSlider: Setting = new Setting("Herd Behaviour", 5, (Settings.closeByBirdsInfluence* 1000).toInt, 100, _ + "") {
      override def onAdjust() = {
        Settings.setInf(this.value / 1000.0)
      }
    }

    val avoidanceSlider: Setting = new Setting("Avoidance", 5, (Settings.avoidance * 100).toInt, 20, _ + "") {
      override def onAdjust() = {
        Settings.setAvoidance(this.value / 100.0)
      }
    }

    val safeSpaceRadiusSlider: Setting = new Setting("Safespace radius", 15, Settings.safeSpace.toInt, 80, _ + " (Playarea is " + width + "x" + height + ")") {
      override def onAdjust() = {
        Settings.setSafeSpace(this.value)
      }
    }

    val gravityOSlider: Setting = new Setting("Gravity Towards Origo", 5, (Settings.gravityTowardsOrigo* 10000).toInt, 15, _ + "") {
      override def onAdjust() = {
        Settings.setGravityO(this.value / 10000.0)
      }
    }

    val visibilitySlider: Setting = new Setting("Visibility", 0, 400, 400, _ + "") {
      override def onAdjust() = {
        Settings.setVisibility(this.value)
      }
    }

    val settingspanel = new BoxPanel(Orientation.Vertical) {

      contents +=buttonRow
      contents += maxSpeedSlider
      contents += gravitySlider
      contents += influenceSlider
      contents += safeSpaceRadiusSlider
      contents += avoidanceSlider
      contents += gravityOSlider
      contents += visibilitySlider

    }

    contents = new BoxPanel(Orientation.Horizontal) {
      contents += box
      contents += settingspanel
      
    }

    /*contents = new EasyPanel {
      placeN(box, (0, 0), TwoWide, FillBoth(1, 1), (2, 2, 2, 2))

      placeN(startButton, (0, 1), TwoWide, Slight, NoBorder)

      placeNW(maxSpeedSlider, (0, 2), OneSlot, FillHorizontal(1), NoBorder)
      placeNW(avoidanceSlider, (1, 2), OneSlot, FillHorizontal(1), NoBorder)
      placeNE(influenceSlider, (0, 3), OneSlot, FillHorizontal(1), NoBorder)
      placeNE(gravitySlider, (1, 3), OneSlot, FillHorizontal(1), NoBorder)
      placeNE(safeSpaceRadiusSlider, (0, 4), OneSlot, FillHorizontal(1), NoBorder)

    }*/

    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) = {

        if (birdCount < birdCountMax && startCounter % 100 == 0) {

          startCounter = 0

          birdbox.newBird()

          birdCount += 1
        }

        startCounter += 1

        birdbox.moveBirds()
        box.repaint()

      }
    }

    val timer = new javax.swing.Timer(1, listener)
    timer.start()

  }

}