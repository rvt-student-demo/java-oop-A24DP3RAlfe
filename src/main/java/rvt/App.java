package rvt;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class App {
public static void main(String[] args) {
    File file  = new File("src/main/java/rvt/ToDolist.java");
    try (Scanner reader = new Scanner(file)) {
        if (reader.hasNextLine()) {
            System.out.println(reader.nextLine());
        } else {
            System.out.println("File is empty.");
        }
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}
}
