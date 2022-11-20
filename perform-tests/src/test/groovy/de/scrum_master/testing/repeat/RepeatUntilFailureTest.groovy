package de.scrum_master.testing.repeat

import spock.lang.RepeatUntilFailure
import spock.lang.Specification

class RepeatUntilFailureTest extends Specification {
  @RepeatUntilFailure(maxAttempts = 2)
  def "repeated feature"() {
    println "xxx"
    expect:
    Integer.parseInt("123") == 123
  }
}
