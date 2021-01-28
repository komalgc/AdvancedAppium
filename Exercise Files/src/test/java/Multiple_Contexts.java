import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Multiple_Contexts {
    private static final String APP_ANDROID = "/Users/rahullokurte/Desktop/TheApp-v1.9.0.apk";
    private static final String APPIUM = "http://localhost:4723/wd/hub";
    private static final String APPIUM_PRO = "https://appiumpro.com";
    private static final String OTHER_SITE = "https://cloudgrey.io";

    private static final By WEBVIEW_PAGE = MobileBy.AccessibilityId("Webview Demo"); // TODO
    private static final By URL_FIELD = By.xpath("//android.widget.EditText[@content-desc=\"urlInput\"]"); // TODO
    private static final By GO_BUTTON = MobileBy.AccessibilityId("navigateBtn"); // TODO

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("deviceName", "Android Emulator");
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

    @Nullable
    private String getWebContext(AppiumDriver driver) {
        ArrayList<String> contexts = new ArrayList<String>(driver.getContextHandles());
        for (String context : contexts) {
            if (!context.equals("NATIVE_APP")) {
                return context;
            }
        }
        return null;
    }

    @Test
    public void testHybridApp() {
        // 1. Navigate to the webview page
        WebDriverWait wait = new WebDriverWait(driver, 10);
        WebElement webview = wait.until(ExpectedConditions.presenceOfElementLocated(WEBVIEW_PAGE));
        webview.click();

        // 2. Attempt to navigate to an incorrect site
        driver.findElement(URL_FIELD).sendKeys(OTHER_SITE);
        driver.findElement(GO_BUTTON).click();

        // 3. Assert that an error message pops up
       Alert alert = driver.switchTo().alert();
        assert alert.getText().contains("Sorry");
        alert.accept();


        // 4. assert that the webview did not actually go anywhere
        driver.context(getWebContext(driver));
        String bodytext = driver.findElement(By.cssSelector("body")).getText();
        assert bodytext.contains("Please navigate to a webpage");

        // 5. attempt to navigate to the correct site
        WebElement urlfield = driver.findElement(URL_FIELD);
        urlfield.clear();
        urlfield.sendKeys(APPIUM_PRO);
        driver.findElement(GO_BUTTON).click();


        // 6. assert that the webview went to the right place
        driver.context(getWebContext(driver));
        wait.until(ExpectedConditions.titleContains("Appium Pro"));


    }
}
