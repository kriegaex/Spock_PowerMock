package de.scrum_master.testing

import org.spockframework.mock.MockUtil

class ReflectiveMethodInvoker extends DelegatingMetaClass {
  private final static mockUtil = new MockUtil()

  ReflectiveMethodInvoker(MetaClass metaClass) { super(metaClass) }

  ReflectiveMethodInvoker(Class theClass) { super(theClass) }

  Object invokeMethod(Object instance, String methodName, Object[] arguments) {
    println "invokeMethod: $instance -> $methodName($arguments) / $arguments.length"
    def argTypes = arguments.collect { mockUtil.isMock(it) ? it.class.superclass : it.class }
    instance.class.getDeclaredMethod(methodName, *argTypes).invoke(instance, arguments)
  }
}
