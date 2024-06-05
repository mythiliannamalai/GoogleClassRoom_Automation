package TestPackage;

import BasePackage.BaseClass;
import PagesPackage.Grades;
import PagesPackage.Login;
import UtilityPackage.Utility;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class ClassroomTest extends BaseClass {
    static Login login;
    static Utility utility;
    static Grades grades;

    public ClassroomTest() throws IOException {
        super();
    }
    @Test
    public static void initialization() throws InterruptedException, IOException {
        setup();
        login = new Login();
        login.cls_Login(prop.getProperty("email"),prop.getProperty("password"));
        utility = new Utility();
        List<String> cnlist = utility.readClassName();
        grades =new Grades();
        grades.Grades(cnlist);
    }

}
