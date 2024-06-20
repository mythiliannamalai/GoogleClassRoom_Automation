package TestPackage;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class SheetsQuickstart {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens/paths";

    private static final List<String> SCOPES =
            Collections.singletonList(SheetsScopes.SPREADSHEETS);

    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    static Sheets.Spreadsheets spreadsheets;

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = SheetsQuickstart.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String... args) throws IOException, GeneralSecurityException {
        System.out.println(SCOPES);
        getSpreadSheetInstance();
        File folder = new File("Student_Assignments"); // Update this path
        List<String> csvFiles = listCsvFilesCreatedToday(folder);
        System.out.println(csvFiles);

        for (String csvFile : csvFiles) {
            createNewSpreadSheet(csvFile);
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

    private static void createNewSpreadSheet(String csvFilePath) {
        Spreadsheet createdResponse = null;
        try {
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME).build();
            System.out.println(service.toString());

            SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
            String sheetTitle = new File(csvFilePath).getName();
            System.out.println(sheetTitle);
            spreadsheetProperties.setTitle(sheetTitle);

            SheetProperties sheetProperties = new SheetProperties();
            sheetProperties.setTitle("Test 1");
            Sheet sheet = new Sheet().setProperties(sheetProperties);

            Spreadsheet spreadsheet = new Spreadsheet().setProperties(spreadsheetProperties)
                    .setSheets(Collections.singletonList(sheet));
            createdResponse = service.spreadsheets().create(spreadsheet).execute();

            System.out.println("SpreadSheet URL: " + createdResponse.getSpreadsheetUrl());

            List<List<Object>> data = new ArrayList<>(
                    readDataFromCsv(csvFilePath)
            );
            System.out.println(data);
            System.out.println(createdResponse.getSpreadsheetId());

            writeSheet(data, "Test 1!A1", createdResponse.getSpreadsheetId());
//            createNewSheet(createdResponse.getSpreadsheetId(), "Test 2");
//            createNewSheet(createdResponse.getSpreadsheetId(), "Test 3");
//            writeDataGoogleSheets("Test 3", data, createdResponse.getSpreadsheetId());
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getSpreadSheetInstance() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        spreadsheets = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(), getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Google Sheet Java Integrate").build().spreadsheets();
    }

    public static void writeSheet(List<List<Object>> inputData, String sheetAndRange, String existingSpreadSheetID) throws IOException {
        ValueRange body = new ValueRange().setValues(inputData);
        System.out.println(body);
        UpdateValuesResponse result = spreadsheets.values().update(existingSpreadSheetID,sheetAndRange,body)
                .setValueInputOption("USER_ENTERED").execute();
        System.out.printf("%d cells updated.\n", result.getUpdatedCells());
    }

    public static List<List<Object>> readDataFromCsv(String filePath) {
//        String filePath = "Student_Assignments/CIC-BLR-Bootcamp-Section1-Python-Programming (2024-06-06).csv";
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

    //Creates new sheet in the existing sheet
    public static void createNewSheet(String existingSpreadSheetID, String newSheetTitle) throws IOException {
        AddSheetRequest addSheetRequest = new AddSheetRequest();
        SheetProperties sheetProperties = new SheetProperties();

        addSheetRequest.setProperties(sheetProperties);
        addSheetRequest.setProperties(sheetProperties.setTitle(newSheetTitle));

        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();

        List<Request> requestList = new ArrayList<>();
        batchUpdateSpreadsheetRequest.setRequests(requestList);

        Request request = new Request();
        request.setAddSheet(addSheetRequest);
        requestList.add(request);

        batchUpdateSpreadsheetRequest.setRequests(requestList);

        spreadsheets.batchUpdate(existingSpreadSheetID,batchUpdateSpreadsheetRequest).execute();
        System.out.println("Created");
    }


    //write data to existing sheet
    public static void writeDataGoogleSheets(String sheetName, List<List<Object>> data, String existingSpreadSheetID) throws IOException {
        int nextRow = getRows(sheetName, existingSpreadSheetID)+ 1;
        System.out.println(nextRow);
        writeSheet(data,sheetName + "!A1",existingSpreadSheetID);
    }

    private static int getRows(String sheetName, String existingSpreadSheetID) throws IOException {
        List<List<Object>> values = spreadsheets.values().get(existingSpreadSheetID, sheetName)
                .execute().getValues();
        System.out.println(values);
        int numRows = values != null ? values.size() : 0;
        System.out.printf("%d rows retrieved. in '"+sheetName+"'\n", numRows);
        return numRows;
    }
}
