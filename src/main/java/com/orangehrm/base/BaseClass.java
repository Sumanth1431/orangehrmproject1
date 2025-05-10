package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
//	protected WebDriver driver;
//	private static ActionDriver actiondriver;
	
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actiondriver = new ThreadLocal<>();
	
	
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	// Getter method for soft assert
	public SoftAssert getSoftAssert() {
		return softAssert.get();
	}
	@BeforeSuite
	// load the configuration file
	public void loadConfig() throws IOException {

		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir") + "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");
	}

	
	
	
	@BeforeMethod
// initialization of webdriver based on browser defined in config file
	public synchronized void setup() throws IOException {
		System.out.println("Setting up webdriver for:" + this.getClass().getSimpleName());// to get particular driver for which class
		launchBrowser();
		configureBrowser();
		staticWait(3);
		logger.info("webdriver is initalized");
		logger.trace("This is a Trace message");
		logger.error("This is a error message");
		logger.debug("This is a debug message");
		logger.fatal("This is a fatal message");
		logger.warn("This is a warm message");
		// Initialize ActionDriver for the current Thread
				actiondriver.set(new ActionDriver(getDriver()));
				logger.info("ActionDriver initlialized for thread: " + Thread.currentThread().getId());

				// Start the Extent Report
				// ExtentManager.getReporter(); //--This has been implemented in TestListener
				

	}

	private void launchBrowser() {

		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			// Create ChromeOptions
						ChromeOptions options = new ChromeOptions();
						options.addArguments("--headless"); // Run Chrome in headless mode
						options.addArguments("--disable-gpu"); // Disable GPU for headless mode
						options.addArguments("--window-size=1920,1080"); // Set window size
						options.addArguments("--disable-notifications"); // Disable browser notifications
						options.addArguments("--no-sandbox"); // Required for some CI environments like Jenkins
						options.addArguments("--disable-dev-shm-usage"); // Resolve issues in resource-limited environments

			//driver = new ChromeDriver();
			driver.set(new ChromeDriver(options)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("ChromeDriver Instance is created.");
		} else if (browser.equalsIgnoreCase("firefox")) {
			//
			//driver = new FirefoxDriver();
			// Create FirefoxOptions
						FirefoxOptions options = new FirefoxOptions();
						options.addArguments("--headless"); // Run Firefox in headless mode
						options.addArguments("--disable-gpu"); // Disable GPU rendering (useful for headless mode)
						options.addArguments("--width=1920"); // Set browser width
						options.addArguments("--height=1080"); // Set browser height
						options.addArguments("--disable-notifications"); // Disable browser notifications
						options.addArguments("--no-sandbox"); // Needed for CI/CD environments
						options.addArguments("--disable-dev-shm-usage"); // Prevent crashes in low-resource environments

						// driver = new FirefoxDriver();
						driver.set(new FirefoxDriver(options)); // New Changes as per Thread
						ExtentManager.registerDriver(getDriver());
			logger.info("firefox Instance is created.");

		} else if (browser.equalsIgnoreCase("edge")) {
			//driver = new EdgeDriver();
			EdgeOptions options = new EdgeOptions();
			options.addArguments("--headless"); // Run Edge in headless mode
			options.addArguments("--disable-gpu"); // Disable GPU acceleration
			options.addArguments("--window-size=1920,1080"); // Set window size
			options.addArguments("--disable-notifications"); // Disable pop-up notifications
			options.addArguments("--no-sandbox"); // Needed for CI/CD
			options.addArguments("--disable-dev-shm-usage"); // Prevent resource-limited crashes
			
			// driver = new EdgeDriver();
			driver.set(new EdgeDriver(options)); // New Changes as per Thread
			ExtentManager.registerDriver(getDriver());
			logger.info("edgedriver Instance is created.");
		} else {
			throw new IllegalArgumentException("Browser Not Supported:" + browser);
		}
	}

	private void configureBrowser() {
		// implicit wait///here parseint n all used because implictwait is defined as
		// string in config file so to convert int we used below line
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));

		// maximixe diver
		getDriver().manage().window().maximize();

		// get the url
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("url not navigated:" + e.getMessage());
		}

	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("unable to quit the driver:" + e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed.");
		driver.remove();
		actiondriver.remove();
		
		//ExtentManager.endTest();// --This has been implemented in TestListener
	}
	
	// Getter Method for WebDriver
		public static WebDriver getDriver() {

			if (driver.get() == null) {
				System.out.println("WebDriver is not initialized");
				throw new IllegalStateException("WebDriver is not initialized");
			}
			return driver.get();

		}

		// Getter Method for ActionDriver
		public static ActionDriver getActionDriver() {

			if (actiondriver.get() == null) {
				System.out.println("ActionDriver is not initialized");
				throw new IllegalStateException("ActionDriver is not initialized");
			}
			return actiondriver.get();

		}
	public static Properties getProp() {
		return prop;
	}
//	public WebDriver getDriver() {
//		
//		return driver;
//	}
	
	public void setDriver(ThreadLocal<WebDriver> driver) {
		this.driver=driver;
	}
	
	//static wait for pause in java
	public void staticWait(int sec) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(sec));
		
		
	}
}
