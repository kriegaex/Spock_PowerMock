package de.scrum_master.testing.kotlin

class ClassToBeTested(val classUnderTest: Tested) {
  fun callMe() = classUnderTest.finalMethod()
}
