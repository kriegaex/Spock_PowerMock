package de.scrum_master.testing

import spock.lang.IgnoreIf
import spock.lang.Specification
import spock.util.environment.OperatingSystem

class IgnoreIfTest extends Specification {
  @IgnoreIf({ SpockSettings.SKIP_SLOW_TESTS })
  def "slow test"() {
    expect:
    true
  }

  def "quick test"() {
    expect:
    true
  }

  @IgnoreIf({ os.family == OperatingSystem.Family.WINDOWS })
  def "Windows test"() {
    expect:
    true
  }

  @IgnoreIf({ !jvm.isJava8Compatible() })
  def "needs Java 8"() {
    expect:
    true
  }

  @IgnoreIf({ env["USERNAME"] != "kriegaex" })
  def "user-specific"() {
    expect:
    true
  }
}
