package de.scrum_master.testing

import spock.lang.Specification

class SampleIT extends Specification {
  def test() {
    println "${specificationContext.currentSpec.name}.${specificationContext.currentFeature.name}"
    expect:
    true
  }
}
