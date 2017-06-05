package com.acme;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestExample {
	WebDriver driver;
	
	@Before
	public void setUp() throws Exception {
		// Uncomment appropriate driver for your system
		System.setProperty("webdriver.gecko.driver", "webdrivers/win64/geckodriver.exe"); // 64 Bit Windows
		//System.setProperty("webdriver.gecko.driver", "webdrivers/linux64/geckodriver"); // 64 Bit Linux
		
		// Set up the driver global to this class
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}

	@Test
	public void registerTest() {
		
		driver.get("http://localhost:8080/");
		
		driver.findElement(By.linkText("Register")).click();
		driver.findElement(By.id("email")).sendKeys("garbarge@garbage.com");
		driver.findElement(By.id("password")).sendKeys("testpass");
		driver.findElement(By.id("confirmPassword")).sendKeys("testpass");
		driver.findElement(By.cssSelector(".btn")).click();
	}
	
	@After
	public void tearDown() throws Exception {
		// Once the tests are done, close down the webdriver
		//driver.close();
	}

}