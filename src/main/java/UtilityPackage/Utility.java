package UtilityPackage;

import BasePackage.BaseClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public List<String> readClassName() throws IOException {
        List<String> classnames = new ArrayList<>();
        File file = new File("./src/main/java/ConfigPackage/ClassNames.txt");
        FileReader reader = new FileReader(file);
        BufferedReader bf = new BufferedReader(reader);
        String classname;
        while ((classname = bf.readLine()) != null) {
            System.out.println(classname);
            classnames.add(classname);
        }
        bf.close();
        reader.close();
        return classnames;
    }
}
