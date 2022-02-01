package de.scrum_master.testing;

public final class UnderTest extends UnderTestBase {
  public UnderTest(Collaborator collaborator) {
    super(collaborator);
  }

  public void sayHello(String to) {
    System.out.println("Hello " + to + "!");
    doSomething();
  }

  public final String finalMethod() {
    return "final method";
  }
}
