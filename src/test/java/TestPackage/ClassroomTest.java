package TestPackage;

import BasePackage.BaseClass;
import PagesPackage.Grades;
import PagesPackage.Login;
import UtilityPackage.Utility;


import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.interpol.ConfigurationInterpolator;
import org.apache.commons.configuration2.interpol.EnvironmentLookup;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ClassroomTest extends BaseClass {
    static Login login;
    static Utility utility;
    static Grades grades;

    public ClassroomTest() throws IOException {
        super();
    }
    @Test
    public static void initialization() throws Exception {
        Configurations configurations = new Configurations();
        // Load properties file
        PropertiesConfiguration config = configurations.properties(new File("src/main/resources/config.properties"));

        // Adding environment variable lookup
        ConfigurationInterpolator interpolator = config.getInterpolator();
        interpolator.addDefaultLookup(new EnvironmentLookup());

        String email = config.getString("email");
        String password = config.getString("password");


        setup();
        login = new Login();
        utility = new Utility();
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email or password environment variables are not set.");
        }
        login.cls_Login(email, password);
    List<String> cnlist = utility.readClassName();
    grades = new Grades();
    //grades.Grades(cnlist);
        grades.Grades1(cnlist);
    }
}




