package de.scrum_master.testing.sarek;

import de.scrum_master.testing.Collaborator;
import de.scrum_master.testing.FinalClass;
import de.scrum_master.testing.UnderTest;
import dev.sarek.agent.mock.MockFactory;
import org.testng.annotations.Test;

import javax.swing.*;

import static dev.sarek.jar.bytebuddy.matcher.ElementMatchers.named;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class SarekRelocatedTestNGTest {
  @Test
  public void canMockApplicationClasses() {
    MockFactory<FinalClass> mockFactory1 = MockFactory
      .forClass(FinalClass.class)
      .mockConstructors()
      .addGlobalInstance()
      .mockStatic(
        named("finalStaticMethod"),
        (method, args) -> false,
        (method, args, proceedMode, returnValue, throwable) -> "stubbed static"
      )
      .build();
    MockFactory<UnderTest> mockFactory2 = MockFactory.forClass(UnderTest.class).mockConstructors().addGlobalInstance().build();

    assertNull(new FinalClass().finalMethod());
    assertEquals("stubbed static", FinalClass.finalStaticMethod());
    assertNull(new UnderTest(new Collaborator()).finalMethod());

    mockFactory1.close();
    mockFactory2.close();

    assertEquals("x", new FinalClass().finalMethod());
    assertEquals("x", FinalClass.finalStaticMethod());
    assertEquals("final method", new UnderTest(new Collaborator()).finalMethod());
  }

  @Test
  public void canMockBootstrapClasses_Swing() {
    // Try with resources works for Mock because it implements AutoCloseable
    try (
      MockFactory<JTable> mockFactory1 = MockFactory.forClass(JTable.class).mockConstructors().addGlobalInstance().build();
      MockFactory<GroupLayout> mockFactory2 = MockFactory.forClass(GroupLayout.class).mockConstructors().addGlobalInstance().build();
      MockFactory<JTextField> mockFactory3 = MockFactory.forClass(JTextField.class).mockConstructors().addGlobalInstance().build()
    )
    {
      JTable jTable = new JTable(3, 3);
      assertEquals(0, jTable.getRowCount());
      assertEquals(0, jTable.getColumnCount());
      assertNull(new GroupLayout(null).getLayoutStyle());
      assertNull(new JTextField().getSelectedTextColor());
    }
  }
}
