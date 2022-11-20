package de.scrum_master.stackoverflow.q71087906;

import static java.nio.charset.StandardCharsets.UTF_8;

class ConsumerLambdaUser {
  AsyncHandler asyncHandler;
  Component component;

  Component getComponent() {
    return component;
  }

  void exampleMethod(String input) {
    String name = "my name";
    byte[] data = input.getBytes(UTF_8);

    getComponent().doCall(builder ->
      builder
        .name(name)
        .data(data)
        .build()
    ).whenCompleteAsync(asyncHandler);
  }
}
