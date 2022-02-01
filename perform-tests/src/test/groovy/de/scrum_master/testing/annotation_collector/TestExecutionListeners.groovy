package de.scrum_master.testing.annotation_collector

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

@Retention(RetentionPolicy.RUNTIME)
@interface TestExecutionListeners {
Class[] listeners()
}
