package de.scrum_master.stackoverflow.q71631373

import spock.lang.Requires
import spock.lang.Specification
import spock.lang.Unroll

// TODO: Remove @Requires after https://github.com/spockframework/spock/issues/1544 is fixed
@Requires({ GroovySystem.shortVersion as double < 4.0 })
class DynamicBaseClassTest extends Specification {
  @Unroll("verify #className")
  def "basic event class functionality"() {
    given:
    def service = new Service()
    def event = Spy(baseEventClass.getConstructor().newInstance())

    when:
    event.init()
    then:
    // '.id' and '.name' should be enough, but on Spock 2.1 there is a problem
    // when not explicitly using the '@' notation for direct field access.
    event.@id > 0
    event.@name.length() == 10

    when:
    service.sendEvent(event)
    then:
    1 * event.sendExternalEvent(_, event.class.name, [:])

    where:
    baseEventClass << getEventClasses()
    className = baseEventClass.simpleName
  }

  static List<Class<? extends BaseEvent>> getEventClasses() {
    [FirstEvent, SecondEvent, ThirdEvent]
  }
}

interface Event {
  void init()

  void sendExternalEvent(String id, String className, Map options)
}

class Service {
  void sendEvent(Event event) {
    event.sendExternalEvent("123", event.class.name, [:])
  }
}

abstract class BaseEvent implements Event {
  private static final Random random = new Random()
  private static final String alphabet = (('A'..'Z') + ('0'..'9')).join()

  protected int id
  protected String name

  @Override
  void init() {
    id = 1 + random.nextInt(100)
    name = (1..10).collect { alphabet[random.nextInt(alphabet.length())] }.join()
  }
}

class FirstEvent extends BaseEvent {
  @Override
  void sendExternalEvent(String id, String className, Map options) {}

  String doFirst() { "first" }
}

class SecondEvent extends BaseEvent {
  @Override
  void sendExternalEvent(String id, String className, Map options) {}

  String doSecond() { "second" }
}

class ThirdEvent extends BaseEvent {
  @Override
  void sendExternalEvent(String id, String className, Map options) {}

  int doThird() { 3 }
}
