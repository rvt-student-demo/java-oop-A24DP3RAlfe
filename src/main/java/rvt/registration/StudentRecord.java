package rvt.registration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StudentRecord {
    private String firstName;
    private String lastName;
    private String email;
    private String personalCode;
    private String registrationDateTime; // formatted

    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StudentRecord(String firstName, String lastName, String email, String personalCode, LocalDateTime registeredAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.personalCode = personalCode;
        this.registrationDateTime = registeredAt.format(FORMAT);
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPersonalCode() { return personalCode; }
    public String getRegistrationDateTime() { return registrationDateTime; }

    public String toCSV() {
        return String.join(",",
                escape(firstName),
                escape(lastName),
                escape(email),
                escape(personalCode),
                escape(registrationDateTime)
        );
    }

    public static StudentRecord fromCSV(String line) {
        String[] parts = line.split(",");
        if (parts.length < 5) return null;
        return new StudentRecord(unescape(parts[0]), unescape(parts[1]), unescape(parts[2]), unescape(parts[3]), LocalDateTime.parse(parts[4], FORMAT));
    }

    private static String escape(String s) {
        return s.replace("\n", " ").replace(",", "\\,");
    }

    private static String unescape(String s) {
        return s.replace("\\,", ",");
    }
}
