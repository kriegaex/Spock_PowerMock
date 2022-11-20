package de.scrum_master.stackoverflow.q71087906;

import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class AsyncHandler implements BiConsumer<Response, Throwable> {
  @Override
  public void accept(Response response, Throwable throwable) {

  }

  @NotNull
  @Override
  public BiConsumer<Response, Throwable> andThen(@NotNull BiConsumer<? super Response, ? super Throwable> after) {
    return BiConsumer.super.andThen(after);
  }
}
