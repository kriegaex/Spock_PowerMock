package de.scrum_master.testing.extension

import org.junit.jupiter.api.extension.*
import org.junit.platform.engine.reporting.ReportEntry
import org.junit.platform.launcher.TestExecutionListener
import org.junit.platform.launcher.TestIdentifier

class ConsoleOutputTestExecutionListener implements TestExecutionListener, BeforeAllCallback, AfterAllCallback {
  private final PrintStream originalSysOut = System.out
  private final PrintStream originalSysErr = System.err

  @Override
  void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
    synchronized (originalSysOut) {
      def stdOutText = entry.getKeyValuePairs().get("stdout")
      if (stdOutText) {
        originalSysOut.print(stdOutText.toUpperCase())
        originalSysOut.flush()
      }
    }
    synchronized (originalSysErr) {
      def stdErrText = entry.getKeyValuePairs().get("stderr")
      if (stdErrText) {
        originalSysErr.print(stdErrText.toUpperCase())
        originalSysErr.flush()
      }
    }
  }

  @Override
  void beforeAll(ExtensionContext context) throws Exception {
    System.out = new PrintStream(new ByteArrayOutputStream())
    System.err = new PrintStream(new ByteArrayOutputStream())
  }

  @Override
  void afterAll(ExtensionContext context) throws Exception {
    synchronized (System.out) {
      System.out.close()
      System.out = originalSysOut
    }
    synchronized (System.err) {
      System.err.close()
      System.err = originalSysErr
    }
  }

  static class HiddenSystemOutAndErr implements BeforeTestExecutionCallback, AfterTestExecutionCallback {

    private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(HiddenSystemOutAndErr.class);

    @Override
    public void beforeTestExecution(ExtensionContext context) {
      var store = context.getStore(NAMESPACE);
      store.put("out", System.out);
      store.put("err", System.err);
      System.setOut(new PrintStream(new ByteArrayOutputStream()));
      System.setErr(new PrintStream(new ByteArrayOutputStream()));
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
      var store = context.getStore(NAMESPACE);
      System.setOut(store.get("out", PrintStream.class));
      System.setErr(store.get("err", PrintStream.class));
    }
  }
}
