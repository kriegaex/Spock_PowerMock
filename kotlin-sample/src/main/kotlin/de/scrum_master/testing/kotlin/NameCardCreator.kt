package de.scrum_master.testing.kotlin

import org.apache.commons.codec.binary.Base64OutputStream
import java.awt.BasicStroke
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import javax.imageio.ImageIO

class NameCardCreator {
  private var bufferedImage = BufferedImage(580, 380, BufferedImage.TYPE_INT_ARGB)
  private var name: String? = null
  val DISCLAIMER = "Name is Correct"

  constructor()

  constructor(name: String?) {
    this.name = name
  }

  constructor(bufferedImage: BufferedImage, name: String?) {
    this.bufferedImage = bufferedImage
    this.name = name
  }

  fun createNameCard(): String? {
    var generic64: String? = null
    try {
      val dashed = BasicStroke(3f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0f, floatArrayOf(5f), 0f)
      val graphics = bufferedImage.createGraphics()
      graphics.drawRoundRect(20, 20, 535, 340, 15, 15) // actual card size
      val fontMetrics = graphics.fontMetrics
      graphics.drawLine(300, 120, 540, 120)
      if (!name.isNullOrEmpty()) {
        graphics.drawString(name, 300, 110)
      }
      graphics.drawString(DISCLAIMER, 50, 290)
      generic64 = toBase64(bufferedImage)
      return generic64
    } catch (ie: IOException) {
      println("Name card could not be generated: $ie.message")
    }
    return generic64
  }

  protected fun toBase64(bufferedImage: BufferedImage): String? {
    var base64Str: String? = null
    var os: ByteArrayOutputStream? = null
    var b64: OutputStream? = null
    try {
      os = ByteArrayOutputStream()
      b64 = Base64OutputStream(os)
      ImageIO.write(bufferedImage, "png", b64)
      base64Str = os.toString("UTF-8")
    } catch (e: Exception) {
      println("Base64 encoding failed: $e.message")
    } finally {
      os?.close()
      b64?.close()
    }
    return base64Str
  }

}
