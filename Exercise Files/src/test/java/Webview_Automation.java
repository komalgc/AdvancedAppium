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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Webview_Automation {
    private static final String APP_ANDROID = "/Users/rahullokurte/Desktop/TheApp-v1.9.0.apk";

    private static final String APPIUM = "http://localhost:4723/wd/hub";

    private AppiumDriver driver;

    private void setUpAndroid() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformVersion", "9");
        caps.setCapability("platformName", "Android");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("automationName", "UiAutomator2");
        caps.setCapability("app", APP_ANDROID);
        driver = new AndroidDriver(new URL(APPIUM), caps);

    }


    @Before
    public void setUp() throws Exception {
        setUpAndroid();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Nullable
    public String getContexthandles(AppiumDriver driver) {
        ArrayList<String> contexts = new ArrayList<>(driver.getContextHandles());
        for (String context : contexts) {
            if (!context.contains("NATIVE_APP")) {
                return context;
            }
        }

        return null;
    }

    @Test
    public void testHybridApp() throws InterruptedException {

        WebDriverWait wait = new WebDriverWait(driver, 10);
       WebElement webview= wait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.AccessibilityId("Webview Demo")));

       webview.click();
        Thread.sleep(1000);
       driver.context(getContexthandles(driver));
       driver.get("https://cloudgrey.io");
      assert driver.getTitle().contains("Cloud Grey: Appium Delivered");

    }
}
