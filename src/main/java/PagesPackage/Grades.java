package PagesPackage;

import BasePackage.BaseClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Grades extends BaseClass {

    public Grades() throws IOException {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public void Grades( List<String> cnlist) throws Exception {

        for (String cname : cnlist) {
            String text = null;
            boolean classFound = false;
            // Locate the specific classroom and navigate to it
            List<WebElement> list = driver.findElements(By.xpath("(//div[@aria-label='Teaching'])[2]"));
            for (WebElement element : list) {
                List<WebElement> classrooms = element.findElements(By.xpath("./child::a"));
                for (WebElement elements : classrooms) {
                    List<WebElement> classes = elements.findElements(By.xpath(".//div[@class='kXvNXe']/div[@class='asQXV YVvGBb']"));
                    for (WebElement ele : classes) {
                        text = ele.getText();
                        if (text.equals(cname)) {
                            ele.click();
                            classFound = true;
                            break;
                        }
                        else {
                            throw new Exception("Incorrect class name please check your class name...");
                        }
                    }
                }
            }

            // Navigate to the Grades section
            WebElement grades = driver.findElement(By.linkText("Grades"));
            grades.click();

            // Collect assignment headers
            List<WebElement> assignments = driver.findElements(By.xpath("//th[@role='columnheader']"));
            List<String> headers = new ArrayList<>();
            headers.add("Student Name");
            for (WebElement element : assignments) {
                List<WebElement> assignmentTopics = element.findElements(By.xpath("./descendant::div/a"));
                for (WebElement topicElement : assignmentTopics) {
                    String task = topicElement.getText();
                    headers.add(task);
                }
            }

            // Collect student names and their assignment statuses
            List<WebElement> Sname = driver.findElements(By.xpath("//tbody/child::tr/th/div/div/a"));
            List<List<String>> data = new ArrayList<>();

            for (int i = 0; i < Sname.size(); i++) {
                WebElement studentElement = Sname.get(i);
                String stuName = studentElement.getText();
                List<String> studentData = new ArrayList<>();
                studentData.add(stuName);

                List<WebElement> statuses = driver.findElements(By.xpath("//tbody/child::tr[" + (i + 2) + "]/td/div/div/div/span/span/span"));
                for (WebElement statusElement : statuses) {
                    String status = statusElement.getText();
                    studentData.add(status);
                }

                data.add(studentData);
            }
            // getting current date
            LocalDate currentDate =LocalDate.now();

            // Write the collected data to a CSV file
            try (FileWriter out = new FileWriter("Student_Assignments\\" + cname +" ("+currentDate+")"+ ".csv");
                 CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {
                for (List<String> record : data) {
                    printer.printRecord(record);
                    // printer.flush();
                }
                printer.close();
                out.close();

            } catch (IOException es) {
                es.printStackTrace();
            }
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[text()='Home'])[1]")));
            element.click();

        }
        driver.quit();
    }
    public void Grades1( List<String> cnlist) throws Exception {

        for (String cname : cnlist) {
            String text = null;
            boolean classFound = false;
            // Locate the specific classroom and navigate to it
            List<WebElement> list = driver.findElements(By.xpath("(//div[@aria-label='Teaching'])[2]"));
            for (WebElement element : list) {
                List<WebElement> classrooms = element.findElements(By.xpath("./child::a"));
                for (WebElement elements : classrooms) {
                    List<WebElement> classes = elements.findElements(By.xpath(".//div[@class='kXvNXe']/div[@class='asQXV YVvGBb']"));
                    for (WebElement ele : classes) {
                        text = ele.getText();
                        if (text.equals(cname)) {
                            ele.click();
                            classFound = true;
                            break;
                        }
                    }
                    if (classFound) break;
                }
                if (classFound) break;
            }
            // If class not found, print a message and continue to the next iteration
            if (!classFound) {
                System.out.println("Incorrect class name : " + cname);
                continue; // Skip to the next iteration
            }

            // Navigate to the Grades section
            WebElement grades = driver.findElement(By.linkText("Grades"));
            grades.click();

            // Collect assignment headers
            List<WebElement> assignments = driver.findElements(By.xpath("//th[@role='columnheader']"));
            List<String> headers = new ArrayList<>();
            headers.add("Student Name");
            for (WebElement element : assignments) {
                List<WebElement> assignmentTopics = element.findElements(By.xpath("./descendant::div/a"));
                for (WebElement topicElement : assignmentTopics) {
                    String task = topicElement.getText();
                    headers.add(task);
                }
            }

            // Collect student names and their assignment statuses
            List<WebElement> Sname = driver.findElements(By.xpath("//tbody/child::tr/th/div/div/a"));
            List<List<String>> data = new ArrayList<>();

            for (int i = 0; i < Sname.size(); i++) {
                WebElement studentElement = Sname.get(i);
                String stuName = studentElement.getText();
                List<String> studentData = new ArrayList<>();
                studentData.add(stuName);

                List<WebElement> statuses = driver.findElements(By.xpath("//tbody/child::tr[" + (i + 2) + "]/td/div/div/div/span/span/span"));
                for (WebElement statusElement : statuses) {
                    String status = statusElement.getText();
                    studentData.add(status);
                }

                data.add(studentData);
            }
            // getting current date
            LocalDate currentDate =LocalDate.now();

            // Write the collected data to a CSV file
            try (FileWriter out = new FileWriter("Student_Assignments\\" + cname +" ("+currentDate+")"+ ".csv");
                 CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {
                for (List<String> record : data) {
                    printer.printRecord(record);
                    // printer.flush();
                }
                printer.close();
                out.close();

            } catch (IOException es) {
                es.printStackTrace();
            }
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[text()='Home'])[1]")));
            element.click();

        }
        driver.quit();
    }
}
