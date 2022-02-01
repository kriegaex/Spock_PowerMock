package de.scrum_master.stackoverflow.q70661506

import spock.lang.Shared
import spock.lang.Specification

class ReferringToDataTableVariablesTest extends Specification {
  @Shared
  final n = 8

  def "feature [#iterationIndex]: #testNumber, #testRange"() {
    println "$testNumber, $testRange, ${testClosure()}"

    expect:
    true

    where:
    testNumber << (1..n)
    testRange = testNumber + 3..2 * testNumber
    testClosure = { -> testNumber / 2 > 2 }
  }
}
