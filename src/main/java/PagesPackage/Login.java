package PagesPackage;

import BasePackage.BaseClass;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;

public class Login extends BaseClass {
    @FindBy(xpath = "//input[@id='identifierId']")
    WebElement email;
    @FindBy(xpath = "//span[text()='Next']")
    WebElement next_btn;
    @FindBy(xpath = "//input[@aria-label='Enter your password']")
    WebElement password;

    public Login() throws IOException {
        wait= new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }
    public void cls_Login(String un, String pass){
        wait.until(ExpectedConditions.visibilityOf(email)).sendKeys(un);
        wait.until(ExpectedConditions.elementToBeClickable(next_btn)).click();
        wait.until(ExpectedConditions.visibilityOf(password)).sendKeys(pass);
        wait.until(ExpectedConditions.elementToBeClickable(next_btn)).click();
    }
}
