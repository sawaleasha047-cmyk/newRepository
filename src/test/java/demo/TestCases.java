package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import demo.wrappers.Wrappers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


//import io.github.bonigarcia.wdm.WebDriverManager;
//import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver=new ChromeDriver();
    public boolean closeFlipkartLoginPopup(WebDriver driver) {
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        WebElement closeBtn = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='✕']"))
        );
        closeBtn.click();
        return true;
    } catch (Exception e) {
        System.out.println("Popup not found. Continuing...");
        return false;
    }
}

  
    /*
     * TODO: Write your tests here with testng @Test annotation. 
     * Follow `testCase01` `testCase02`... format or what is provided in instructions
     */


     
    /*
     * Do not change the provided methods unless necessary, they will help in automation and assessment
     */

@Test(enabled = true)
public void testCase01() throws InterruptedException {
    System.out.println("Beginning Test Case 01");
    double starRating = 4.0;
    driver.get("https://www.flipkart.com/");
    Wrappers.enterTextWrapper(driver, By.xpath("//input[@name='q']"), "Washing Machine");
    Thread.sleep(3000);
    Wrappers.clickOnElementWrapper(driver, By.xpath("//div[contains(text(), 'Popularity')]"));
    Boolean status = Wrappers.searchStarRatingAndPrintCount(driver, By.xpath("//span[contains(@id, 'productRating')]"), starRating);
    Assert.assertTrue(status);
    System.out.println("Ending Test Case 01");
}

@Test(enabled = true)
public void testCase02() throws InterruptedException {
    System.out.println("Beginning Test Case 02");

    int discount = 17;

    driver.get("https://www.flipkart.com/");

    // ✅ Correct popup close locator
    Wrappers.clickOnElementWrapper(driver, By.xpath("//div[@class='JFPqaw']/span"));

    // Search for iPhone
    Wrappers.enterTextWrapper(driver, By.xpath("//input[@name='q']"), "iPhone");
    Thread.sleep(3000);
    Wrappers.clickOnElementWrapper(driver, By.xpath("//button[@type='submit']"));

    Thread.sleep(5000);

    Boolean status = Wrappers.printTitleAndDiscountIphone(
            driver,
            By.xpath("//div[@class='tUxRFH']/div[@class='UkUwK']/span"),
            discount
    );

    Assert.assertTrue(status);

    System.out.println("Ending Test Case 02");
}


@Test(enabled = true)
public void testCase03() throws InterruptedException {
    System.out.println("Beginning Test Case 03");

    driver.get("https://www.flipkart.com/");
    closeFlipkartLoginPopup(driver);

    Wrappers.enterTextWrapper(driver, By.name("q"), "Coffee Mug");
    Wrappers.pressEnter(driver, By.name("q"));

    Thread.sleep(2500);

    Wrappers.clickOnElementWrapper(driver,
            By.xpath("//div[text()='4★ & above']"));

    Thread.sleep(3000);

    Boolean status = Wrappers.printTitleAndImageUrlOfCoffeeMug(
            driver,
            By.xpath("//div[@data-id]"),    // ✅ FINAL correct locator
            "coffee"
    );

    Assert.assertTrue(status);

    System.out.println("Ending Test Case 03");
}

    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        System.out.println("Successfully Created Driver");
    }

@AfterTest
public void endTest() {
    try {
        if (driver != null) {
            driver.quit();
            System.out.println("Driver closed successfully.");
        }
    } catch (Exception e) {
        System.out.println("Driver was already closed. Ignoring.");
    }
}
}