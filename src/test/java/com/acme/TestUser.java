package com.acme;

import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestUser {
	WebDriver driver;
	
	@Before
	public void setUp() throws Exception {
		// Uncomment appropriate driver for your system
		//System.setProperty("webdriver.gecko.driver", "webdrivers/win64/geckodriver.exe"); // 64 Bit Windows
		System.setProperty("webdriver.gecko.driver", "webdrivers/linux64/geckodriver"); // 64 Bit Linux
		
		// Set up the driver global to this class
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
 
	// Attempt to sign in with valid info and succeed
	@Test
	public void SignInValid() {
		driver.get("http://localhost:8080/");
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).sendKeys("frank.paul@acme.com");
		driver.findElement(By.id("password")).sendKeys("starwars");
		driver.findElement(By.cssSelector(".btn")).click();
		assertTrue(driver.findElement(By.id("account-menu")).isDisplayed());
	}
	// AcmePass app should load list of saved passwords
	@Test
	public void VisitAcmePass() {
		//this.SignInValid();
		driver.findElement(By.linkText("ACMEPASS")).click();
		String TestTitle = driver.getTitle();
		assertEquals("ACMEPasses", TestTitle);
		//junit.org
	}
	
	// Create a new AcmePass entry for a site and ensure worked
	@Test
	public void CreateNewPassValid() {
		this.SignInValid();
		this.VisitAcmePass();
		driver.get("http://localhost:8080/#/acme-pass/new");
		driver.findElement(By.id("field_site")).sendKeys("testSite.com");
		driver.findElement(By.id("field_login")).sendKeys("testlogin");
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		driver.findElement(By.cssSelector("button[type^=submit]")).click();

	}
	
	// Forget to enter a site and check for failure
	@Test
	public void CreateNewPassInvalid() {
		this.SignInValid();
		this.VisitAcmePass();
		driver.get("http://localhost:8080/#/acme-pass/new");
		driver.findElement(By.id("field_login")).sendKeys("testlogin");
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
	}
	
	// Look at a stored password, ensure its displayed
	@Test
	public void ViewSavedPassword() {
		this.VisitAcmePass();
		driver.findElement(By.xpath("//tbody/tr[1]"));
		
	}
	
	// Update a stored password or site name, ensure success
	@Test
	public void EditSavedPasswordValid() {
		this.SignInValid();
		this.VisitAcmePass();
		driver.get("http://localhost:8080/#/acme-pass/1/edit");
		driver.findElement(By.id("field_site")).clear();
		driver.findElement(By.id("field_site")).sendKeys("modify.com");
		driver.findElement(By.id("field_login")).clear();
		driver.findElement(By.id("field_login")).sendKeys("testedit");
		driver.findElement(By.id("field_password")).clear();
		driver.findElement(By.id("field_password")).sendKeys("editpass");
		driver.findElement(By.cssSelector("button[type^=submit]")).click();
	}
	
	// Enter invalid info while editing saved password, ensure fail
	@Test
	public void EditSavedPasswordInvalid() {
		
	}

	// Generate a random password and ensure output matches parameters given
	@Test
	public void UsePasswordGenerator() {
		
	}
	
	// Delete saved password and ensure it is removed
	@Test
	public void DeleteSavedPassword() {
		
	}
	
	
	@After
	public void tearDown() throws Exception {
		// Once the tests are done, close down the webdriver
		driver.close();
	}

}