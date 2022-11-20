package de.scrum_master.testing

class TestUtility {
  static String getCurrentFixtureMethod() {
    def stackTraceElement = new Exception().stackTrace
      .find {it.methodName =~ /(setup|cleanup)(Spec)?/}
    return stackTraceElement
      ? "[${Thread.currentThread().id}] $stackTraceElement.className.$stackTraceElement.methodName"
      : null
  }
}
