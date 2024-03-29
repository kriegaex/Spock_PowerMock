package de.scrum_master.spock_powermock

import org.junit.Rule
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import spock.lang.Specification

import static org.easymock.EasyMock.expect
import static org.powermock.api.easymock.PowerMock.*

//@RunWith(PowerMockRunner)
//@PowerMockRunnerDelegate(JUnitPlatform)
@PrepareForTest(Person)
class PowerMockEasyMockPersonTest extends Specification {
  @Rule PowerMockRule powerMockRule = new PowerMockRule()

  private static Person person = new Person("Kriegisch", "Alexander", new GregorianCalendar(1971, 5 - 1, 8).time)

  def "Person properties"() {
    expect:
    person.lastName == "Kriegisch"
    person.firstName == "Alexander"
    person.dateOfBirth.year == 71
  }

  def "Write Person to file"() {
    given:
    FileOutputStream fosMock = createNiceMockAndExpectNew(FileOutputStream, "mock.txt")
    replay(fosMock)
    when:
    person.writeToFile("mock.txt")
    then:
    verify(fosMock)
  }

  def "Error when creating output file"() {
    given:
    expectNew(FileOutputStream, "mock.txt").andThrow(new IOException("uh-oh"))
    replay(FileOutputStream)
    when:
    person.writeToFile("mock.txt")
    then:
    thrown(IOException)
    verify(FileOutputStream)
  }

  def "Error when closing output file"() {
    given:
    FileOutputStream fosMock = createNiceMockAndExpectNew(FileOutputStream, "mock.txt")
    expect(fosMock.close()).andStubThrow(new IOException("uh-oh"))
    replay(FileOutputStream)
    when:
    person.writeToFile("mock.txt")
    then:
    verify(FileOutputStream)
  }

  def "Static methods"() {
    expect:
    Person.defaultLastName() == "Doe"
    Person.defaultFirstName() == "John"
  }

  def "Static methods (mocked)"() {
    given:
    mockStatic(Person)
    when:
    expect(Person.defaultLastName()).andReturn("Mustermann")
    expect(Person.defaultFirstName()).andReturn("Manfred")
    replay(Person)
    then:
    Person.defaultLastName() == "Mustermann"
    Person.defaultFirstName() == "Manfred"
    verify(Person)
  }
}
