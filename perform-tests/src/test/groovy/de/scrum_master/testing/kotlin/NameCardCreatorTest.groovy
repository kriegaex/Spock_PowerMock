package de.scrum_master.testing.kotlin

import org.apache.commons.codec.binary.Base64
import spock.lang.Specification

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

class NameCardCreatorTest extends Specification {
  def "check that base64 actually encodes an image"() {
    given:
    def nameCardCreator = new NameCardCreator("John Doe")

    when:
    def imageBase64 = nameCardCreator.createNameCard()
//    println imageBase64
    def imageBytes = new Base64().decode(imageBase64)
//    new FileOutputStream("name-card.png").write(imageBytes)
    def bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes))

    then:
    bufferedImage.width == 580
    bufferedImage.height == 380
  }

  def "check that name was written on card"() {
    given:
    def graphics2D = Mock(Graphics2D)
    def bufferedImage = Mock(BufferedImage) {
      // Return Graphics2D mock
      createGraphics() >> graphics2D
    }
    when:
    new NameCardCreator(bufferedImage, "John Doe").createNameCard()

    then:
    1 * graphics2D.drawString("John Doe", _, _)
  }

  def "exception during card creation leads to null result"() {
    given:
    def bufferedImage = Mock(BufferedImage) {
      // Throw an exception for each method call
      _(*_) >> { throw new IOException("uh-oh") }
    }
    expect:
    new NameCardCreator(bufferedImage, "John Doe").createNameCard() == null
  }

  def "exception during base64 encoding leads to null result"() {
    given:
    def bufferedImage = Mock(BufferedImage) {
      // Throw an exception for each method call
      _(*_) >> { throw new IOException("uh-oh") }
    }

    expect:
    new NameCardCreator().toBase64(bufferedImage) == null
  }
}
