package de.scrum_master.testing;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.currentThread;

public class MultiplexingConcurrentPrintStream extends PrintStream {
  private final Map<Long, ByteArrayOutputStream> buffers = Collections.synchronizedMap(new HashMap<>());

  public MultiplexingConcurrentPrintStream(PrintStream original) {
    super(original);
  }

  @Override
  public synchronized void write(int b) {
    if (isRegistered())
      getBuffer().write(b);
    else
      super.write(b);
  }

  @Override
  public synchronized void write(@NotNull byte[] buf, int off, int len) {
    if (isRegistered())
      getBuffer().write(buf, off, len);
    else
      super.write(buf, off, len);
  }

  public synchronized void register() {
    register(currentThread().getId());
  }

  public synchronized void register(long threadId) {
    if (!isRegistered(threadId))
      buffers.put(threadId, new ByteArrayOutputStream());
  }

  private synchronized void unregister(long threadId) {
    if (!isRegistered(threadId))
      return;
    ByteArrayOutputStream buffer = buffers.get(threadId);
    try {
      buffer.close();
    }
    catch (IOException ignored) {}
    buffers.remove(threadId);
  }

  private boolean isRegistered() {
    return isRegistered(currentThread().getId());
  }

  private boolean isRegistered(long threadId) {
    return buffers.containsKey(threadId);
  }

  public synchronized void dump() {
    dump(currentThread().getId());
  }

  public synchronized void dump(long threadId) {
    final byte[] bytes = getBuffer(threadId).toByteArray();
    super.write(bytes, 0, bytes.length);
    super.flush();
    unregister(threadId);
  }

  private ByteArrayOutputStream getBuffer() {
    return getBuffer(currentThread().getId());
  }

  private ByteArrayOutputStream getBuffer(long threadId) {
    ByteArrayOutputStream buffer = buffers.get(threadId);
    if (buffer == null) {
      buffer = new ByteArrayOutputStream();
      buffers.put(threadId, buffer);
    }
    return buffer;
  }

  public void clearAll() {
    for (ByteArrayOutputStream buffer : buffers.values()) {
      try {
        buffer.close();
      }
      catch (IOException ignored) {}
    }
    buffers.clear();
  }
}
