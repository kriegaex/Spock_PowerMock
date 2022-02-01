package de.scrum_master.testing

import mockit.Mock
import mockit.MockUp
import mockit.internal.state.SavePoint
import spock.lang.Requires
import spock.lang.Specification
import spock.lang.Stepwise

import static de.scrum_master.testing.JavaAgentDetector.isJMockitActive
import static de.scrum_master.testing.JavaAgentDetector.isRemoveFinalAgentActive

@Stepwise
@Requires({ JMockitActive && !removeFinalAgentActive })
class FinalClassJMockitTest extends Specification {
  static final String UUID_DUMMY = "136dcd59-dfe3-401a-b909-187665960c94"

  def jMockitSavePoint = new SavePoint()

  def cleanup() {
    jMockitSavePoint.rollback()
  }

  def "use original final class before JMockit"() {
    expect:
    new AnotherClass().doSomething(new FinalClass()) == "x"
  }

  def "use JMockit to stub final method in final class"() {
    given: "a fake object declared inline"
    new MockUp<FinalClass>() {
      @Mock
      String finalMethod() {
        "mocked"
      }
    }

    expect: "the faked class' method is stubbed"
    new AnotherClass().doSomething(new FinalClass()) == "mocked"
  }

  def "use original final class after JMockit"() {
    expect:
    new AnotherClass().doSomething(new FinalClass()) == "x"
  }

  def "use JMockit to stub static method of final system class"() {
    given: "a fake object declared as an inner static class"
    new FakeUUID()

    expect: "the faked class' static method is stubbed"
    AnotherClass.createUUID().toString() == UUID_DUMMY
  }

  static class FakeUUID extends MockUp<UUID> {
    /**
     * Actually this mock method would also match if not declared static
     * because it just seems to matched based on method name and parameter
     * list, not on modifiers.
     *
     * So if you remove the 'static' modifier, then class can be inlined as an
     * anonymous class into the test like 'new MockUp<UUID>() { ... }'.
     */
    @Mock
    static UUID randomUUID() {
      UUID.fromString(UUID_DUMMY)
    }
  }
}
