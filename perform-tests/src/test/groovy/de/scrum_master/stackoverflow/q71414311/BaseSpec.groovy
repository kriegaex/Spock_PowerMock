package de.scrum_master.stackoverflow.q71414311

import spock.lang.Specification

import static de.scrum_master.testing.TestUtility.currentFixtureMethod

class BaseSpec extends Specification {
  def setupSpec() {
    println currentFixtureMethod
  }

  def cleanupSpec() {
    println currentFixtureMethod
  }

  def setup() {
    println currentFixtureMethod
  }

  def cleanup() {
    println currentFixtureMethod
  }
}
