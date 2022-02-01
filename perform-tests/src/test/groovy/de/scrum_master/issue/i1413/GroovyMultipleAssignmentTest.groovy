package de.scrum_master.issue.i1413

import spock.lang.Specification

class GroovyMultipleAssignmentTest extends Specification {

  def "Tuple with cleanup"() {
    when:
    def (first, second) = getArray()

    then:
    first
    !second

    cleanup:
    println 'cleanup'
  }

  private Boolean[] getArray() {
    [true, false]
  }
}
