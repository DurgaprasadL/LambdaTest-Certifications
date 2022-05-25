package com.lambdatest;

import Utills.WebDriverHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

public class AddProduct {
    private RemoteWebDriver driver;
    WebDriverHelper driverHelper;


    //Elements
    protected static final By SHOP_BY_CATEGORY_NAVIGATION = By.cssSelector("shop-by-category");

    private String Status = "failed";

    @BeforeMethod public void setup(Method m, ITestContext ctx) throws MalformedURLException {
        String username = System.getenv("LT_USERNAME") == null ? "Your LT Username" : System.getenv("LT_USERNAME");
        String authKey = System.getenv("LT_ACCESS_KEY") == null ? "Your LT AccessKey" : System.getenv("LT_ACCESS_KEY");
        String hub = "@hub.lambdatest.com/wd/hub";
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platform", "win10");
        caps.setCapability("browserName", "chrome");
        caps.setCapability("version", "latest");
        caps.setCapability("build", "TestNG With Java");
        caps.setCapability("name", m.getName() + " - " + this.getClass().getName());
        caps.setCapability("plugin", "git-testng");
        String[] Tags = new String[] { "Feature", "Falcon", "Severe" };
        caps.setCapability("tags", Tags);
        driver = new RemoteWebDriver(new URL("https://" + username + ":" + authKey + hub), caps);
        driverHelper = new WebDriverHelper(driver);
    }

    @Test public void addProducts() {
        driverHelper.getURL("https://ecommerce-playground.lambdatest.io/");
        driverHelper.waitForPresence(SHOP_BY_CATEGORY_NAVIGATION, 30);
        driverHelper.click(By.className("shop-by-category"));
        driverHelper.click(By.cssSelector(".mz-pure-drawer:first-of-type .navbar-nav>li:nth-of-type(3)"));
        driverHelper.click(By.cssSelector("#container .manufacturer .mz-filter-group-content div:first-of-type div"));
        driverHelper.mouseHoverOnElement(By.cssSelector(".carousel-item:first-of-type [title='iPod Touch']"));
        driverHelper.staleElementRefresh(By.cssSelector(".carousel-item:first-of-type [title='iPod Touch']"));
        driverHelper.waitForTime(5);
        driverHelper.mouseHoverOnElement(By.cssSelector(".carousel-item:first-of-type [title='iPod Touch']"));
        driverHelper.waitForClickable(By.cssSelector("div[data-view_id='grid'] .product-layout:first-of-type button[title='Add to Cart']"), 30);
        driverHelper.click(By.cssSelector("div[data-view_id='grid'] .product-layout:first-of-type button[title='Add to Cart']"));
        driverHelper.click(By.cssSelector("#notification-box-top .btn-primary"));
        driverHelper.waitForVisibility(By.cssSelector("#content .btn-secondary"), 30);
        driverHelper.click(By.cssSelector("#content .btn-secondary"));
        driverHelper.waitForVisibility(By.xpath("//strong[contains(text(),'This is a dummy website for Web Automation Testing')]"), 30);
        boolean value = driver.findElement(By.xpath("//strong[contains(text(),'This is a dummy website for Web Automation Testing')]")).isDisplayed();
        Assert.assertTrue(value, "Element is not displayed.");
        Status = "Passed";
    }

    @AfterMethod public void tearDown() {
        driver.executeScript("lambda-status=" + Status);
        driver.quit();
    }
}
