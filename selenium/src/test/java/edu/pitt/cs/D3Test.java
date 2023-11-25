package edu.pitt.cs;

import org.openqa.selenium.Cookie;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import java.time.Duration;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class D3Test {
    private WebDriver driver;
    private Map<String, Object> vars;
    JavascriptExecutor js;
    
    // 添加基本URL
    private static final String BASE_URL = "http://localhost:8080/";

    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        // driver = new ChromeDriver();
        js = (JavascriptExecutor) driver;
        vars = new HashMap<String, Object>();
        
        // 设置隐式等待时间为30秒
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        
        // 统一窗口大小
        driver.manage().window().setSize(new Dimension(1200, 800));
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    // 设置cookie的方法
    private void setCookies(String... cookieValues) {
        driver.get(BASE_URL); // 使用BASE_URL
        for (int i = 0; i < cookieValues.length; i++) {
            Cookie newCookie = new Cookie(String.valueOf(i + 1), cookieValues[i]);
            driver.manage().addCookie(newCookie);
        }
    }

        @Test
      public void tEST1LINKS() {
          driver.get(BASE_URL);
          WebElement resetLink = driver.findElement(By.linkText("Reset"));
          assertEquals(BASE_URL + "reset", resetLink.getAttribute("href"));
      }


  

      @Test
      public void tEST2RESET() {
          setCookies("true", "true", "true");
          driver.get(BASE_URL);
          driver.findElement(By.linkText("Reset")).click();
          List<WebElement> cats = driver.findElements(By.cssSelector("#listing .list-group-item"));
          assertTrue(cats.get(0).getText().contains("ID 1. Jennyanydots"));
          assertTrue(cats.get(1).getText().contains("ID 2. Old Deuteronomy"));
          assertTrue(cats.get(2).getText().contains("ID 3. Mistoffelees"));
      }


    @Test
    public void tEST3CATALOG() throws URISyntaxException {
        driver.get(BASE_URL);
        driver.findElement(By.linkText("Catalog")).click();
        WebElement element = driver.findElement(By.cssSelector("li:nth-child(3) > img"));
        String attribute = element.getAttribute("src");
        URI uri = new URI(attribute);
        String path = uri.getPath();
        assertEquals("/images/cat2.jpg", path);
    }

     

    @Test
    public void tEST4LISTING() {
        driver.get(BASE_URL);
        driver.manage().window().setSize(new Dimension(1310, 912));
        driver.findElement(By.linkText("Catalog")).click();
        {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".list-group-item:nth-child(3)")));
        }
        assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
    }

    @Test
    public void tEST5RENTACAT() {
        driver.get(BASE_URL);
        driver.manage().window().setSize(new Dimension(1310, 912));
        driver.findElement(By.linkText("Rent-A-Cat")).click();
        {
            List<WebElement> elements = driver.findElements(By.xpath("//button[@onclick='rentSubmit()']"));
            assert(elements.size() > 0);
        }
        {
            List<WebElement> elements = driver.findElements(By.xpath("//button[@onclick='returnSubmit()']"));
            assert(elements.size() > 0);
        }
    }

    @Test
    public void tEST6RENT() {
        driver.get(BASE_URL);
        driver.manage().window().setSize(new Dimension(1310, 912));
        driver.findElement(By.linkText("Rent-A-Cat")).click();
        driver.findElement(By.id("rentID")).click();
        driver.findElement(By.id("rentID")).sendKeys("1");
        driver.findElement(By.cssSelector(".form-group:nth-child(3) .btn")).click();
        assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li")).getText(), is("Rented out"));
        assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li[2]")).getText(), is("ID 2. Old Deuteronomy"));
        assertThat(driver.findElement(By.xpath("//div[@id='listing']/ul/li[3]")).getText(), is("ID 3. Mistoffelees"));
        assertThat(driver.findElement(By.xpath("//div[@id='rentResult']")).getText(), is("Success!"));
    }

    @Test
    public void tEST7RETURN() {
        // 设置前置条件: 猫 ID 2 已被出租
        setCookies("false", "true", "false");

        driver.get(BASE_URL);
        driver.findElement(By.linkText("Rent-A-Cat")).click();

        // 前置条件检查
        List<WebElement> catsBeforeReturn = driver.findElements(By.cssSelector("#listing .list-group-item"));
        assertTrue(catsBeforeReturn.get(1).getText().contains("Rented out"));

        driver.findElement(By.id("returnID")).sendKeys("2");
        driver.findElement(By.cssSelector(".form-group:nth-child(4) .btn")).click();

        List<WebElement> catsAfterReturn = driver.findElements(By.cssSelector("#listing .list-group-item"));
        assertTrue(catsAfterReturn.get(1).getText().contains("ID 2. Old Deuteronomy"));
    }

    @Test
    public void tEST8FEEDACAT() {
        driver.get(BASE_URL);
        driver.manage().window().setSize(new Dimension(1440, 809));
        driver.findElement(By.linkText("Feed-A-Cat")).click();
        {
            List<WebElement> elements = driver.findElements(By.xpath("//button[contains(.,'Feed')]"));
            assert(elements.size() > 0);
        }
    }

    @Test
    public void tEST9FEED() {
        driver.get(BASE_URL);
        driver.manage().window().setSize(new Dimension(1440, 809));
        driver.findElement(By.linkText("Feed-A-Cat")).click();
        driver.findElement(By.id("catnips")).click();
        driver.findElement(By.id("catnips")).sendKeys("6");
        // press the feed button 
        driver.findElement(By.cssSelector(".btn")).click();
        assertThat(driver.findElement(By.id("feedResult")).getText(), is("Nom, nom, nom."));
    }

    @Test
    public void tEST10GREETACAT() {
        driver.get(BASE_URL);
        driver.manage().window().setSize(new Dimension(1440, 809));
        driver.findElement(By.linkText("Greet-A-Cat")).click();
        assertThat(driver.findElement(By.xpath("//h4[contains(.,'Meow!Meow!Meow!')]")).getText(), is("Meow!Meow!Meow!"));
    }

    @Test
    public void tEST11GREETACATWITHNAME() {
        driver.get(BASE_URL);
        driver.manage().window().setSize(new Dimension(1440, 809));
        driver.get(BASE_URL + "greet-a-cat/Jennyanydots");
        assertThat(driver.findElement(By.xpath("//h4[contains(.,'Meow! from Jennyanydots.')]")).getText(), is("Meow! from Jennyanydots."));
    }
}