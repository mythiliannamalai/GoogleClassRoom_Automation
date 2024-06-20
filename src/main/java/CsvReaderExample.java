import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvReaderExample {

    public static List<List<Object>> readDataFromCsv(String filePath) {
        List<List<Object>> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> allRows = reader.readAll();
            for (String[] row : allRows) {
                List<Object> rowData = new ArrayList<>(Arrays.asList(row));
                data.add(rowData);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) {
        String filePath = "C:\\Users\\moham\\OneDrive\\Desktop\\CIC-BLR-TRIAL-Internship-Python-Programming.csv";
        List<List<Object>> data = readDataFromCsv(filePath);
        System.out.println(data);
        // Print the data
        for (List<Object> row : data) {
            for (Object cell : row) {
                System.out.print(cell + "\t");
            }
            System.out.println();
        }
    }
}
