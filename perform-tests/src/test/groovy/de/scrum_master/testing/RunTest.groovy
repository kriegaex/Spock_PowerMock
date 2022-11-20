package de.scrum_master.testing

import spock.lang.Specification
import spock.util.EmbeddedSpecRunner

class RunTest extends Specification {
  EmbeddedSpecRunner specRunner = new EmbeddedSpecRunner()

  def runEmbeddedSpec() {
    given:
    specRunner.configurationScript {
      runner {
        parallel {
          enabled true
          fixed 4
        }
      }
      System.setOut(new MultiplexingConcurrentPrintStream(System.out))
    }

    expect:
    specRunner.runClass(ParallelExecutionTest)
  }
}
