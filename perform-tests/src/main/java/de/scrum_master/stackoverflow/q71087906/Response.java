package de.scrum_master.stackoverflow.q71087906;

public class Response {
  private String text;

  public Response(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return "Response{" +
      "text='" + text + '\'' +
      '}';
  }
}
