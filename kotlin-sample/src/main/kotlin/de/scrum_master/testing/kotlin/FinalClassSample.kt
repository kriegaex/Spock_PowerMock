package de.scrum_master.testing.kotlin

class FinalClassSample : Tested {
  override fun finalMethod(): Unit = throw IllegalAccessError("you should not see this")
}
