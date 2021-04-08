package com.qa.choonz.selenium.pages;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateUser {
	
public static final String URL = "C:\\Users\\nikos\\eclipse-workspace\\Choonz Project\\ChoonzProject\\src\\main\\resources\\static\\signup.html";

	private static WebDriver driver;
	
	@BeforeAll
	public static void init() {
		System.out.println("ANything");
		System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
		ChromeOptions cOptions = new ChromeOptions();
		cOptions.setHeadless(false);

		cOptions.setCapability("profile.default_content_setting_values.cookies", 2);
		cOptions.setCapability("network.cookie.cookieBehavior", 2);
		cOptions.setCapability("profile.block_third_party_cookies", true);
		driver = new ChromeDriver(cOptions);

	}

	@BeforeEach
	public void setup() {
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.get(CreateUser.URL);
	}

	@AfterAll
	public static void tearDown() {
		driver.quit();
	}
	
	
	@Test
	public void newUser() throws InterruptedException {
		Actions action = new Actions(driver);
		WebElement userNameContainer = driver.findElement(By.cssSelector("#username"));
		action.moveToElement(userNameContainer).perform();
		action.click();
		Thread.sleep(2000);
		
		userNameContainer.sendKeys("Nikpap");
		
		
		WebElement passwordContainer = driver.findElement(By.cssSelector("#password"));
		action.moveToElement(passwordContainer).perform();
		action.click();
		Thread.sleep(2000);
		passwordContainer.sendKeys("1234");
		
		
		WebElement confirmPasswordContainer = driver.findElement(By.cssSelector("#confpassword"));
		action.moveToElement(confirmPasswordContainer).perform();
		action.click();
		Thread.sleep(2000);
		
		confirmPasswordContainer.sendKeys("1234");
		
		
		WebElement createAccountButton = driver.findElement(By.cssSelector("#submit"));
		action.moveToElement(createAccountButton).perform();
		action.click();
		Thread.sleep(2000);
		
		new WebDriverWait(driver, 5).until(ExpectedConditions
                .presenceOfElementLocated(By.xpath("/html/body/div[1]/div/div[1]/button[1]")));
		
		
		System.out.println(driver.getCurrentUrl());
		if (driver.getCurrentUrl().equals("http://localhost:8082/home")) {
			
		} else {
			fail();
		}
		
		
	}
	

}

