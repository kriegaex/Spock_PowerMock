package de.scrum_master.stackoverflow.q71087906

import spock.lang.Specification
import spock.lang.Subject
//import spock.lang.Tag

import java.lang.reflect.Field
import java.util.concurrent.CompletableFuture
import java.util.function.Consumer

import static java.nio.charset.StandardCharsets.UTF_8

//@Tag("fast")
class ConsumerLambdaUserTest extends Specification {
  def componentMock = Mock(Component)

  @Subject
  def sut = new ConsumerLambdaUser(asyncHandler: new AsyncHandler(), component: componentMock)

  //@Tag("excluded")
  def "mock test"() {
    given:
    String name

    when:
    sut.exampleMethod('teststring')

    then:
    1 * componentMock.doCall(_ as Consumer<Request>) >> { args ->
      Consumer<Request> requestConsumer = args[0]
      Field nameField = requestConsumer.class.declaredFields[1]
//    Field nameField = requestConsumer.class.getDeclaredField('arg$2')
      nameField.accessible = true
      byte[] nameBytes = nameField.get(requestConsumer)
      name = new String(nameBytes, UTF_8)
      return new CompletableFuture()
    }
    name == 'teststring'
  }

  def "no mock test"() {
    given:
    def component = Spy(Component)
    String name
    def sut = new ConsumerLambdaUser(asyncHandler: new AsyncHandler(), component: component)

    when:
    sut.exampleMethod('teststring')

    then:
    1 * component.doCall(_ as Consumer<Request>) >> { args ->
      Consumer<Request> requestConsumer = args[0]
      Field nameField = requestConsumer.class.declaredFields[1]
//    Field nameField = requestConsumer.class.getDeclaredField('arg$2')
      nameField.accessible = true
      byte[] nameBytes = nameField.get(requestConsumer)
      name = new String(nameBytes, UTF_8)
      return callRealMethod()
    }
    name == 'teststring'
  }
}
