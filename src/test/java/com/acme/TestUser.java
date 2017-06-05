package com.acme;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestUser {
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
	public void SignInValid() {
		
		driver.get("http://localhost:8080/");
		
		driver.findElement(By.linkText("Register")).click();
		driver.findElement(By.id("email")).sendKeys("garbarge@garbage.com");
		driver.findElement(By.id("password")).sendKeys("testpass");
		driver.findElement(By.id("confirmPassword")).sendKeys("testpass");
		driver.findElement(By.cssSelector(".btn")).click();
	}
	
	// Attempt to sign in with valid info and succeed
	@Test
	public void SignInValid() {
		
	}
	
	// Click the sign out button, ensure no longer signed in
	@Test
	public void SignOut() {
		
	}
	
	// Attempt to sign in with wrong info, should get error message
	@Test
	public void SignInInvalid() {
		
	}
	
	// After a failed sign in, click on forgot password and fill form check for success message
	@Test
	public void ResetPasswordValid() {
		
	}
	
	// Attempt a password reset with invalid email address and check for error message
	@Test
	public void ResetPasswordInvalid() {
		
	}
	
	// Change first/last/email on record and check for success message
	@Test
	public void AccountSettingsValid() {
		// Need to log user back in first!
		
	}
	
	// Change first/last/email to invalid data and check for fail message
	@Test
	public void AccountSettingsInvalid() {
		
	}
	
	// Change password and check for success
	@Test
	public void PasswordChangeValid() {
		
	}
	
	// Enter mismatching passwords and check error
	@Test
	public void PasswordChangeInvalid() {
		
	}
	
	// AcmePass app should load list of saved passwords
	@Test
	public void VisitAcmePass() {
		
	}
	
	// Create a new AcmePass entry for a site and ensure worked
	@Test
	public void CreateNewPassValid() {
		
	}
	
	// Forget to enter a site and check for failure
	@Test
	public void CreateNewPassInvalid() {
		
	}
	
	// Look at a stored password, ensure its displayed
	@Test
	public void ViewSavedPassword() {
		
	}
	
	// Update a stored password or site name, ensure success
	@Test
	public void EditSavedPasswordValid() {
		
	}
	
	// Enter invalid info while editing saved password, ensure fail
	@Test
	public void EditSavedPasswordInvalid() {
		
	}

	// Generate a random password and ensure output matches paramters given
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