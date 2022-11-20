package de.scrum_master.stackoverflow.q71087906;

import java.util.Arrays;

public class Request {
  private String name;
  private byte[] data;

  public Request() {
    System.out.println("Creating request");
  }

  public Request name(String name) {
    this.name = name;
    return this;
  }

  public Request data(byte[] data) {
    this.data = data;
    return this;
  }

  public Request build() {
    if (name == null || name.trim().equals(""))
      throw new RuntimeException("invalid request name");
    if (data == null)
      throw new RuntimeException("invalid request data");
    System.out.println("Building " + this);
    return this;
  }

  @Override
  public String toString() {
    return "Request{" +
      "name='" + name + '\'' +
      ", data=" + Arrays.toString(data) +
      '}';
  }
}
