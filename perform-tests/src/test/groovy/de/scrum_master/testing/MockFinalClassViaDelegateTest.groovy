package de.scrum_master.testing

import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Specification

import static de.scrum_master.testing.JavaAgentDetector.isRemoveFinalAgentActive
import static java.lang.reflect.Modifier.isFinal

class MockFinalClassViaDelegateTest extends Specification {
  @Requires({ removeFinalAgentActive })
  def "Spy on original"() {
    given: "a final class instance with an injected mock collaborator"
    def collaborator = Mock(Collaborator)
    def underTest = Spy(new UnderTest(collaborator))

    when: "calling a specific method in the class under test"
    underTest.sayHello("world")

    then: "a specific method in the collaborator gets called"
    1 * collaborator.collaborate()
  }

  @IgnoreIf({ removeFinalAgentActive })
  def "Spy on delegate"() {
    given: "a final class instance with an injected mock collaborator"
    def collaborator = Mock(Collaborator)
    def delegateTarget = new UnderTest(collaborator)

    and: "a spy on a @Delegate class (trick to 'mock the unmockable')"
    def underTest = Spy(new UnderTestDelegate(underTest: delegateTarget))

    when: "calling a specific method in the class under test"
    underTest.sayHello("world")

    then: "a specific method in the collaborator gets called"
    1 * collaborator.collaborate()

    when: "trying to stub a final method on the delegate"
    underTest.finalMethod() >> "mocked"

    then: "it will not work because the delegating method will also be final"
    isFinal(UnderTestDelegate.getDeclaredMethod("finalMethod").modifiers)
    underTest.finalMethod() == "final method"
  }

  static class UnderTestDelegate {
    @Delegate UnderTest underTest
  }
}
