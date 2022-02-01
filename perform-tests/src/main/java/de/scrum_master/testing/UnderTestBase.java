package de.scrum_master.testing;

public class UnderTestBase {
  private Collaborator collaborator;

  public UnderTestBase(Collaborator collaborator) {
    this.collaborator = collaborator;
  }

  public void doSomething() {
    System.out.println("Doing something basic");
    collaborator.collaborate();
  }
}
