package de.scrum_master.issue.i1454

import org.spockframework.util.SpockReleaseInfo
import org.spockframework.util.VersionNumber
import spock.lang.IgnoreIf
import spock.lang.Requires
import spock.lang.Specification

@Requires(value = { false }, inherited = true)
abstract class Base extends Specification {
  @IgnoreIf({ SpockReleaseInfo.version < new VersionNumber(2, 1, 0, "M1") || data.device == 'GPU' })
  @IgnoreIf({ brb.foo() })
  def 'base'(String device) {
    expect:
    device == 'CPU'

    where:
    device << ['CPU', 'GPU']
  }
}

//@Requires({ SpockReleaseInfo.version >= new VersionNumber(3, 1, 0, "M1") })
class AccessDataVariableViaPreconditionContextTest extends Base {
  @IgnoreIf({ SpockReleaseInfo.version < new VersionNumber(2, 1, 0, "M1") || data.device == 'GPU' })
  @IgnoreIf({ xox.foo() })
  def 'I only work on the CPU!'(String device) {
    expect:
    device == 'CPU'

    where:
    device << ['CPU', 'GPU']
  }
}
