package de.scrum_master.testing.annotation_collector

import groovy.transform.AnnotationCollector
import spock.lang.Specification

@MetaAnnotation
class AnnotationCollectorTest extends Specification {
  def test() {
    given:
    def annotations = AnnotationCollectorTest.annotations
    def annotationTypes = annotations.collect { it.annotationType() }
    annotations.each { println it }

    expect:
    !annotationTypes.contains(AnnotationCollector)
    !annotationTypes.contains(MetaAnnotation)

    and:
    annotationTypes.contains(ActiveProfiles)
    annotationTypes.contains(SpringBootTest)
    annotationTypes.contains(TestExecutionListeners)
    annotationTypes.contains(AutoConfigureWireMock)
    annotationTypes.contains(EmbeddedKafka)
    annotationTypes.contains(TestPropertySource)
  }
}
