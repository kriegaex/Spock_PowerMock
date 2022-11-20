package de.scrum_master.stackoverflow.q57298557

class PersonBuilder {
  Person person = new Person()

  PersonBuilder withAge(int age) {
    person.age = age
    this
  }

  PersonBuilder withName(String name) {
    person.name = name
    this
  }

  PersonBuilder withHair(String hair) {
    person.hair = hair
    this
  }

  Person build() {
    person
  }
}
