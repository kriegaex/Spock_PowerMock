package de.scrum_master.testing.extension

import de.scrum_master.testing.MultiplexingConcurrentPrintStream
import org.spockframework.runtime.extension.IGlobalExtension
import org.spockframework.runtime.model.MethodInfo
import org.spockframework.runtime.model.SpecInfo

class ConcurrentSystemOutErrExtension implements IGlobalExtension {
  public static final PrintStream originalSysOut = System.out
  public static final PrintStream originalSysErr = System.err
  public static final MultiplexingConcurrentPrintStream multiplexedSysOut = new MultiplexingConcurrentPrintStream(originalSysOut)
  public static final MultiplexingConcurrentPrintStream multiplexedSysErr = new MultiplexingConcurrentPrintStream(originalSysErr)

  @Override
  void start() {
    System.out = multiplexedSysOut
    System.err = multiplexedSysErr
  }

  @Override
  void stop() {
    System.out = originalSysOut
    System.err = originalSysErr
    multiplexedSysOut.clearAll()
    multiplexedSysErr.clearAll()
  }

  @Override
  void visitSpec(SpecInfo spec) {
//    spec.addSetupSpecInterceptor({
//      multiplexedSysOut.register()
//      multiplexedSysErr.register()
//      it.proceed()
//    })
    spec.addSetupInterceptor({
      multiplexedSysOut.register()
      multiplexedSysErr.register()
      println ">> [${Thread.currentThread().id}] $it.spec.displayName.$it.iteration.displayName"
      it.proceed()
    })
    spec.addCleanupInterceptor({
      it.proceed()
      println "<< [${Thread.currentThread().id}] $it.spec.displayName.$it.iteration.displayName"
      multiplexedSysOut.dump()
      multiplexedSysErr.dump()
    })
//    spec.addCleanupSpecInterceptor({
//      it.proceed()
//      multiplexedSysOut.dump()
//      multiplexedSysErr.dump()
//    })
  }
}
