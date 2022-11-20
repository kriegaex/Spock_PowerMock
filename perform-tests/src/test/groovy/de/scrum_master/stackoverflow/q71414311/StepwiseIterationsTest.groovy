package de.scrum_master.stackoverflow.q71414311

import spock.lang.Rollup
import spock.lang.Stepwise
import spock.lang.Tag
import spock.lang.Unroll

import static de.scrum_master.testing.TestUtility.currentFixtureMethod

@Tag("failing")
class StepwiseIterationsTest extends BaseSpec {
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

  @Unroll("without skip #count")
  def "run all tests"() {
    println count
    expect:
    new UnderTest().isOk(count)

    where:
    count << (1..5)
  }

  @Stepwise
  @Unroll("with skip #count")
  def "run all tests with skip after first failure"() {
    println count
    expect:
    new UnderTest().isOk(count)

    where:
    count << (1..5)
  }

  @Stepwise
  @Rollup
  def "run all tests rolled-up with skip after first failure"() {
    println count
    expect:
    new UnderTest().isOk(count)

    where:
    count << (1..5)
  }

  //@Stepwise -> InvalidSpec Cannot use @Stepwise, feature method (...) is not data-driven
  def "not iterated"() {
    expect:
    true
  }

  static class UnderTest {
    boolean isOk(int iteration) {
      if (iteration == 2)
        return false//throw new RuntimeException("uh-oh")
      true
    }
  }

}
