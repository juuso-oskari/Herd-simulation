

package gui

import o1._

import o1.util._
import o1.gui.layout._
import scala.swing._
import scala.swing.event.{ ValueChanged }
import java.awt.Dimension

// A single, labeled slider-based setting in the GUI that selects and Int value.
private[gui] class Setting(name: String, minValue: Int, initialValue: Int, maxValue: Int, val format: Int => String) extends EasyPanel {

  private val slider = new Slider {
    orientation = Orientation.Horizontal
    min = minValue
    value = initialValue
    max = maxValue
    majorTickSpacing = 1
    paintTicks = true
    minimumSize = new Dimension(200, 40)
  }
  private var previousValue = initialValue
  private val nameLabel = new Label(name)
  private val valueLabel = new Setting.FixedLabel { horizontalAlignment = Alignment.Left }

  this.updateLabel()

  this.placeNW(nameLabel, (0, 0), TwoWide, Slight, NoBorder)
  this.placeW(slider, (0, 1), OneSlot, Slight, (0, 2, 0, 4))
  this.placeNW(valueLabel, (1, 1), OneSlot, FillHorizontal(1), (0, 2, 2, 4))

  this.listenTo(slider)
  this.reactions += {
    case ValueChanged(source) if source == slider =>
      if (this.releasedAtNewValue) {
        this.previousValue = this.value
        onAdjust()
      }
      this.updateLabel()
  }

  private def releasedAtNewValue = !this.slider.adjusting && this.previousValue != this.value

  def updateLabel() = {
    valueLabel.text = this.format(this.value)
  }

  def value = this.slider.value

  def onAdjust() = {

  }

  override def enabled_=(value: Boolean) = {
    super.enabled = value
    this.slider.enabled = value
    this.nameLabel.enabled = value
    this.valueLabel.enabled = value
  }

}

private object Setting {

  private class FixedLabel extends Label { // later: refactor
    private var fixedSize: Option[Dimension] = None
    override def text_=(newText: String) = {
      super.text = newText
      this.fixedSize match {
        case Some(fixed) =>
          this.preferredSize = fixed
        case None =>
          this.fixedSize = Some(this.preferredSize)

      }
    }
  }

}