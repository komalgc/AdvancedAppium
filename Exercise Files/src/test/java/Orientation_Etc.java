import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Orientation_Etc {
    private static final String APP_ANDROID = "/Users/rahullokurte/Desktop/TheApp-v1.9.0.apk";
    private static final String APPIUM = "http://localhost:4723/wd/hub";

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("platformVersion", "9");
        caps.setCapability("deviceName", "Android Emulator");
        caps.setCapability("app", APP_ANDROID);
        driver = new AndroidDriver(new URL(APPIUM), caps);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testScreenMethods() throws IOException {

        ScreenOrientation curOrientation = driver.getOrientation();
        System.out.println(curOrientation);
        if (curOrientation != ScreenOrientation.LANDSCAPE) {
            driver.rotate(ScreenOrientation.LANDSCAPE);
        }

        //Get the Dimensions
        Dimension size = driver.manage().window().getSize();
        System.out.println(size);

        //Get Screenshot
        File screenshot = driver.getScreenshotAs(OutputType.FILE);

        File savefile = new File("/Users/rahullokurte/Desktop/KomalWorkspace/Advanced-Appium/screen.jpeg");

        FileUtils.copyFile(screenshot, savefile);
        driver.rotate(ScreenOrientation.PORTRAIT);


    }
}
