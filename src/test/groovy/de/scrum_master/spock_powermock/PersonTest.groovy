package de.scrum_master.spock_powermock

import org.junit.Rule
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import spock.lang.Specification

import static org.powermock.api.mockito.PowerMockito.*

@PrepareForTest([Person.class])
class PersonTest extends Specification {
    @Rule PowerMockRule rule = new PowerMockRule();

    private static Person person = new Person("Kriegisch", "Alexander", new Date(1971 - 1900, 5 - 1, 8))

    def "Person properties"() {
        expect:
        person.getLastName() == "Kriegisch"
        person.getFirstName() == "Alexander"
        person.getDateOfBirth().getYear() == 71
    }

    def "Write Person to file"() {
        setup:
        FileOutputStream fosMock = mock(FileOutputStream.class)
        when:
        whenNew(FileOutputStream.class).withArguments("mock.txt").thenReturn(fosMock)
        then:
        person.writeToFile("mock.txt")
    }

    def "Error when creating output file"() {
        when:
        whenNew(FileOutputStream.class).withArguments("mock.txt").thenThrow(IOException.class)
        person.writeToFile("mock.txt")
        then:
        thrown(IOException)
    }

    def "Error when closing output file"() {
        setup:
        FileOutputStream fosMock = mock(FileOutputStream.class)
        when:
        whenNew(FileOutputStream.class).withArguments("mock.txt").thenReturn(fosMock)
        when(fosMock.close()).thenThrow(IOException.class)
        then:
        person.writeToFile("mock.txt")
    }

    def "Static methods"() {
        expect:
        Person.defaultLastName() == "Doe"
        Person.defaultFirstName() == "John"
    }

    def "Static methods (mocked)"() {
        setup:
        mockStatic(Person.class)
        when:
        when(Person.defaultLastName()).thenReturn("Mustermann")
        when(Person.defaultFirstName()).thenReturn("Manfred")
        then:
        Person.defaultLastName() == "Mustermann"
        Person.defaultFirstName() == "Manfred"
    }
}