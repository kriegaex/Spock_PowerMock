package de.scrum_master

import de.scrum_master.testing.MyTest
import org.junit.runner.JUnitCore
import org.junit.runner.Request
import org.junit.runner.Result
import spock.util.EmbeddedSpecRunner

class MySpecRunner {
  static void main(String[] args) {
    Request request = Request.classes(MyTest)
    Result junitResult = new JUnitCore().run(request)
    println "Tests run: " + junitResult.runCount
    println "Tests ignored: " + junitResult.ignoreCount
    println "Tests failed: " + junitResult.failureCount

    def spockRunner = new EmbeddedSpecRunner()
    def spockResult = spockRunner.runClass(MyTest)
    println "Tests run: " + spockResult.runCount
    println "Tests ignored: " + spockResult.ignoreCount
    println "Tests failed: " + spockResult.failureCount
  }
}
