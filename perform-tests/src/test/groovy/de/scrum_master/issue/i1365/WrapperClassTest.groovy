package de.scrum_master.issue.i1365

import spock.lang.Specification
import spock.lang.Unroll

class WrapperClassTest extends Specification {
  def internalClass = Mock(InternalClass)
  def wrapper = new WrapperClass(internalClass: internalClass)

  @Unroll
  def "test #methodName with args #args"() {
    when:
    wrapper."$methodName"(*args)

    then:
    // Failing original variant
    // 1 * internalClass."$methodName"(*args)
    // Workaround by leonard84 in https://github.com/spockframework/spock/issues/1365#issuecomment-894623578
    1 * internalClass."$methodName"(*_) >> { invocationArgs ->
      assert invocationArgs == args
    }

    where:
    methodName     | args
    'firstMethod'  | ['firstArg', 'secondArg']
    'secondMethod' | ['firstArg', 'secondArg', 123L]
  }
}

class WrapperClass {
  InternalClass internalClass

  void firstMethod(String firstArg, String secondArg) {
    internalClass.firstMethod(firstArg, secondArg)
  }

  void secondMethod(String firstArg, String secondArg, long thirdArg) {
    internalClass.secondMethod(firstArg, secondArg, thirdArg)
  }
}

class InternalClass {
  void firstMethod(String firstArg, String secondArg) {}

  void secondMethod(String firstArg, String secondArg, long thirdArg) {}
}
