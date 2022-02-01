package de.scrum_master.testing

import spock.lang.Requires
import spock.lang.Specification
import spock.util.environment.Jvm

import static de.scrum_master.testing.JavaAgentDetector.canMock

class FinalClassFinalMethodTest extends Specification {

  @Requires({ canMock(FinalClass) })
  def "mock final class FinalClass via RemoveFinalAgent"() {
    given:
    def finalClass = Stub(FinalClass) {
      finalMethod() >> "mocked"
    }

    expect: "can use stubbed methods directly from Spock"
    finalClass.finalMethod() == "mocked"

    and: "can use stubbed methods indirectly from Java class"
    new AnotherClass().doSomething(finalClass) == "mocked"
  }

  @Requires({ canMock(UUID) })
  def "mock final class UUID via RemoveFinalAgent"() {
    given:
    def uuid = Stub(UUID) {
      toString() >> "xxx-yyy-zzz"
    }

    expect: "can use stubbed methods directly from Spock"
    uuid.toString() == "xxx-yyy-zzz"

    and: "can use stubbed methods indirectly from Java class"
    new JREClassUser().uuidToString(uuid) == "xxx-yyy-zzz"
  }

  @Requires({ canMock(StringJoiner) })
  def "mock final class StringJoiner via RemoveFinalAgent"() {
    given:
    def stringJoiner = Stub(StringJoiner) {
      length() >> 999
      toString() >> "dummy"
    }

    expect: "can use stubbed methods directly from Spock"
    stringJoiner.toString() == "dummy"
    stringJoiner.length() == 999

    and: "can use stubbed methods indirectly from Java class"
    new JREClassUser().joinStrings(stringJoiner, "one", "two", "three") == "dummy"
  }

  @Requires({ canMock(URL) })
  def "mock final class URL via JRE instrumentation"() {
    given:
    def webContent = "<html><body>Hello WWW!</body><html>"
    def url = Stub(URL) {
      // Original method is also final, so this only works if the transformer
      // removes 'final' from both class and method
      getContent() >> webContent
    }

    expect: "can use stubbed methods directly from Spock"
    url.getContent() == webContent

    and: "can use stubbed methods indirectly from Java class"
    new JREClassUser().getURLContent(url) == webContent
  }

  // Mocking String does not work in Java 9+
  @Requires({ canMock(String) && !jvm.java9Compatible })
  def "stub final class String via JRE instrumentation"() {
    given:
    def string = Stub(String) {
      equals(_) >> true
      equalsIgnoreCase(_ as String) >> true
      toUpperCase() >> "I prefer mixed case!"
    }
    def jreClassUser = Spy(JREClassUser) {
      uuidToString(_ as UUID) >> "UUID-Spy-toString"
    }
    jreClassUser.metaClass = new ReflectiveMethodInvoker(jreClassUser.metaClass)

    expect: "can use stubbed methods directly from Spock"
    string.equalsIgnoreCase("dummy")
    string.toUpperCase() == "I prefer mixed case!"

    and: "can use stubbed methods indirectly from Java class"
    jreClassUser.areStringsEqual(string, "foo")
    jreClassUser.areStringsEqualIgnoreCase(string, "dummy")
    jreClassUser.stringToUpperCase(string) == "I prefer mixed case!"
    jreClassUser.joinStrings(new StringJoiner("/"), ["aa", "bb"] as String[]) == "aa/bb"

//    and: "ReflectiveMethodInvoker correctly delegates to the spy, not to the original instance"
//    jreClassUser.uuidToString(UUID.randomUUID()) == "UUID-Spy-toString"
  }

  @Requires({ canMock(String) })
  def "spy on final class String via JRE instrumentation"() {
    given:
    String string = Spy(String, constructorArgs: ["hello"]) {
      equals(_) >> true
      equalsIgnoreCase(_ as String) >> true
    }
    def jreClassUser = new JREClassUser()
    jreClassUser.metaClass = new ReflectiveMethodInvoker(jreClassUser.metaClass)

    expect: "can use stubbed methods directly from Spock"
    // Note: We explicitly use 'equals()' because '==' does not use the stubbed method.
    // This would not be necessary if we did not override the 'equals()' method.
    // But then we would have to use '== "hello"'.
    string.equals("foo")
    string.equalsIgnoreCase("dummy")
    string.toUpperCase() == "HELLO"

    and: "can use stubbed methods indirectly from Java class"
    jreClassUser.areStringsEqual(string, "foo")
    jreClassUser.areStringsEqualIgnoreCase(string, "dummy")
    jreClassUser.stringToUpperCase(string) == "HELLO"
  }

}
