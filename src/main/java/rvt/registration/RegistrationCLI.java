package rvt.registration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class RegistrationCLI {
    private final FileHandler fileHandler;
    private final Scanner scanner = new Scanner(System.in);

    private static final Pattern NAME_PATTERN = Pattern.compile("^[A-Za-zÀ-ž]{3,}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[^@\s]+@[^@\s]+\\.[^@\s]+$");
    private static final Pattern PCODE_PATTERN = Pattern.compile("^\\d{6,14}$");

    public RegistrationCLI() {
        this.fileHandler = new FileHandler("data/users.csv");
    }

    public void start() {
        System.out.println("Student Registration CLI");
        while (true) {
            System.out.println("\nChoose action: register, show, remove, edit, exit");
            System.out.print("> ");
            String cmd = scanner.nextLine().trim().toLowerCase();
            switch (cmd) {
                case "register": doRegister(); break;
                case "show": doShow(); break;
                case "remove": doRemove(); break;
                case "edit": doEdit(); break;
                case "exit": System.out.println("Bye"); return;
                default: System.out.println("Unknown command");
            }
        }
    }

    private void doRegister() {
        String first = prompt("First name (letters only, min 3): ", NAME_PATTERN);
        String last = prompt("Last name (letters only, min 3): ", NAME_PATTERN);
        String email = prompt("Email: ", EMAIL_PATTERN);
        String pcode = prompt("Personal code (digits 6-14): ", PCODE_PATTERN);

        // check duplicate personal code
        List<StudentRecord> all = fileHandler.readAll();
        for (StudentRecord r : all) {
            if (r.getPersonalCode().equals(pcode)) {
                System.out.println("A user with that personal code already exists.");
                return;
            }
        }

        StudentRecord s = new StudentRecord(first, last, email, pcode, LocalDateTime.now());
        fileHandler.append(s);
        System.out.println("Registered.");
    }

    private void doShow() {
        List<StudentRecord> all = fileHandler.readAll();
        if (all.isEmpty()) { System.out.println("No records."); return; }
        printTable(all);
    }

    private void doRemove() {
        System.out.print("Enter personal code to remove: ");
        String code = scanner.nextLine().trim();
        List<StudentRecord> all = fileHandler.readAll();
        List<StudentRecord> remaining = new ArrayList<>();
        boolean removed = false;
        for (StudentRecord r : all) {
            if (r.getPersonalCode().equals(code)) { removed = true; continue; }
            remaining.add(r);
        }
        if (removed) {
            fileHandler.overwriteAll(remaining);
            System.out.println("Removed.");
        } else {
            System.out.println("Not found.");
        }
    }

    private void doEdit() {
        System.out.print("Enter personal code to edit: ");
        String code = scanner.nextLine().trim();
        List<StudentRecord> all = fileHandler.readAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            StudentRecord r = all.get(i);
            if (r.getPersonalCode().equals(code)) {
                found = true;
                String first = promptAllowEmpty("First name (leave empty to keep) : ", NAME_PATTERN);
                String last = promptAllowEmpty("Last name (leave empty to keep) : ", NAME_PATTERN);
                String email = promptAllowEmpty("Email (leave empty to keep) : ", EMAIL_PATTERN);
                String newFirst = first.isEmpty() ? r.getFirstName() : first;
                String newLast = last.isEmpty() ? r.getLastName() : last;
                String newEmail = email.isEmpty() ? r.getEmail() : email;
                StudentRecord updated = new StudentRecord(newFirst, newLast, newEmail, r.getPersonalCode(), LocalDateTime.now());
                all.set(i, updated);
                fileHandler.overwriteAll(all);
                System.out.println("Updated.");
                break;
            }
        }
        if (!found) System.out.println("Not found.");
    }

    private String prompt(String label, Pattern p) {
        while (true) {
            System.out.print(label);
            String v = scanner.nextLine().trim();
            if (p.matcher(v).matches()) return v;
            System.out.println("Invalid input, try again.");
        }
    }

    private String promptAllowEmpty(String label, Pattern p) {
        while (true) {
            System.out.print(label);
            String v = scanner.nextLine().trim();
            if (v.isEmpty()) return "";
            if (p.matcher(v).matches()) return v;
            System.out.println("Invalid input, try again.");
        }
    }

    private void printTable(List<StudentRecord> list) {
        int w1 = 15, w2 = 15, w3 = 25, w4 = 14, w5 = 19;
        String fmt = "| %-" + w1 + "s | %-" + w2 + "s | %-" + w3 + "s | %-" + w4 + "s | %-" + w5 + "s |%n";
        String sep = "+" + "-".repeat(w1 + 2) + "+" + "-".repeat(w2 + 2) + "+" + "-".repeat(w3 + 2) + "+" + "-".repeat(w4 + 2) + "+" + "-".repeat(w5 + 2) + "+";
        System.out.println(sep);
        System.out.printf(fmt, "First name", "Last name", "Email", "P.code", "Registered At");
        System.out.println(sep);
        for (StudentRecord r : list) {
            String email = r.getEmail();
            if (email.length() > w3) email = email.substring(0, w3-3) + "...";
            System.out.printf(fmt, r.getFirstName(), r.getLastName(), email, r.getPersonalCode(), r.getRegistrationDateTime());
        }
        System.out.println(sep);
    }
}
