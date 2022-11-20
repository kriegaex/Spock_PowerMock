package de.scrum_master.stackoverflow.q71087906;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class Component {
  public CompletableFuture<Response> doCall(Consumer<Request> requestConsumer) {
    requestConsumer.accept(new Request());
    return CompletableFuture.completedFuture(new Response("real"));
  }
}
