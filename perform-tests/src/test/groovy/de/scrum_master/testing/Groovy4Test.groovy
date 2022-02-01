package de.scrum_master.testing

import spock.lang.Specification

class Groovy4Test extends Specification {
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
