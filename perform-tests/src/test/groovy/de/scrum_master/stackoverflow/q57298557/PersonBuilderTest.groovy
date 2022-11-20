package de.scrum_master.stackoverflow.q57298557

import org.spockframework.mock.EmptyOrDummyResponse
import org.spockframework.util.SpockReleaseInfo
import spock.lang.Requires
import spock.lang.Specification

class PersonBuilderTest extends Specification {
  def "create person with real builder"() {
    given:
    def personBuilder = new PersonBuilder()

    when:
    def person = personBuilder
      .withHair("blonde")
      .withAge(22)
      .withName("Alice")
      .build()

    then:
    person.age == 22
    person.hair == "blonde"
    person.name == "Alice"
  }

  def "create person with stub builder, no interactions"() {
    given:
    PersonBuilder personBuilder = Stub()
    personBuilder.build() >> new Person(name: "John Doe", age: 99, hair: "black")

    when:
    def person = personBuilder
      .withHair("blonde")
      .withAge(22)
      .withName("Alice")
      .build()

    then:
    person.age == 99
    person.hair == "black"
    person.name == "John Doe"
  }

  def "create person with mock builder, use interactions"() {
    given:
    PersonBuilder personBuilder = Mock(defaultResponse: EmptyOrDummyResponse.INSTANCE)

    when:
    def person = personBuilder
      .withHair("blonde")
      .withAge(22)
      .withName("Alice")
      .build()

    then:
    3 * personBuilder./with.*/(_)
    1 * personBuilder.build() >> new Person(name: "John Doe", age: 99, hair: "black")
    person.age == 99
    person.hair == "black"
    person.name == "John Doe"
  }

  @Requires({ SpockReleaseInfo.version.major >= 2})
  def "create person with mock builder, use interactions, Spock 2.x"() {
    given:
    PersonBuilder personBuilder = Mock()

    when:
    def person = personBuilder
      .withHair("blonde")
      .withAge(22)
      .withName("Alice")
      .build()

    then:
    3 * personBuilder./with.*/(_) >> _
    1 * personBuilder.build() >> new Person(name: "John Doe", age: 99, hair: "black")
    person.age == 99
    person.hair == "black"
    person.name == "John Doe"
  }

  def "create person with a la carte mock builder, use interactions"() {
    given:
    PersonBuilder personBuilder = Mock(defaultResponse: ThisResponse.INSTANCE) {
      3 * /with.*/(_)
      1 * build() >> new Person(name: "John Doe", age: 99, hair: "black")
    }

    when:
    def person = personBuilder
      .withHair("blonde")
      .withAge(22)
      .withName("Alice")
      .build()

    then:
    person.age == 99
    person.hair == "black"
    person.name == "John Doe"
  }
}
