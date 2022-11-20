import de.scrum_master.testing.MultiplexingConcurrentPrintStream
import spock.lang.Specification

runner {
  filterStackTrace true
  parallel {
    enabled false//true
    // These values are the default already, specifying them is redundant
    // defaultSpecificationExecutionMode = CONCURRENT
    // defaultExecutionMode = CONCURRENT
    fixed 4
  }
}

//System.setOut(new MultiplexingConcurrentPrintStream(System.out))

/**
 * Use like this in order to print Spock/Geb labels:
 *   given:_ "foo"
 *   when:_ "bar"
 *   then:_ "zot"
 */
class LabelPrinter {
  def _(def message) {
    println message
    true
  }
}

Specification.mixin LabelPrinter
