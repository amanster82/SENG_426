package com.acme;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
		this.VisitAcmePass();
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
	
	// Forget to enter a site and check for failure
	@Test
	public void CreateNewPassInvalid() {
		this.VisitAcmePass();
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); //create new
		driver.findElement(By.id("field_login")).sendKeys("testlogin");
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel create
		assertFalse (driver.findElement(By.xpath("//tbody/tr[1]")).isDisplayed());
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); //create new
		driver.findElement(By.id("field_site")).sendKeys("something.com");
		driver.findElement(By.id("field_login")).clear();
		driver.findElement(By.id("field_password")).sendKeys("testpass");
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel create
		driver.findElement(By.cssSelector("button.btn.btn-primary")).click(); //create new
		driver.findElement(By.id("field_site")).sendKeys("something.com");
		driver.findElement(By.id("field_login")).sendKeys("testpass");
		driver.findElement(By.id("field_password")).clear();
		driver.findElement(By.cssSelector("button[disabled^=disabled]"));
		driver.findElement(By.cssSelector("button.btn.btn-default")).click(); //cancel create
		
	}
	
	// Look at a stored password, ensure its displayed
	@Test
	public void ToggleViewSavedPassword() {
		this.CreateNewPassValid();
		String pass = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("", pass);
		//need to test on successfull code
		driver.findElement(By.className("glyphicon glyphicon-eye-open")).click();
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
		
	}
	
	// Update a stored password or site name, ensure success
	@Test
	public void EditSavedPasswordValid() {
		this.VisitAcmePass();
		driver.get("http://localhost:8080/#/acme-pass/1/edit");
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
		//need to test on successfull code
		driver.findElement(By.className("glyphicon glyphicon-eye-open")).click();
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
		//need to test on successfull code
		driver.findElement(By.className("glyphicon glyphicon-eye-open")).click();
		String passview = driver.findElement(By.xpath("//tbody/tr[1]/td[4]")).getText();
		assertEquals ("testpass", passview);
	}

	// Generate a random password and ensure output matches parameters given
	@Test
	public void UsePasswordGenerator() {
		
	}
	
	// Delete saved password and ensure it is removed
	@Test
	public void DeleteSavedPassword() {
		
	}
	
	@Test
	public void Sorting() {
		//Boolean isPresent = driver.findElements(By.)
	}
	
	@Test
	public void Pagination() {
		//ArrayList<WebElement> lst = new ArrayList<WebElement>(); 
		for (int i=0; i<21; i++){
			this.CreateNewPassValid();
		}
		//needs to count rows == 20
		//assertFalse(iselementpresent(driver.))
		List<WebElement> lst = driver.findElements(By.xpath(".//tbody/tr"));
		driver.findElement(By.linkText("»")).click(); //go to next page
		//needs to count rows ==1
		driver.findElement(By.linkText("«")).click(); //go to first page
		//needs to count rows again == 20
		
	}
	
	
	@After
	public void tearDown() throws Exception {
		// Once the tests are done, close down the webdriver
		driver.close();
	}

}