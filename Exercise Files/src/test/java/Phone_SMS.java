import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import io.appium.java_client.android.GsmCallActions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Phone_SMS {
    private static final String APP_ANDROID = "/Users/rahullokurte/Desktop/TheApp-v1.9.0.apk";
    private static final String APPIUM = "http://localhost:4723/wd/hub";
    private static final String PHONE_NUMBER = "5551237890";

    private By VERIFY_SCREEN = MobileBy.AccessibilityId("Verify Phone Number");
    private By SMS_WAITING = MobileBy.xpath("//android.widget.TextView[contains(@text, 'Waiting to receive')]");
    private By SMS_VERIFIED = MobileBy.xpath("//android.widget.TextView[contains(@text, 'verified')]");

    private AndroidDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("autoGrantPermissions" , true);
        caps.setCapability("app", APP_ANDROID);
        driver = new AndroidDriver(new URL(APPIUM), caps);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testPhoneAndSMS() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 10);

        driver.findElement(MobileBy.AccessibilityId("Login Screen")).click();

        driver.makeGsmCall(PHONE_NUMBER, GsmCallActions.CALL);
        Thread.sleep(1000);
        driver.makeGsmCall(PHONE_NUMBER, GsmCallActions.ACCEPT);
        driver.findElement(MobileBy.AccessibilityId("username")).sendKeys("alice");
        Thread.sleep(1000);
        driver.makeGsmCall(PHONE_NUMBER, GsmCallActions.CANCEL);
        driver.navigate().back();

        //Send SMS
        wait.until(ExpectedConditions.presenceOfElementLocated(VERIFY_SCREEN)).click();
        wait.until(ExpectedConditions.presenceOfElementLocated(SMS_WAITING));
        //Send the correct code and send SMS

        driver.sendSMS(PHONE_NUMBER, "Your code is 123456");
        wait.until(ExpectedConditions.presenceOfElementLocated(SMS_VERIFIED));

    }
}
