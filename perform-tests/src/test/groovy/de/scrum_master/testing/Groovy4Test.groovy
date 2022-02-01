package de.scrum_master.testing

import org.spockframework.compiler.SpockTransform
import org.spockframework.util.GroovyReleaseInfo
import org.spockframework.util.SpockReleaseInfo
import org.spockframework.util.VersionChecker
import org.spockframework.util.VersionNumber
import spock.lang.Specification

import static org.spockframework.util.SpockReleaseInfo.isCompatibleGroovyVersion

class Groovy4Test extends Specification {
  def setupSpec() {
    def groovyVersion = GroovyReleaseInfo.getVersion()
    def disableVersionCheck = VersionChecker.DISABLE_GROOVY_VERSION_CHECK_PROPERTY_NAME
    println "Groovy version = $groovyVersion"
    println "$disableVersionCheck = ${System.getProperty(disableVersionCheck)}"
    println "Is Groovy version compatible? ${isCompatibleGroovyVersion(groovyVersion)}"

    // This should be executed automatically, but is not. Calling it manually, triggers the version check.
    // new SpockTransform()

    // This is what the SpockTransform constructor actually does. Calling it manually, triggers the version check.
    // new VersionChecker().checkGroovyVersion("Xander")
  }

  def "use switch expression"() {
    given:
    def i = 2
    def result = switch(i) {
      case 0 -> 'zero'
      case 1 -> 'one'
      case 2 -> 'two'
      default -> throw new IllegalStateException('unknown number')
    }

    expect:
    GroovySystem.version.startsWith("4.")
    result == "two"
  }

  def "use sealed classes"() {
    given:
    def threeDayForecast = [
      new Rainy(rainfall: 12),
      new Sunny(temp: 35),
      new Cloudy(uvIndex: 6)
    ]

    expect:
    GroovySystem.version.startsWith("4.")
    threeDayForecast.size() == 3
    threeDayForecast[1] instanceof Sunny
  }
}

sealed abstract class Weather { }
final class Rainy extends Weather { Integer rainfall }
final class Sunny extends Weather { Integer temp }
final class Cloudy extends Weather { Integer uvIndex }
