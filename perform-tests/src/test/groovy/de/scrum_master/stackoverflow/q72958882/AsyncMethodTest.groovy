package de.scrum_master.stackoverflow.q72958882

import spock.lang.Specification
import spock.util.concurrent.PollingConditions

class AsyncMethodTest extends Specification {
  def "my feature"() {
    given:
    int timesExecuted
    // In order to verify interactions on self-invoked methods of a class under
    // test, we need a spy.
    UnderTest underTest = Spy() {
      method1() >> {
        // On the one hand, we need to stub this method in order to increment a
        // counter which can then be checked in the polling condition. On the
        // other hand, we  need to call the real method in order to make the
        // class under test behave normally.
        callRealMethod()
        timesExecuted++
      }
    }

    when:
    underTest.doSomething()

    then:
    new PollingConditions(timeout: 1).eventually {
      timesExecuted == 1
    }
  }
}

interface MessageReceiver {
  def process(def message, def consumer)
}

class UnderTest {
  def doSomething() {
    Thread.start { getMessageReceiver().process("message", "consumer") }
  }

  private MessageReceiver getMessageReceiver() {
    return { message, consumer ->
      method1()
    }
  }

  def method1() {
    sleep 500
    println "Doing something"
  }
}
