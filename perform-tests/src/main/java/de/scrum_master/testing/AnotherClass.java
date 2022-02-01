package de.scrum_master.testing;

import java.util.UUID;

public class AnotherClass {
  public String doSomething(FinalClass finalClass) {
    return finalClass.finalMethod();
  }

  public String doSomethingElse() {
    return FinalClass.finalStaticMethod();
  }

  public static UUID createUUID() {
    return UUID.randomUUID();
  }
}
