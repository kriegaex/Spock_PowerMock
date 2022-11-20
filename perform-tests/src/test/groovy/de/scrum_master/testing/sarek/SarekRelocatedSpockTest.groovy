package de.scrum_master.testing.sarek

import de.scrum_master.testing.Collaborator
import de.scrum_master.testing.FinalClass
import de.scrum_master.testing.UnderTest
import dev.sarek.agent.mock.MockFactory
import spock.lang.Specification

import javax.swing.*

import static dev.sarek.jar.bytebuddy.matcher.ElementMatchers.named

class SarekRelocatedSpockTest extends Specification {
  def canMockApplicationClasses() {
    // Test new Groovy 3 Parrot parser supporting Java lambdas and try-with-resources
    new Thread(() -> { println "### lambda runnable" }).start()

    given:
    MockFactory<FinalClass> mockFactory1 = MockFactory
      .forClass(FinalClass)
      .mockConstructors()
      .addGlobalInstance()
      .mockStatic(
        named("finalStaticMethod"),
        { method, args -> false },
        { method, args, proceedMode, returnValue, throwable -> "stubbed static" }
      )
      .build()
    MockFactory<UnderTest> mockFactory2 = MockFactory.forClass(UnderTest).mockConstructors().addGlobalInstance().build()

    expect:
    new FinalClass().finalMethod() == null
    FinalClass.finalStaticMethod() == "stubbed static"
    new UnderTest(new Collaborator()).finalMethod() == null

    when:
    mockFactory1.close()
    mockFactory2.close()

    then:
    new FinalClass().finalMethod() == "x"
    FinalClass.finalStaticMethod() == "x"
    new UnderTest(new Collaborator()).finalMethod() == "final method"
  }

  /**
   * Needs either Sarek Spock extension or Sarek Java agent on command line
   */
  def canMockBootstrapClasses_Swing() {
    given:
    MockFactory<JTable> mockFactory1 = MockFactory.forClass(JTable).mockConstructors().addGlobalInstance().build()
    MockFactory<GroupLayout> mockFactory2 = MockFactory.forClass(GroupLayout).mockConstructors().addGlobalInstance().build()
    MockFactory<JTextField> mockFactory3 = MockFactory.forClass(JTextField).mockConstructors().addGlobalInstance().build()
    and:
    JTable jTable = new JTable(3, 3)

    expect:
    jTable.getRowCount() == 0
    jTable.getColumnCount() == 0
    !new GroupLayout(null).getLayoutStyle()
    !new JTextField().getSelectedTextColor()
  }
}
