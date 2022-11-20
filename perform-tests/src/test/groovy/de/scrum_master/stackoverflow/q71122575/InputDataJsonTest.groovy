package de.scrum_master.stackoverflow.q71122575

import groovy.json.JsonSlurper
import spock.lang.Shared
import spock.lang.Specification
//import spock.lang.Tag

//@Tag("parent")
abstract class InputDataJsonTest extends Specification {
  @Shared
  List<Creds> credsList = []

  def setupSpec() {
    def inputJSON = '''{
      "validLogin" : {
        "username" : "abc",
        "password" : "correcttest"
      },
      "invalidLogin" : {
        "username" : "xyz",
        "password" : "badtest"
      }
    }'''

    credsList = new JsonSlurper().parseText(inputJSON)
      .values()
      .collect { login -> new Creds(username: login.username, password: login.password) }
  }

  def "parent test"() {
    println "parent test #$count"
    expect:
    true

    where:
    count << (1..3)
  }
}
