package rvt.registration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private final Path csvPath;

    public FileHandler(String path) {
        this.csvPath = Path.of(path);
        ensureFile();
    }

    private void ensureFile() {
        try {
            File f = csvPath.toFile();
            if (!f.exists()) {
                f.getParentFile().mkdirs();
                f.createNewFile();
                // write header
                try (BufferedWriter w = new BufferedWriter(new FileWriter(f, true))) {
                    w.write("firstName,lastName,email,personalCode,registeredAt\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void append(StudentRecord s) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(csvPath.toFile(), true))) {
            w.write(s.toCSV());
            w.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized List<StudentRecord> readAll() {
        List<StudentRecord> out = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(csvPath);
            boolean first = true;
            for (String line : lines) {
                if (first) { first = false; continue; } // skip header
                if (line.trim().isEmpty()) continue;
                StudentRecord r = StudentRecord.fromCSV(line);
                if (r != null) out.add(r);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out;
    }

    public synchronized void overwriteAll(List<StudentRecord> list) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(csvPath.toFile(), false))) {
            w.write("firstName,lastName,email,personalCode,registeredAt\n");
            for (StudentRecord s : list) {
                w.write(s.toCSV());
                w.write("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
