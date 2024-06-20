import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CleanCSV {

    public static void main(String[] args) {
        String filePath = "Student_Assignments/CIC-BLR-Bootcamp-Section1-Python-Programming (2024-06-06).csv"; // Update with your file path

        List<String[]> cleanedData = readAndCleanCSV(filePath);

        if (cleanedData != null) {
            writeCSV(filePath, cleanedData);
            System.out.println("CSV cleaned and saved to: " + filePath);
        } else {
            System.out.println("No data to write.");
        }
    }

    private static List<String[]> readAndCleanCSV(String filePath) {
        List<String[]> cleanedData = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> allRows = reader.readAll();
            boolean isHeader = true;
            for (String[] row : allRows) {
                if (isHeader) {
                    cleanedData.add(row); // Add the header row as it is
                    isHeader = false;
                } else {
                    List<String> cleanedRow = new ArrayList<>();
                    boolean rowHasContent = false;
                    for (String cell : row) {
                        if (cell != null && !cell.trim().isEmpty()) {
                            rowHasContent = true;
                            cleanedRow.add(cell.trim());
                        } else {
                            cleanedRow.add(""); // Add empty cell to maintain column count
                        }
                    }
                    if (rowHasContent) {
                        cleanedData.add(cleanedRow.toArray(new String[0]));
                    }
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return null;
        }
        return cleanedData;
    }

    private static void writeCSV(String filePath, List<String[]> data) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
