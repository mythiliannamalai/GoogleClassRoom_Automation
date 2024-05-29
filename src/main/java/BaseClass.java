import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;


import java.util.Set;

public class BaseClass {
    public WebDriver driver;


    public String Parent_Window=null;

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
        for(String wid : set){
            if(!wid.equals(Parent_Window)){
                driver.switchTo().window(wid);
            }
        }
    }
}
