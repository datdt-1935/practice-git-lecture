package framework;

import static org.testng.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class Capture {
	WebDriver driver = null;
	
	private static Log log = LogFactory.getLog(Capture.class);
	
	@BeforeTest
	public void init() {

		System.setProperty("webdriver.chrome.driver", "C:\\Users\\nguyen.thi.thuyb\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		log.info("before");
	}
	
	
	private void login() {
		
		driver.get("http://selenium-training.herokuapp.com/login");
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		WebElement emailField = driver.findElement(By.cssSelector("form[action='/login'] input[type='email']"));
		emailField.clear();
		emailField.sendKeys("nguyen.thuy2@gmail.com");

		WebElement passwordField = driver.findElement(By.cssSelector("form[action='/login'] input[type='password']"));
		passwordField.clear();
		passwordField.sendKeys("123456");

		WebElement submit = driver.findElement(By.name("commit"));
		submit.click();

		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".user_info"))));
		
	}

	@DataProvider
	public Object[][] editName() {
		Object[][] data = new Object[1][1];

		data[0][0] = "ThuyNT Thuy";// thay doi ten
		return data;
	}

	// Doi ten thanh cong
	@Test(priority = 1, dataProvider = "editName", groups = "Setting")
	public void testTC1(String name) {
		log.info("start test case TC1");
		login();
		driver.get("https://selenium-training.herokuapp.com/users/625/edit");

		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".edit_user"))));

		WebElement form = driver.findElement(By.cssSelector(".edit_user"));
		WebElement nameField = form.findElement(By.cssSelector("input[name='user[name]']"));
		nameField.clear();
		nameField.sendKeys(name);

		WebElement btn = form.findElement(By.cssSelector("input[type='submit']"));
		btn.click();
		driver.switchTo().alert().accept();

		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".user_info"))));
		WebElement userInfor = driver.findElement(By.cssSelector(".user_info"));
		// assertEquals(userInfor.getText(), name);

		String[] nameTitle = driver.getTitle().split("\\|");
		String[] realOutput = { userInfor.getText(), nameTitle[0].trim() };
		String[] expectedOutput = { name, name };
		assertEquals(realOutput, expectedOutput);

	}

	@Test()
	public void testTC5() {
		login();
		driver.get("https://selenium-training.herokuapp.com/users/625/edit");

		WebDriverWait wait = new WebDriverWait(driver, 15);
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".edit_user"))));

		WebElement form = driver.findElement(By.cssSelector(".edit_user"));
		Select languages = new Select(form.findElement(By.cssSelector("select[name='user[language]']")));

		languages.selectByValue("2");

		WebElement btn = form.findElement(By.cssSelector("input[type='submit']"));
		btn.click();
		driver.switchTo().alert().accept();

		driver.navigate().to("https://selenium-training.herokuapp.com/users/625/edit");
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.cssSelector(".edit_user"))));

		form = driver.findElement(By.cssSelector(".edit_user"));
		languages = new Select(form.findElement(By.cssSelector("select[name='user[language]']")));
		try {
			assertEquals(languages.getFirstSelectedOption().getAttribute("value"), "1");
		} catch (AssertionError e) {
			// TODO: handle exception
			capture("TC5.jpg");
		}

	}

	private void capture(String path) {
		File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(screenshot, new File("C:\\Users\\nguyen.thi.thuyb\\Pictures\\Capture\\" + path));
		} catch (IOException ioe) {
			// TODO: handle exception
			ioe.printStackTrace();
		}
	}

	@AfterTest
	public void close() {
		log.info("after");
		driver.close();
	}
}
