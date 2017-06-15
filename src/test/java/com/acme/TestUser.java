package com.acme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestUser {
	WebDriver driver;
	
	@Before
	public void setUp() throws Exception {
		// Uncomment appropriate driver for your system
		//System.setProperty("webdriver.gecko.driver", "webdrivers/win64/geckodriver.exe"); // 64 Bit Windows
		System.setProperty("webdriver.gecko.driver", "webdrivers/linux64/geckodriver"); // 64 Bit Linux
		
		// Set up the driver global to this class
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(3, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		this.SignInValid();
	}
 
	// Attempt to sign in with valid info and succeed
	@Test
	public void SignInValid() {
		driver.get("http://localhost:8080/");
		
		// Fill out login form with Franks info
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).sendKeys("frank.paul@acme.com");
		driver.findElement(By.id("password")).sendKeys("starwars");
		driver.findElement(By.cssSelector(".btn")).click();
		
		// Verify we are logged in by checking for menu item
		assertTrue(driver.findElement(By.id("account-menu")).isDisplayed());
	}
	
	// AcmePass app should load list of saved passwords
	@Test
	public void VisitAcmePass() {
		driver.get("http://localhost:8080/#/acme-pass");
		
		// Verify page is correct by checking page title
		String TestTitle = driver.getTitle();
		assertEquals("ACMEPasses", TestTitle);
	}
	
	// Create a new AcmePass entry for a site and ensure worked
	@Test
	public void CreateNewPassValid() {
		driver.get("http://localhost:8080/#/acme-pass/new");
		
		// Fill out form with a sample password
		driver.findElement(By.id("field_site")).sendKeys("testSite.com");
		driver.findElement(By.id("field_login")).sendKeys("testlogin");
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		
		// Click save
		driver.findElement(By.cssSelector("button[type^=submit]")).click();
		
		// Verify that the values we entered are now displayed
		String site = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
		assertEquals("testSite.com", site);
		String login = driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText();
		assertEquals("testlogin", login);
		String pass = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals("", pass);
	}
	
	// Helper method to fill out the new password form if visible
	public void GeneratePass(String pass) {
		if((isElementPresent(By.id("modal-dialog modal-lg"))) == false) {
			WebDriverWait wait = new WebDriverWait(driver, 2);
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			
			// Wait until form displayed then fill out info and submit
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("field_site")));
			driver.findElement(By.id("field_site")).sendKeys(pass);
			driver.findElement(By.id("field_login")).sendKeys(pass);
			driver.findElement(By.id("field_password")).sendKeys(pass);
			driver.findElement(By.cssSelector("button[type^=submit]")).click();
		}
	}
	
	// Helper method to verify that the right modal is present
	public boolean isElementPresent(By locatorKey) {
	    try {
	        driver.findElement(locatorKey);
	        System.out.println("the modal is present.");
	        return true;
	    } catch (org.openqa.selenium.NoSuchElementException e) {
	    	System.out.println("the modal is closed");
	    	return false;
	    }
	}
	
	// Forget to enter a site and check for failure
	@Test
	public void CreateNewPassInvalid() {
		this.VisitAcmePass();
		
		// Click on Create new
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		driver.findElement(By.id("field_login")).sendKeys("testlogin");
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		assertTrue(driver.findElement(By.cssSelector("button[disabled^=disabled]")).isDisplayed()); //button disabled
		
		// Click cancel, form should close
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); 
		
		// Reopen form and fill out with invalid info
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); 
		driver.findElement(By.id("field_site")).sendKeys("something.com");
		driver.findElement(By.id("field_login")).clear();
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		
		// Check that we are unable to submit
		assertTrue(driver.findElement(By.cssSelector("button[disabled^=disabled]")).isDisplayed());
		
		// Cancel and reopen form and enter more invalid info of different kind
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); 
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); 
		driver.findElement(By.id("field_site")).sendKeys("something.com");
		driver.findElement(By.id("field_login")).sendKeys("testpass");
		driver.findElement(By.id("field_password")).clear();
		
		// Ensure we still cannot submit
		assertTrue(driver.findElement(By.cssSelector("button[disabled^=disabled]")).isDisplayed());

		// Close the modal
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); 
	}
	
	// Look at a stored password, ensure its displayed
	@Test
	public void ToggleViewSavedPassword() {
		this.CreateNewPassValid();
		
		// Make sure password is not displayed
		String pass = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("", pass);
		
		// Click on the eye icon
		driver.findElement(By.className("glyphicon-eye-open")).click();
		
		// Now verify that the password is displayed
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
	}
	
	// Update a stored password or site name, ensure success
	@Test
	public void EditSavedPasswordValid() {
		// Add a new password
		this.CreateNewPassValid();
		
		// Edit the recently added password and submit
		driver.get("http://localhost:8080/#/acme-pass/1/edit");
		driver.findElement(By.className("btn-info")).click();
		driver.findElement(By.id("field_site")).clear();
		driver.findElement(By.id("field_site")).sendKeys("modify.com");
		driver.findElement(By.id("field_login")).clear();
		driver.findElement(By.id("field_login")).sendKeys("testedit");
		driver.findElement(By.id("field_password")).clear();
		driver.findElement(By.id("field_password")).sendKeys("editpass");
		driver.findElement(By.cssSelector("button[type^=submit]")).click();
		
		// Test to make sure the new values exist
		String site = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
		assertEquals ("modify.com", site);
		String login = driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText();
		assertEquals ("testedit", login);
		String pass = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("", pass);
		
		// Show the password, and make sure its up to date
		driver.findElement(By.className("glyphicon-eye-open")).click();
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
	}
	
	// Enter invalid info while editing saved password, ensure fail
	@Test
	public void EditSavedPasswordInvalid() {
		this.CreateNewPassValid();
		
		// Edit the password, but enter invalid info so can only cancel halfway
		driver.findElement(By.xpath("//td[7]/div/button")).click(); 
		driver.findElement(By.id("field_site")).clear();
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
		driver.findElement(By.cssSelector("button.btn.btn-default")).click();
		
		// Verify the info is still the same
		String site = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
		assertEquals ("testSite.com", site);
		
		// Edit the password but enter invalid data so only can cancel edit again
		driver.findElement(By.xpath("//td[7]/div/button")).click(); 
		driver.findElement(By.id("field_login")).clear();
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); 
		
		// Verify nothing changed
		String login = driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText();
		assertEquals ("testlogin", login);
		
		// Edit once more, enter invalid data once again
		driver.findElement(By.xpath("//td[7]/div/button")).click(); //edit password
		driver.findElement(By.id("field_password")).clear();
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel edit
		driver.findElement(By.className("glyphicon-eye-open")).click();
		
		// Ensure value is unchanged
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
	}

	// Generate a random password and ensure output matches parameters given
	@Test
	public void UsePasswordGenerator() {
		this.VisitAcmePass();
		
		// Open up the password generator
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); 
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); 
		
		// Select only lower case characters of length 8
		driver.findElement(By.id("field_upper")).click();
		driver.findElement(By.id("field_digits")).click();
		driver.findElement(By.id("field_special")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		
		// Check generated password and see if it fits options by regex check
		String genlowpass = driver.findElement(By.id("field_password")).getAttribute("value");
		assertTrue (genlowpass.matches("^[a-z]+$"));
		assertTrue (genlowpass.length() == 8);
		
		// Select only upper case characters of length 8
		driver.findElement(By.id("field_lower")).click();
		driver.findElement(By.id("field_upper")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		
		// Check generated password and see if it fits options by regex check
		String genuppass = driver.findElement(By.id("field_password")).getAttribute("value");
		assertTrue (genuppass.matches("^[A-Z]+$"));
		assertTrue (genuppass.length() == 8);
		
		// Select only special characters of length 8
		String splChrs = "!@#$%-_";
		driver.findElement(By.id("field_upper")).click();
		driver.findElement(By.id("field_special")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		
		// Check generated password and see if it fits options by regex check
		String genspecpass = driver.findElement(By.id("field_password")).getAttribute("value");
		assertTrue (genspecpass.matches("^[" + splChrs + "]+$"));
		assertTrue (genspecpass.length() == 8);
		
		// Select only digits characters of length 8
		driver.findElement(By.id("field_digits")).click();
		driver.findElement(By.id("field_special")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();

		// Check generated password and see if it fits options by regex check
		String gendigpass = driver.findElement(By.id("field_password")).getAttribute("value");
		assertTrue (gendigpass.matches("^\\d+$"));
		assertTrue (gendigpass.length() == 8);
		
		// Select all characters types of length 30
		driver.findElement(By.id("field_lower")).click();
		driver.findElement(By.id("field_upper")).click();
		driver.findElement(By.id("field_special")).click();
		driver.findElement(By.id("field_length")).clear();
		driver.findElement(By.id("field_length")).sendKeys("30");
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		
		// Check generated password and see if it fits options by regex check
		String genpass = driver.findElement(By.id("field_password")).getAttribute("value");
		assertTrue (gendigpass.matches("^\\d+$"));
		boolean total = genpass.matches("^[A-Za-z\\d!@#$%-_]+$");
		boolean lower = genpass.matches(".*[a-z]+.*");
		boolean upper = genpass.matches(".*[A-Z]+.*");
		boolean digits = genpass.matches(".*\\d+.*");
		boolean special = genpass.matches(".*["+ splChrs +"]+.*");
		assertTrue (genpass.length() == 30);
		assertTrue(lower&&upper&&digits&&special&&total);
		
		// Select all characters types of length 30 no repeats
		driver.findElement(By.id("field_repetition")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		genpass = driver.findElement(By.id("field_password")).getAttribute("value");
		assertTrue (gendigpass.matches("^\\d+$"));
		total = genpass.matches("^[A-Za-z\\d!@#$%-_]+$");
		lower = genpass.matches(".*[a-z]+.*");
		upper = genpass.matches(".*[A-Z]+.*");
		digits = genpass.matches(".*\\d+.*");
		special = genpass.matches(".*["+ splChrs +"].*");
		assertTrue (genpass.length() == 30);
		
		// Verify that there are no repeated characters
		boolean noRepeats = true;
		Map<Character, String> map1 = new HashMap<Character, String>();
		for(int index = 0; index < genpass.length(); index++)
		{
			noRepeats &= !map1.containsKey(genpass.charAt(index));
			map1.put(genpass.charAt(index), "1");
		}
		assertTrue(lower&&upper&&digits&&special&&total&&noRepeats);
		
		// Test with no rules selected the generate password button is not enabled
		driver.findElement(By.id("field_lower")).click();
		driver.findElement(By.id("field_upper")).click();
		driver.findElement(By.id("field_digits")).click();
		driver.findElement(By.id("field_special")).click();
		assertTrue(driver.findElement(By.cssSelector("button[disabled^=disabled]")).isDisplayed()); //button disabled
		
	}
	
	// Delete saved password and ensure it is removed
	@Test
	public void DeleteSavedPassword() {
		this.VisitAcmePass();
		driver.get("http://localhost:8080/#/acme-pass/new");
		
		// Create a new AcmePass
		driver.findElement(By.id("field_site")).sendKeys("testSite.com");
		driver.findElement(By.id("field_login")).sendKeys("testlogin");
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		driver.findElement(By.cssSelector("button[type^=submit]")).click(); 
		
		// Order by date to find the one we just added
		driver.get("http://localhost:8080/#/acme-pass?sort=createdDate,desc");
		List<WebElement> elements = driver.findElements(By.xpath("//tbody/tr"));
		Collection<String> startElements = new ArrayList<String>();
		Iterator<WebElement> iter = elements.iterator();				
		while (iter.hasNext()){
			WebElement item = iter.next();
			String label = item.getText();
			startElements.add(label);
		}
		
		// Verify the correct site and login name are there
		WebElement passwordRow = driver.findElement(By.xpath("//tbody/tr[1]"));
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.cssSelector("button.btn.btn-default")).click();
		String site = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
		assertEquals ("testSite.com", site);
		String login = driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText();
		assertEquals ("testlogin", login);
		
		// Verify the password maches
		driver.findElement(By.className("glyphicon-eye-open")).click();
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
		elements = driver.findElements(By.xpath("//tbody/tr"));
		
		Collection<String> midElements = new ArrayList<String>();
		Iterator<WebElement> iter2 = elements.iterator();				
		while (iter2.hasNext()){
			WebElement item = iter2.next();
			String label = item.getText();
			midElements.add(label);
		}
		
		// Check to see if an element was removed, and the correct one
		assertTrue("Cancel button doesnt work", startElements.equals(midElements));
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.cssSelector("button.btn.btn-danger[type^=submit]")).click();
		elements = driver.findElements(By.xpath("//tbody/tr"));
		
		Collection<String> lastElements = new ArrayList<String>();
		Iterator<WebElement> iter3 = elements.iterator();
		while (iter3.hasNext()){
			WebElement item = iter3.next();
			String label = item.getText();
			lastElements.add(label);
		}
		startElements.removeAll(lastElements);
		
		if(!startElements.isEmpty()){
			assertTrue("Incorrect password removed", startElements.contains(passwordRow));
		} else {
			assertTrue("Password wasn't removed", false);
		}		
	}
	
	// Returns a list of titles of AcmePass elements based upon a specified column index
	public List<String> elementsToArray(List<WebElement> webElements, int column){
		List<String> unsortedElements = new ArrayList<String>();
		Iterator<WebElement> iter = webElements.iterator();				
		while (iter.hasNext()){
			WebElement item = iter.next();
			String label = item.findElement(By.xpath("td["+column+"]")).getText();
			unsortedElements.add(label);
		}
		return unsortedElements;
	}
	
	@Test
	public void Sorting() {
		driver.get("http://localhost:8080/#/acme-pass");
		
		// Create a list of the tr elements and check if they are sorted
		List<WebElement> elements = driver.findElements(By.xpath("//tbody/tr"));
		List<String> unsortedElements = elementsToArray(elements, 1);
		List<String> sortedElements = new ArrayList<String>(unsortedElements);
		Collections.sort(sortedElements);
		assertTrue("List sorted incorrectly", sortedElements.equals(unsortedElements));
		
		// Reverse the sorting
		driver.findElement(By.xpath("//thead/tr/th[1]")).click();
		
		// Check to see if it is sorted by reverse now
		elements = driver.findElements(By.xpath("//tbody/tr"));
		unsortedElements = elementsToArray(elements, 1);
		sortedElements = new ArrayList<String>(unsortedElements);
		Collections.sort(sortedElements);
		Collections.reverse(sortedElements);
		assertTrue("Reverse List sorted incorrectly", sortedElements.equals(unsortedElements));
		
		// There are 4 more fields to sort upon, test each of them
		for(int x = 2; x <= 6; x++){
			
			// Click once to sort initially
			driver.findElement(By.xpath("//thead/tr/th["+x+"]")).click();
			
			// Verify it is sorted upon that column
			elements = driver.findElements(By.xpath("//tbody/tr"));
			unsortedElements = elementsToArray(elements, x);
			sortedElements = new ArrayList<String>(unsortedElements);
			Collections.sort(sortedElements);
			assertTrue("List sorted incorrectly: " + x, sortedElements.equals(unsortedElements));
			
			// Click again to reverse the sort
			driver.findElement(By.xpath("//thead/tr/th["+x+"]")).click();
			
			// Check to make sure it is in reverse order
			elements = driver.findElements(By.xpath("//tbody/tr"));
			unsortedElements = elementsToArray(elements, x);
			sortedElements = new ArrayList<String>(unsortedElements);
			Collections.sort(sortedElements);
			Collections.reverse(sortedElements);
			assertTrue("Reverse List sorted incorrectly: " + x, sortedElements.equals(unsortedElements));
		}
	}
	
	@Test
	public void Pagination() {
		// Start by opening the site
		this.VisitAcmePass();
		
		// Create a bunch of passwords so that we should have pages
		for (int i=0; i<21; i++){
			this.GeneratePass("password" + Integer.toString(i)); // Generate Passwords.
		}
	
		// Verify there are now multiple pages
		driver.findElement(By.xpath("//tbody/tr[20]")).isDisplayed();
		driver.findElement(By.linkText("»")).click(); //go to next page
		
		// Needs to count rows >= 1
		driver.findElement(By.xpath("//tbody/tr[1]")).isDisplayed();
		driver.findElement(By.linkText("«")).click(); //go to first page
		
		// Needs to count rows again == 20
		driver.findElement(By.xpath("//tbody/tr[20]")).isDisplayed();
		
	}

	@After
	public void tearDown() throws Exception {
		// Once the tests are done close down the webdriver
		driver.close();
	}
}