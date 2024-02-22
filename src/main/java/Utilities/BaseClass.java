package Utilities;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.model.Media;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class BaseClass {

    public static WebDriver driver; // WebDriver instance
    public static ExtentReports extent = ExtentManager.getInstance();
    public static ExtentTest test;


    @Parameters("browser")
    @BeforeMethod
    public void setUp2(Method method, @Optional("Chrome") String browser) throws MalformedURLException {
        Properties prop = ConfigLoader.loadConfig("src/test/resources/env.properties");
        String environment = prop.getProperty("test.environment");

        String AUTOMATE_USERNAME = prop.getProperty("browserstack.user");
        String AUTOMATE_ACCESS_KEY = prop.getProperty("browserstack.key");
        String URL = "https://" + AUTOMATE_USERNAME + ":" + AUTOMATE_ACCESS_KEY +
                "@hub-cloud.browserstack.com/wd/hub";
        DesiredCapabilities caps = new DesiredCapabilities();

        Map<String, Object> browserstackOptions = new HashMap<>();
        browserstackOptions.put("userName", AUTOMATE_USERNAME);
        browserstackOptions.put("accessKey", AUTOMATE_ACCESS_KEY);
        browserstackOptions.put("projectName", "Testing");
        browserstackOptions.put("buildName", "Automation ");
        browserstackOptions.put("sessionName", method.getName()); // dynamically set session name

        if ("remote".equalsIgnoreCase(environment)) {
            switch (browser.toLowerCase()) {
                case "chrome":
                    browserstackOptions.put("os", "Windows");
                    browserstackOptions.put("osVersion", "10");
                    browserstackOptions.put("browserName", "Chrome");
                    browserstackOptions.put("browserVersion", "latest");
                    break;
                case "firefox":
                    browserstackOptions.put("os", "Windows");
                    browserstackOptions.put("osVersion", "10");
                    browserstackOptions.put("browserName", "Firefox");
                    browserstackOptions.put("browserVersion", "latest");
                    break;
                case "ios":
                    browserstackOptions.put("osVersion", "16");
                    browserstackOptions.put("deviceName", "iPhone 14");
                    browserstackOptions.put("realMobile", "true");
                    break;
                case "android":
                    browserstackOptions.put("osVersion", "12.0");
                    browserstackOptions.put("deviceName", "Samsung Galaxy S22 Ultra");
                    browserstackOptions.put("realMobile", "true");
                    browserstackOptions.put("browserName", "Chrome");
                    browserstackOptions.put("appiumVersion", "1.22.0");
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
            caps.setCapability("bstack:options", browserstackOptions);
            driver = new RemoteWebDriver(new URL(URL), caps);
        } else {

            switch (browser.toLowerCase()) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions chromeOptions = new ChromeOptions();
                    chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--no-sandbox");
                    chromeOptions.addArguments("--disable-gpu");
                    chromeOptions.addArguments("--disable-dev-shm-usage");
                    driver = new ChromeDriver(chromeOptions);
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    firefoxOptions.addArguments("--headless");
                    driver = new FirefoxDriver(firefoxOptions);
                    break;
                case "edge":
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions edgeOptions = new EdgeOptions();
                    edgeOptions.addArguments("--headless");
                    driver = new EdgeDriver(edgeOptions);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        test = extent.createTest(method.getName());
    }



    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


@AfterMethod
public void tearDown(ITestResult result) throws IOException {
        if (result.getStatus()== ITestResult.FAILURE){
            String screenshotPath = TestUtils.takeScreenShot(driver,result.getName());
            test.fail("Details of Failure", MediaEntityBuilder.createScreenCaptureFromPath("pathToScreenshot").build());

        }else{
            test.pass("Test passed successfully");
        }

        extent.flush();
        driver.quit();

}


    public void captureScreenshot(String testName) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String destDir = "screenshots";
        new File(destDir).mkdirs();
        String destFile = testName + ".png";

        try {
            FileUtils.copyFile(source, new File(destDir + "/" + destFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
