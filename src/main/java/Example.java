import com.google.api.services.sheets.v4.model.SpreadsheetProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Example {
    public static void main(String[] args) {
        File folder = new File("Student_Assignments"); // Update this path
        List<String> csvFiles = listCsvFilesCreatedToday(folder);

        for (String csvFile : csvFiles) {
            System.out.println(csvFile);
            SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
            String sheetTitle = new File(csvFile).getName();
//            spreadsheetProperties.setTitle(sheetTitle);
            System.out.println(sheetTitle);
        }
    }

    private static List<String> listCsvFilesCreatedToday(File folder) {
        List<String> csvFiles = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".csv")) {
                try {
                    BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    LocalDate fileDate = attrs.creationTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (fileDate.equals(today)) {
                        csvFiles.add(file.getAbsolutePath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return csvFiles;
    }
}
