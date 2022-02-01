package de.scrum_master.testing.kotlin

import spock.lang.Specification

class KotlinNonOpenClassTest extends Specification {
  def "fails without mock"() {
    given:
    def finalClassSample = new FinalClassSample()
    def classToBeTested = new ClassToBeTested(finalClassSample)

    when:
    classToBeTested.callMe()

    then:
    thrown IllegalAccessError
  }

  def "can mock definalised class"() {
    given:
    def finalClassSample = Mock(FinalClassSample)
    def classToBeTested = new ClassToBeTested(finalClassSample)

    when:
    classToBeTested.callMe()

    then:
    notThrown IllegalAccessError
  }
}
