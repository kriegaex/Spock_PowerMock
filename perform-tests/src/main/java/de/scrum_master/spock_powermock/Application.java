package de.scrum_master.spock_powermock;

import java.io.IOException;
import java.util.GregorianCalendar;

public class Application {
  public static void main(String[] args) throws IOException {
    Person person = new Person("Kriegisch", "Alexander", new GregorianCalendar(1971, 5 - 1, 8).getTime());
    final String fileName = "kriegisch.txt";
    person.writeToFile(fileName);
    System.out.println(person + " written to file '" + fileName + "'");
  }
}
