package de.scrum_master.testing

import spock.lang.Specification

class RepeatedTest extends Specification {
  def "test run #run, parameters [#param1, #param2]"() {
    // See https://github.com/spockframework/spock/issues/1285#issuecomment-790764765
    where:
    [[param1, param2], run] << [
      [
        ['a', 'b'],
        ['c', 'd']
      ],
      (1..5)
    ].combinations()
  }

  def "nested multi-assignment, parameters [#a, #b, #c]"() {
    // See https://spockframework.org/spock/docs/2.0-M4/all_in_one.html#_multi_variable_data_pipes
    where:
    [a, [b, _, c]] << [
      ['a1', 'a2'].permutations(),
      [
        ['b1', 'd1', 'c1'],
        ['b2', 'd2', 'c2']
      ]
    ].combinations()
  }

  def "my feature"() {
    new Exception().printStackTrace()
    given: true
    and: true
    and: true

    expect: true

    when: true
    and: true

    then: true
    and: true

    then: true

    expect: true
    and: true

    cleanup: true
  }
}
