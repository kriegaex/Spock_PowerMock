package de.scrum_master.issue.i1234

import spock.lang.Specification

class FooBarTest extends Specification {
  def 'finding bar in [test: [foo, bar]] as argument constraint will work when calling foo on a GroovyMock'() {
    given:
    FooBar fooBar = GroovySpy(FooBar)

    when:
    fooBar.foo(test: ['foo', 'bar'])

    then:
    1 * fooBar.foo { args ->
      args.test.any {
        println it
        it == 'bar'
      }
    }
  }
}
