import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Google_classroom {
    public WebDriver driver;
    public String Parent_Window = null;
    public void setup() throws InterruptedException {

        driver = new ChromeDriver();
        driver.get("https://classroom.google.com/");
        driver.manage().window().maximize();
        Thread.sleep(2000);
        WebElement sign_in = driver.findElement(By.xpath("(//a[@data-gfe-link-id='sign-in'])[1]"));
        sign_in.click();
        Thread.sleep(2000);
        Actions actions = new Actions(driver);
        actions.sendKeys(Keys.DOWN).perform();
        actions.sendKeys(Keys.ENTER).perform();
        Parent_Window = driver.getWindowHandle();
        Set<String> set = driver.getWindowHandles();
        for (String wid : set) {
            if (!wid.equals(Parent_Window)) {
                driver.switchTo().window(wid);
            }
        }
    }
    public void login(String email, String password) throws InterruptedException {
        WebElement un = driver.findElement(By.xpath("//input[@id='identifierId']"));
        un.sendKeys(email);
        driver.findElement(By.xpath("//span[text()='Next']")).click();
        Thread.sleep(9000);
        WebElement pass = driver.findElement(By.xpath("//input[@aria-label='Enter your password']"));
        pass.sendKeys(password);
        driver.findElement(By.xpath("//span[text()='Next']")).click();
    }
    public void Grades(String email, String password,String cn) throws InterruptedException {
        setup();
        login(email,password);
        String text = null;
        Thread.sleep(9000);

        // Locate the specific classroom and navigate to it
        List<WebElement> list = driver.findElements(By.xpath("(//div[@aria-label='Teaching'])[2]"));
        for (WebElement element : list) {
            List<WebElement> classrooms = element.findElements(By.xpath("./child::a"));
            for (WebElement elements : classrooms) {
                List<WebElement> classes = elements.findElements(By.xpath(".//div[@class='kXvNXe']/div[@class='asQXV YVvGBb']"));
                for (WebElement ele : classes) {
                    text = ele.getText();
                    Thread.sleep(5000);
                    if (text.equals(cn)) {
                        ele.click();
                        break;
                    }
                }
            }
        }
        Thread.sleep(9000);

        // Navigate to the Grades section
        WebElement grades = driver.findElement(By.linkText("Grades"));
        grades.click();
        Thread.sleep(9000);

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

        // Write the collected data to a CSV file
        try (FileWriter out = new FileWriter("C:\\Users\\Admin\\IdeaProjects\\Google_ClassRoom\\Student_Assignments\\"+cn+".csv");
             CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(headers.toArray(new String[0])))) {
            for (List<String> record : data) {
                printer.printRecord(record);
            }
            printer.flush();
        } catch (IOException es) {
            es.printStackTrace();
        }
    }

}
