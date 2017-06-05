package com.acme;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestSiteVisitor {
	WebDriver driver;
	
	@Before
	public void setUp() throws Exception {
		// Uncomment appropriate driver for your system
		//System.setProperty("webdriver.gecko.driver", "webdrivers/win64/geckodriver.exe"); // 64 Bit Windows
		System.setProperty("webdriver.gecko.driver", "webdrivers/linux64/geckodriver"); // 64 Bit Linux
		
		// Set up the driver global to this class
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	}
	
	@After
	public void tearDown() throws Exception {
		// Once the tests are done, close down the webdriver
		driver.close();
	}

	//@Test
	// This test ensures that a site visitor can create an ACMEPass
//	public void registerTest() {
//		driver.get("http://localhost:8080/");
//		driver.findElement(By.linkText("Register")).click();
//		driver.findElement(By.id("email")).sendKeys("garbarge@garbage.com");
//		driver.findElement(By.id("password")).sendKeys("testpass");
//		driver.findElement(By.id("confirmPassword")).sendKeys("testpass");
//		driver.findElement(By.cssSelector(".btn")).click();
//		driver.findElement(By.linkText("ACMEPass")).isDisplayed();
//	}
	@Test
	//This test ensures that a site visitor cannot create an existing ACMEPass user
	public void registerExistingTest() {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("Register")).click();
		driver.findElement(By.id("email")).sendKeys("alice.sandhu@acme.com");
		driver.findElement(By.id("password")).sendKeys("princess");
		driver.findElement(By.id("confirmPassword")).sendKeys("princess");
		driver.findElement(By.cssSelector(".btn")).click();
		driver.findElement(By.cssSelector("strong")).isDisplayed();
	}
	@Test
	//This test ensures that a site visitor cannot create an ACMEPass user without all the information
	public void registerFailedTest() {
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("Register")).click();
		//Test only the both blank doesn't register user
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.cssSelector(".btn")).click();
		driver.findElement(By.cssSelector("strong")).isDisplayed();
		//Test only the username doesn't register user
		driver.findElement(By.id("email")).sendKeys("test@test.com");
		driver.findElement(By.cssSelector(".btn")).click();
		driver.findElement(By.cssSelector("strong")).isDisplayed();
		//Test only the password doesn't register user
		driver.findElement(By.id("email")).clear();
		driver.findElement(By.id("password")).sendKeys("princess");
		driver.findElement(By.cssSelector(".btn")).click();
		//Test only the password doesn't register user
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("confirmPassword")).sendKeys("testpass");
		driver.findElement(By.cssSelector(".btn")).click();
		driver.findElement(By.cssSelector("strong")).isDisplayed();
	}
	@Test
	// This test ensures that a site visitor can log into ACMEPass
	public void loginTest(){
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("Sign in")).click();
		driver.findElement(By.id("username")).sendKeys("alice.sandhu@acme.com");
		driver.findElement(By.id("password")).sendKeys("princess");
		driver.findElement(By.cssSelector(".btn")).click();
		driver.findElement(By.linkText("ACMEPass")).isDisplayed();
	}
	@Test
	// This test ensures that a site visitor cannot log into ACMEPass with missing information
	public void failedLoginTest(){
		driver.get("http://localhost:8080/");
		driver.findElement(By.linkText("Sign in")).click();
		//test only the both blank doesn't log the user in
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.cssSelector(".btn")).click();
		driver.findElement(By.cssSelector("strong")).isDisplayed();
		//test only username doesn't log the user in
		driver.findElement(By.id("username")).sendKeys("alice.sandhu@acme.com");
		driver.findElement(By.cssSelector(".btn")).click();
		driver.findElement(By.cssSelector("strong")).isDisplayed();
		//test only the password doesn't log the user in
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("password")).sendKeys("princess");
		driver.findElement(By.cssSelector(".btn")).click();
		driver.findElement(By.cssSelector("strong")).isDisplayed();
			
	}

}
