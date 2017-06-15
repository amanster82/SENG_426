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
		driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(1, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(1, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		this.SignInValid();
	}
 
	// Attempt to sign in with valid info and succeed
	
	
	public void SignInValid() {
		driver.get("http://localhost:8080/");
		driver.findElement(By.id("login")).click();
		driver.findElement(By.id("username")).sendKeys("frank.paul@acme.com");
		driver.findElement(By.id("password")).sendKeys("starwars");
		driver.findElement(By.cssSelector(".btn")).click();
		assertTrue(driver.findElement(By.id("account-menu")).isDisplayed());
	}
	// AcmePass app should load list of saved passwords
	
	
	public void VisitAcmePass() {
		driver.get("http://localhost:8080/#/acme-pass");
		String TestTitle = driver.getTitle();
		assertEquals("ACMEPasses", TestTitle);
		//junit.org
	}
	
	// Create a new AcmePass entry for a site and ensure worked
	@Test
	public void CreateNewPassValid() {
		//this.VisitAcmePass();
		driver.get("http://localhost:8080/#/acme-pass/new");
		driver.findElement(By.id("field_site")).sendKeys("testSite.com");
		driver.findElement(By.id("field_login")).sendKeys("testlogin");
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		driver.findElement(By.cssSelector("button[type^=submit]")).click(); //save
		String site = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
		assertEquals ("testSite.com", site);
		String login = driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText();
		assertEquals ("testlogin", login);
		String pass = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("", pass);
	}
	
	public void GeneratePass(String pass) {
		
		if(	(isElementPresent(By.id("modal-dialog modal-lg"))) == false) {
			WebDriverWait wait = new WebDriverWait(driver, 5);
			driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("field_site")));
			driver.findElement(By.id("field_site")).sendKeys(pass);
			driver.findElement(By.id("field_login")).sendKeys(pass);
			driver.findElement(By.id("field_password")).sendKeys(pass);
			driver.findElement(By.cssSelector("button[type^=submit]")).click();
		}
	}
	
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

/*	public boolean isElementVisible(String cssLocator){
	    return driver.findElement(By.cssSelector(cssLocator)).isDisplayed();
	}
*/
	
	
	// Forget to enter a site and check for failure
	@Test
	public void CreateNewPassInvalid() {
		this.VisitAcmePass();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); //create new
		driver.findElement(By.id("field_login")).sendKeys("testlogin");
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		assertTrue(driver.findElement(By.cssSelector("button[disabled^=disabled]")).isDisplayed()); //button disabled
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel create
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); //create new
		driver.findElement(By.id("field_site")).sendKeys("something.com");
		driver.findElement(By.id("field_login")).clear();
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		assertTrue(driver.findElement(By.cssSelector("button[disabled^=disabled]")).isDisplayed());//button disabled
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel create
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); //create new
		driver.findElement(By.id("field_site")).sendKeys("something.com");
		driver.findElement(By.id("field_login")).sendKeys("testpass");
		driver.findElement(By.id("field_password")).clear();
		assertTrue(driver.findElement(By.cssSelector("button[disabled^=disabled]")).isDisplayed());//button disabled
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel create
		
	}
	
	// Look at a stored password, ensure its displayed
	@Test
	public void ToggleViewSavedPassword() {
		this.CreateNewPassValid();
		String pass = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("", pass);
		driver.findElement(By.className("glyphicon-eye-open")).click();
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
		
	}
	
	// Update a stored password or site name, ensure success
	@Test
	public void EditSavedPasswordValid() {
		this.CreateNewPassValid();
		driver.get("http://localhost:8080/#/acme-pass/1/edit");
		driver.findElement(By.className("btn-info")).click();
		driver.findElement(By.id("field_site")).clear();
		driver.findElement(By.id("field_site")).sendKeys("modify.com");
		driver.findElement(By.id("field_login")).clear();
		driver.findElement(By.id("field_login")).sendKeys("testedit");
		driver.findElement(By.id("field_password")).clear();
		driver.findElement(By.id("field_password")).sendKeys("editpass");
		driver.findElement(By.cssSelector("button[type^=submit]")).click();
		String site = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
		assertEquals ("modify.com", site);
		String login = driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText();
		assertEquals ("testedit", login);
		String pass = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("", pass);
		driver.findElement(By.className("glyphicon-eye-open")).click();
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
	}
	
	// Enter invalid info while editing saved password, ensure fail
	@Test
	public void EditSavedPasswordInvalid() {
		this.CreateNewPassValid();
		driver.findElement(By.xpath("//td[7]/div/button")).click(); //edit password
		driver.findElement(By.id("field_site")).clear();
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel edit
		String site = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
		assertEquals ("testSite.com", site);
		driver.findElement(By.xpath("//td[7]/div/button")).click(); //edit password
		driver.findElement(By.id("field_login")).clear();
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel edit
		String login = driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText();
		assertEquals ("testlogin", login);
		driver.findElement(By.xpath("//td[7]/div/button")).click(); //edit password
		driver.findElement(By.id("field_password")).clear();
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel edit
		driver.findElement(By.className("glyphicon-eye-open")).click();
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
	}

	// Generate a random password and ensure output matches parameters given
	@Test
	public void UsePasswordGenerator() {
		this.VisitAcmePass();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); //create new
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); //click generate password
		
		
		//select only lower case characters of length 8
		
		driver.findElement(By.id("field_upper")).click();
		driver.findElement(By.id("field_digits")).click();
		driver.findElement(By.id("field_special")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		String genlowpass = driver.findElement(By.id("field_password")).getAttribute("value");
		System.out.println("does it get here?" + genlowpass); 
		assertTrue (genlowpass.matches("^[a-z]+$"));
		assertTrue (genlowpass.length() == 8);
		
		//select only upper case characters of length 8
		 
		driver.findElement(By.id("field_lower")).click();
		driver.findElement(By.id("field_upper")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		String genuppass = driver.findElement(By.id("field_password")).getAttribute("value");
		System.out.println("does it get here?" + genuppass); 
		assertTrue (genuppass.matches("^[A-Z]+$"));
		assertTrue (genuppass.length() == 8);
		
		
		//select only special characters of length 8
		String splChrs = "!@#$%-_";
		driver.findElement(By.id("field_upper")).click();
		driver.findElement(By.id("field_special")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		String genspecpass = driver.findElement(By.id("field_password")).getAttribute("value");
		System.out.println("does it get here?" + genspecpass); 
		assertTrue (genspecpass.matches("^[" + splChrs + "]+$"));
		assertTrue (genspecpass.length() == 8);
		
		
		//select only digits characters of length 8
		driver.findElement(By.id("field_digits")).click();
		driver.findElement(By.id("field_special")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		String gendigpass = driver.findElement(By.id("field_password")).getAttribute("value");
		System.out.println("does it get here?" + gendigpass); 
		assertTrue (gendigpass.matches("^\\d+$"));
		assertTrue (gendigpass.length() == 8);
		
		
		//select all characters types of length 30
		driver.findElement(By.id("field_lower")).click();
		driver.findElement(By.id("field_upper")).click();
		driver.findElement(By.id("field_special")).click();
		driver.findElement(By.id("field_length")).clear();
		driver.findElement(By.id("field_length")).sendKeys("30");
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		String genpass = driver.findElement(By.id("field_password")).getAttribute("value");
		assertTrue (gendigpass.matches("^\\d+$"));
		boolean total = genpass.matches("^[A-Za-z\\d!@#$%-_]+$");
		boolean lower = genpass.matches(".*[a-z]+.*");
		boolean upper = genpass.matches(".*[A-Z]+.*");
		boolean digits = genpass.matches(".*\\d+.*");
		boolean special = genpass.matches(".*["+ splChrs +"]+.*");
		System.out.println("Booleans: " + total + " " + lower + " " + upper + " " + digits + " " + " " + special);
		System.out.println("does it get here?" + genpass); 
		assertTrue (genpass.length() == 30);
		assertTrue(lower&&upper&&digits&&special&&total);
		
		//select all characters types of length 30 no repeats
		driver.findElement(By.id("field_repetition")).click();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click();
		genpass = driver.findElement(By.id("field_password")).getAttribute("value");
		assertTrue (gendigpass.matches("^\\d+$"));
		total = genpass.matches("^[A-Za-z\\d!@#$%-_]+$");
		lower = genpass.matches(".*[a-z]+.*");
		upper = genpass.matches(".*[A-Z]+.*");
		digits = genpass.matches(".*\\d+.*");
		special = genpass.matches(".*["+ splChrs +"].*");
		System.out.println("does it get here?" + genpass); 
		assertTrue (genpass.length() == 30);
		
		boolean noRepeats = true;
		Map<Character, String> map1 = new HashMap<Character, String>();
		for(int index = 0; index < genpass.length(); index++)
		{
			noRepeats &= !map1.containsKey(genpass.charAt(index));
			map1.put(genpass.charAt(index), "1");
		}
		System.out.println("Booleans: " + total + " " + lower + " " + upper + " " + digits + " " + " " + special + " " + noRepeats);
		
		assertTrue(lower&&upper&&digits&&special&&total&&noRepeats);
		
		//test with no rules selected the generate password button is not enabled
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
		driver.findElement(By.id("field_site")).sendKeys("testSite.com");
		driver.findElement(By.id("field_login")).sendKeys("testlogin");
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		driver.findElement(By.cssSelector("button[type^=submit]")).click(); //save
		
		driver.get("http://localhost:8080/#/acme-pass?sort=createdDate,desc");
		List<WebElement> elements = driver.findElements(By.xpath("//tbody/tr"));
		Collection<String> startElements = new ArrayList<String>();
		
		Iterator<WebElement> iter = elements.iterator();				
		while (iter.hasNext())
		{
			WebElement item = iter.next();
			String label = item.getText();
			startElements.add(label);
		}
		
		WebElement passwordRow = driver.findElement(By.xpath("//tbody/tr[1]"));
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.cssSelector("button.btn.btn-default")).click();
		String site = driver.findElement(By.xpath("//tbody/tr[1]/td[2]")).getText();
		assertEquals ("testSite.com", site);
		String login = driver.findElement(By.xpath("//tbody/tr[1]/td[3]")).getText();
		assertEquals ("testlogin", login);
		driver.findElement(By.className("glyphicon-eye-open")).click();
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
		elements = driver.findElements(By.xpath("//tbody/tr"));
		
		Collection<String> midElements = new ArrayList<String>();
		
		Iterator<WebElement> iter2 = elements.iterator();				
		while (iter2.hasNext())
		{
			WebElement item = iter2.next();
			String label = item.getText();
			midElements.add(label);
		}
		
		assertTrue("Cancel button doesnt work", startElements.equals(midElements));
		driver.findElement(By.xpath("//button[2]")).click();
		driver.findElement(By.cssSelector("button.btn.btn-danger[type^=submit]")).click();
		elements = driver.findElements(By.xpath("//tbody/tr"));
		
		Collection<String> lastElements = new ArrayList<String>();
		Iterator<WebElement> iter3 = elements.iterator();
		while (iter3.hasNext())
		{
			WebElement item = iter3.next();
			String label = item.getText();
			lastElements.add(label);
		}
		
		startElements.removeAll(lastElements);
		
		if(!startElements.isEmpty())
		{
			assertTrue("Incorrect password removed", startElements.contains(passwordRow));
		}
		else
		{
			assertTrue("Password wasn't removed", false);
		}		
	}
	
	public List<String> elementsToArray(List<WebElement> webElements, int column){
		List<String> unsortedElements = new ArrayList<String>();
		
		Iterator<WebElement> iter = webElements.iterator();				
		while (iter.hasNext())
		{
			WebElement item = iter.next();
			String label = item.findElement(By.xpath("td["+column+"]")).getText();
			unsortedElements.add(label);
		}
		return unsortedElements;
	}
	
	@Test
	public void Sorting() {
		//Boolean isPresent = driver.findElements(By.)
		driver.get("http://localhost:8080/#/acme-pass");
		
		List<WebElement> elements = driver.findElements(By.xpath("//tbody/tr"));
		List<String> unsortedElements = elementsToArray(elements, 1);
		List<String> sortedElements = new ArrayList<String>(unsortedElements);
		Collections.sort(sortedElements);
		assertTrue("List sorted incorrectly", sortedElements.equals(unsortedElements));
		
		driver.findElement(By.xpath("//thead/tr/th[1]")).click();
		
		elements = driver.findElements(By.xpath("//tbody/tr"));
		unsortedElements = elementsToArray(elements, 1);
		sortedElements = new ArrayList<String>(unsortedElements);
		Collections.sort(sortedElements);
		Collections.reverse(sortedElements);
		assertTrue("Reverse List sorted incorrectly", sortedElements.equals(unsortedElements));
		
		for(int x = 2; x <= 6; x++)
		{
			driver.findElement(By.xpath("//thead/tr/th["+x+"]")).click();
			
			elements = driver.findElements(By.xpath("//tbody/tr"));
			unsortedElements = elementsToArray(elements, x);
			sortedElements = new ArrayList<String>(unsortedElements);
			Collections.sort(sortedElements);
			assertTrue("List sorted incorrectly: " + x, sortedElements.equals(unsortedElements));
			
			driver.findElement(By.xpath("//thead/tr/th["+x+"]")).click();
			
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
		
		this.VisitAcmePass();
		
		for (int i=0; i<21; i++){
			this.GeneratePass("password" + Integer.toString(i)); // Generate Passwords.
		}
	
		driver.findElement(By.xpath("//tbody/tr[20]")).isDisplayed();
		driver.findElement(By.linkText("»")).click(); //go to next page
		//needs to count rows ==1
		driver.findElement(By.xpath("//tbody/tr[1]")).isDisplayed();
		driver.findElement(By.linkText("«")).click(); //go to first page
		//needs to count rows again == 20
		driver.findElement(By.xpath("//tbody/tr[20]")).isDisplayed();
		
	}
	

	@After
	public void tearDown() throws Exception {
		// Once the tests are done close down the webdriver
		driver.close();
	}

}