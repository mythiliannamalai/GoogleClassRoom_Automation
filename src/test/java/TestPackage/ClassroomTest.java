package TestPackage;

import BasePackage.BaseClass;
import PagesPackage.Grades;
import PagesPackage.Login;
import UtilityPackage.Utility;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.List;

import static TestPackage.SheetsQuickstart.*;

public class ClassroomTest extends BaseClass {
    static Login login;
    static Utility utility;
    static Grades grades;

    public ClassroomTest() throws IOException {
        super();
    }
    @Test
    public static void initialization() throws Exception {
        setup();
        login = new Login();
        login.cls_Login(prop.getProperty("email"),prop.getProperty("password"));
        utility = new Utility();
        List<String> cnlist = utility.readClassName();
        grades =new Grades();
        grades.Grades1(cnlist);

        //Calling Google Sheet API methods
        getSpreadSheetInstance();
        File folder = new File("Student_Assignments"); // Update this path
        List<String> csvFiles = listCsvFilesCreatedToday(folder);
        System.out.println(csvFiles);

        for (String csvFile : csvFiles) {
            createNewSpreadSheet(csvFile);
        }
    }

}
