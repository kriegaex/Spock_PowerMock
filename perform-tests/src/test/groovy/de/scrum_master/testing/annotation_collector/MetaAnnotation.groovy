package de.scrum_master.testing.annotation_collector

import groovy.transform.AnnotationCollector

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@ActiveProfiles(profiles = [Profiles.TEST])
@SpringBootTest(classes = [ServiceApplication])
@TestExecutionListeners(listeners = [ServletTestExecutionListener, DependencyInjectionTestExecutionListener, DirtiesContextTestExecutionListener, TransactionalTestExecutionListener, WithSecurityContextTestExecutionListener, SpringMockTestExecutionListener])
@AutoConfigureWireMock(port = Options.DYNAMIC_PORT, stubs = "classpath:wiremock/mappings")
@EmbeddedKafka(controlledShutdown = true, partitions = 1)
@TestPropertySource(properties = ["spring.kafka.bootstrap-servers=\${spring.embedded.kafka.brokers}"])
@Retention(RetentionPolicy.RUNTIME)
@AnnotationCollector
@interface MetaAnnotation {}
