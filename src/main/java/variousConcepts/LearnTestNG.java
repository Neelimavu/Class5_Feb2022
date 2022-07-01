package variousConcepts;

import static org.testng.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class LearnTestNG {
	
	WebDriver driver;
	String browser;
	String url;
	
	//Element List
	By USER_NAME_FIELD = By.xpath("//input[@id='username']");
	By PASSWORD_FIELD = By.xpath("//input[@id='password']");
	By LOGIN_BUTTON_FIELD = By.xpath("//button[@name='login' and @type='submit']");
	By DASHBOARD_HEADER_FIELD = By.xpath("//*[@id=\"page-wrapper\"]/div[2]/div/h2");
	By CUSTOMER_MENU_BUTTON_FIELD = By.xpath("//span[contains(text(), 'Customers')]");
	By ADD_CUSTOMER_FIELD = By.xpath("//a[contains(text(), 'Add Customer')]");
	By FULL_NAME_FIELD = By.xpath("//input[@id='account']");
	By COMPANY_FIELD = By.xpath("//select[@id='cid']");
	By EMAIL_FIELD = By.xpath("//input[@id='email']");	
	By COUNTRY_FIELD = By.xpath("//select[@id='country']");
	
	//Test data
	String username = "demo@techfios.com";
	String password = "abc123";
	String DashboardHeader = "Dashboard";
	
	@BeforeClass
	public void readConfig() {
		
		try {
			
			InputStream input = new FileInputStream("src\\test\\java\\config\\config.properties");
			Properties prop = new Properties();
			prop.load(input);
			browser = prop.getProperty("browser");
			url = prop.getProperty("url");
		}catch(Exception e) {
			e.printStackTrace();
		}
		
					
	}				
		
			
	@BeforeMethod
	public void init() {
		
		if(browser.equalsIgnoreCase("Chrome")) {
			System.setProperty("webdriver.chrome.driver", "driver\\chromedriver.exe");
			driver = new ChromeDriver();
		}else if(browser.equalsIgnoreCase("Firefox")) {
				
			System.setProperty("webdriver.gecko.driver", "driver\\geckodriver.exe");
			driver = new FirefoxDriver();
			}
		 
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
		driver.get(url);

}
	@Test(priority = 1)
	public void loginTest() {
		
		driver.findElement(USER_NAME_FIELD).sendKeys(username);
		driver.findElement(PASSWORD_FIELD).sendKeys(password);
		driver.findElement(LOGIN_BUTTON_FIELD).click();
		Assert.assertEquals(driver.findElement(DASHBOARD_HEADER_FIELD).getText(), DashboardHeader, "Dashboard is not available");
	}
	@Test(priority = 2)
	public void addCustomer() throws InterruptedException {
		
		loginTest();
//		Thread.sleep(4000);
		driver.findElement(CUSTOMER_MENU_BUTTON_FIELD).click();
		driver.findElement(ADD_CUSTOMER_FIELD).click();
		Thread.sleep(5000);
		boolean fullNameField = driver.findElement(FULL_NAME_FIELD).isDisplayed();
		Assert.assertTrue(fullNameField, "Add Customer page is not available");
		
							
		driver.findElement(FULL_NAME_FIELD).sendKeys("Selenium" + generateRandomNo(999));
		
		selectFromDropdown(COMPANY_FIELD, "Techfios");
		
		driver.findElement(EMAIL_FIELD).sendKeys("abc123@techfios.com");
		
		
		selectFromDropdown(COUNTRY_FIELD, "India");
		
	}
	
	public int generateRandomNo(int boundryNo) {
		Random rnd = new Random();
		int randomNumber = rnd.nextInt(boundryNo);
		return randomNumber;
		
	}


	public void selectFromDropdown(By byLocator, String visibleText) {
		
		Select sel1 = new Select(driver.findElement(byLocator));
		sel1.selectByVisibleText(visibleText);
		
		
	}


	
	//@AfterMethod
	public void tearDown() {
		driver.close();
		driver.quit();
		
	}
}