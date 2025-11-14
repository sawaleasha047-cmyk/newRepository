package demo.wrappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.NumberFormat;
import java.time.Duration;

public class Wrappers {

    public static void homePage(WebDriver driver, By locator) {
        try {
            driver.get("https://www.flipkart.com/");
            Thread.sleep( 3000);

            WebElement closePopup = driver.findElement(locator);
            closePopup.click();
        } catch (Exception e) {
            System.out.println("No popup appeared.");
        }
    }

    
    
    
    public static void enterTextWrapper(WebDriver driver, By locator, String textToEnter) {
        System.out.println("Sending Keys");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement inputBox = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            inputBox.clear();
            inputBox.sendKeys(textToEnter);
            inputBox.sendKeys(Keys.ENTER);
        } catch (Exception e) {
            System.out.println("Exception Occurred! " + e.getMessage());
        }
    }

    public static void clickOnElementWrapper(WebDriver driver, By locator) {
        System.out.println("Clicking");
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement clickableElement = wait.until(ExpectedConditions.elementToBeClickable(locator));
            clickableElement.click();
        } catch (Exception e) {
            System.out.println("Exception Occurred! " + e.getMessage());
        }
    }

    public static boolean searchStarRatingAndPrintCount(WebDriver driver, By locator, double starRating) {
        int washingMachineCount = 0;
        boolean success;
        try {
            List<WebElement> starRatingElements = driver.findElements(locator);

            for (WebElement starRatingElement : starRatingElements) {
                try {
                    if (Double.parseDouble(starRatingElement.getText()) <= starRating) {
                        washingMachineCount++;
                    }
                } catch (StaleElementReferenceException e) {
                    // Retry once if element becomes stale
                    starRatingElement = driver.findElement(locator);
                    if (Double.parseDouble(starRatingElement.getText()) <= starRating) {
                        washingMachineCount++;
                    }
                }
            }

            System.out.println("Count of washing machine which has star rating <= " + starRating
                    + ": " + washingMachineCount);

            success = true;
        } catch (Exception e) {
            System.out.println("Exception Occurred!");
            e.printStackTrace();
            success = false;
        }
        return success;
    }

public static boolean printTitleAndDiscountIphone(WebDriver driver, By locator, int discount) {
    boolean success = true;

    try {
        // Map to store iPhone discount -> title
        HashMap<String, String> iphoneTitleDiscountMap = new HashMap<>();

        // Get all discount elements
        List<WebElement> discountPercentageList = driver.findElements(locator);

        for (WebElement productRow : discountPercentageList) {
            String discountPercent = productRow.getText().trim();
            try {
                // Extract numeric discount value
                int discountValue = Integer.parseInt(discountPercent.replaceAll("[^0-9]", ""));
                
                if (discountValue > discount) {
                   
                    WebElement titleElement = productRow.findElement(By.xpath(".//ancestor::div[contains(@class,'tUxRFH')]//div[contains(@class,'KzDlHZ')]"));
                    String iphoneTitle = titleElement.getText().trim();

                    iphoneTitleDiscountMap.put(discountPercent, iphoneTitle);
                }
            } catch (NumberFormatException e) {
                System.out.println("Skipped invalid discount: " + discountPercent);
            } catch (Exception e) {
                System.out.println("Unable to fetch title for discount: " + discountPercent);
            }
        }

        // Print results
        for (Map.Entry<String, String> entry : iphoneTitleDiscountMap.entrySet()) {
            System.out.println("iPhone discount percentage: " + entry.getKey() + " | Title: " + entry.getValue());
        }

    } catch (Exception e) {
        System.out.println("Exception Occurred in printTitleAndDiscountIphone!");
        e.printStackTrace();
        success = false;
    }

    return success;
}

public static boolean printTitleAndImageUrlOfCoffeeMug(WebDriver driver, By locator, String searchKeyword) {
    try {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Wait for product cards
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-id]")));

        // Find all products
        List<WebElement> products = driver.findElements(By.xpath("//div[@data-id]"));

        if (products.isEmpty()) {
            System.out.println("No products found!");
            return false;
        }

        System.out.println("Total products found: " + products.size());

        for (WebElement product : products) {

            WebElement titleElement = null;

            //  Try 3 possible title locators
            List<By> titleXpaths = Arrays.asList(
                By.xpath(".//a[contains(@class,'s1Q9rs')]"),
                By.xpath(".//a[contains(@class,'IRpwTa')]"),
                By.xpath(".//a[contains(@class,'V0OXxd')]")   
            );

            for (By bx : titleXpaths) {
                try {
                    titleElement = product.findElement(bx);
                    break; // found, exit loop
                } catch (Exception ignored) {}
            }

            if (titleElement == null) {
                System.out.println(" Title not found for a product. Skipping...");
                continue;
            }

            String title = titleElement.getText();

            //  Image locator stays same
            WebElement imgElement = product.findElement(
                By.xpath(".//img[contains(@class,'_396cs4') or contains(@class,'_2r_T1I')]")
            );
            String imgUrl = imgElement.getAttribute("src");

            System.out.println("Title: " + title);
            System.out.println("Image URL: " + imgUrl);
        }

        return true;

    } catch (Exception e) {
        System.out.println("Exception Occurred: " + e.getMessage());
        return false;
    }
}


public static void pressEnter(WebDriver driver, By locator) {
    try {
        WebElement element = driver.findElement(locator);
        element.sendKeys(Keys.ENTER);
        System.out.println("Pressed ENTER on element: " + locator.toString());
    } catch (Exception e) {
        System.out.println("Unable to press ENTER on element: " + locator.toString());
        e.printStackTrace();
    }
}


}
