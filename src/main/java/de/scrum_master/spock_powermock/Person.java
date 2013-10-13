package de.scrum_master.spock_powermock;

import java.io.*;
import java.util.Date;

public class Person {
    private String lastName;
    private String firstName;
    private Date dateOfBirth;

    public static String defaultFirstName() {
        return "John";
    }

    public static String defaultLastName() {
        return "Doe";
    }

    public Person(String lastName, String firstName, Date dateOfBirth) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.dateOfBirth = dateOfBirth;
    }

    public void writeToFile(String fileName) throws IOException {
        FileOutputStream fos = null;
        PrintStream out = null;
        try {
            fos = new FileOutputStream(fileName);
            out = new PrintStream(fos);
            out.println(this);
        }
        finally {
            if (fos != null)
                try { fos.close(); }
                catch (IOException e) {
                    e.printStackTrace();
                }
            if (out != null)
                out.close();
        }
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public String toString() {
        return "Person{" +
                "lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
