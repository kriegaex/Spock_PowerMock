package de.scrum_master.testing

import de.scrum_master.testing.extension.ConcurrentSystemOutErrExtension
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import static de.scrum_master.testing.TestUtility.getCurrentFixtureMethod
import static java.lang.System.currentTimeMillis
import static java.lang.Thread.currentThread

class ParallelExecutionTest extends Specification {
  static long startTime = currentTimeMillis()
  static volatile int execCount = 0
  public static final int SLEEP_MILLIS = 100

  def setupSpec() {
    println currentFixtureMethod
  }

  def cleanupSpec() {
    println currentFixtureMethod
  }

  def setup() {
    println currentFixtureMethod
  }

  def cleanup() {
    println currentFixtureMethod
  }

  def "feature-A-#count"() {
    given:
    def i = ++execCount
    printf "%5d %2d [%s] >> %s%n", currentTimeMillis() - startTime, i, currentThread().name, specificationContext.currentIteration.displayName
    def thread = new Thread({
      printf "%5d %2d [%s] XX %s%n", currentTimeMillis() - startTime, i, currentThread().name, specificationContext.currentIteration.displayName
    })
    ConcurrentSystemOutErrExtension.multiplexedSysOut.register(thread.id)
    thread.start()

    sleep SLEEP_MILLIS
    printf "%5d %2d [%s] << %s%n", currentTimeMillis() - startTime, i, currentThread().name, specificationContext.currentIteration.displayName

    cleanup:
    ConcurrentSystemOutErrExtension.multiplexedSysOut.dump(thread.id)

    where:
    count << (1..8)
  }

  def "feature-B-#count"() {
    given:
    def i = ++execCount
    printf "%5d %2d [%s] >> %s%n", currentTimeMillis() - startTime, i, currentThread().name, specificationContext.currentIteration.displayName
    ExecutorService executorService = Executors.newFixedThreadPool(3)
    def submitted = executorService.submit({
      def threadId = currentThread().id
      ConcurrentSystemOutErrExtension.multiplexedSysOut.register(threadId)
      try {
        printf "%5d %2d [%s] XX %s%n", currentTimeMillis() - startTime, i, currentThread().name, specificationContext.currentIteration.displayName
      } finally {
        ConcurrentSystemOutErrExtension.multiplexedSysOut.dump(threadId)
      }
    })
//    def thread = new Thread({
//      printf "%5d %2d [%s] XX %s%n", currentTimeMillis() - startTime, i, currentThread().name, specificationContext.currentIteration.displayName
//    })
//    ConcurrentSystemOutErrExtension.multiplexedSysOut.register(thread.id)
//    thread.start()
    sleep SLEEP_MILLIS
    printf "%5d %2d [%s] << %s%n", currentTimeMillis() - startTime, i, currentThread().name, specificationContext.currentIteration.displayName

//    cleanup:
//    ConcurrentSystemOutErrExtension.multiplexedSysOut.dump(thread.id)

    where:
    count << (1..8)
  }
}
