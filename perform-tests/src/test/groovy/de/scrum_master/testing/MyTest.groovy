package de.scrum_master.testing

import spock.lang.Specification

class MyTest extends Specification {
  def test() {
    println "MyTest"
    println "dev.sarek.agent.path = " + System.getProperty("dev.sarek.agent.path")
    expect:
    true
  }
}
