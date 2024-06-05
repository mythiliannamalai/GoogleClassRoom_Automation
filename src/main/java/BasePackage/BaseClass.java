package BasePackage;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BaseClass {
    public static WebDriver driver;
    public WebDriverWait wait;
    public static String Parent_Window = null;
    public static Properties prop;
    public BaseClass() throws IOException {
        prop = new Properties();
        FileInputStream ip;
        ip = new FileInputStream(
                "./src/main/java/ConfigPackage/User_Credentials.properties");
        prop.load(ip);
    }

    public static void setup() throws InterruptedException {

        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(prop.getProperty("url"));
        driver.manage().window().maximize();

        WebElement sign_in = driver.findElement(By.xpath("(//a[@data-gfe-link-id='sign-in'])[1]"));
        sign_in.click();

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
}
