package de.scrum_master.stackoverflow.q71122575

import spock.lang.Issue
//import spock.lang.Tag
import spock.lang.Unroll

//@Tag(["included", "whatever"])
@Issue("XA-123")
class CredsTest extends InputDataJsonTest {
  @Unroll("verify credentials for user #username")
  def "verify parsed credentials"() {
    given:
    println "$username, $password"

    expect:
    username.length() >= 3
    password.length() >= 6

    where:
    cred << credsList
    username = cred.username
    password = cred.password
  }

//  @Tag("excluded")
  def "excluded"() {
    println "excluded child"
    expect:
    true
  }
}
