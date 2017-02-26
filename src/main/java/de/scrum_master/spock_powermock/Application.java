package de.scrum_master.spock_powermock;

import java.io.IOException;
import java.util.Date;

public class Application {
  public static void main(String[] args) throws IOException {
    Person person = new Person("Kriegisch", "Alexander", new Date(1971 - 1900, 5 - 1, 8));
    final String fileName = "kriegisch.txt";
    person.writeToFile(fileName);
    System.out.println(person + " written to file '" + fileName + "'");
  }
}
