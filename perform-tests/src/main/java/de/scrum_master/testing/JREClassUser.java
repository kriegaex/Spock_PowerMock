package de.scrum_master.testing;

import java.io.IOException;
import java.net.URL;
import java.util.StringJoiner;
import java.util.UUID;

public class JREClassUser {
  public boolean areStringsEqual(String a, String b) {
    System.out.println("a = " + a + ", b = " + b);
    return a.equals(b);
  }

  public boolean areStringsEqualIgnoreCase(String a, String b) {
    System.out.println("a = " + a + ", b = " + b);
    return a.equalsIgnoreCase(b);
  }

  public String stringToUpperCase(String string) {
    System.out.println("Converting to upper case: " + string);
    return string.toUpperCase();
  }

  public String joinStrings(StringJoiner stringJoiner, String... strings) {
    for (String string : strings)
      stringJoiner.add(string);
    System.out.println("Length of joined string: " + stringJoiner.length());
    return stringJoiner.toString();
  }

  public String uuidToString(UUID uuid) {
    return uuid.toString();
  }

  public Object getURLContent(URL url) throws IOException {
    return url.getContent();
  }
}
