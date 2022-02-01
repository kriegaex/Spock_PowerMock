package de.scrum_master.testing

import org.mockito.Mockito
import spock.lang.Specification

class AbstractClassTest extends Specification {
  def "can mock abstract class"() {
    given:
    def mock = Mock(AbstractClass)

    expect:
    mock.doSomething() == null
    mock.doSomethingElse() == 0
  }

  def "can stub abstract class"() {
    given:
    def mock = Mock(AbstractClass) {
      doSomething() >> "stubbed"
      doSomethingElse() >> 11

    }

    expect:
    mock.doSomething() == "stubbed"
    mock.doSomethingElse() == 11
  }

  def "can mock and stub abstract class with Mockito"() {
    given:
    def mock = Mockito.mock(AbstractClass)
    Mockito.when(mock.doSomething()).thenReturn("stubbed")
    Mockito.when(mock.doSomethingElse()).thenReturn(11)

    expect:
    mock.doSomething() == "stubbed"
    mock.doSomethingElse() == 11
  }
}
