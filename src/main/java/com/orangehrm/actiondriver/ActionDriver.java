package com.orangehrm.actiondriver;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

import org.openqa.selenium.JavascriptExecutor;

public class ActionDriver {
	
private WebDriver driver;
private WebDriverWait wait;
public static final Logger logger = BaseClass.logger;


public  ActionDriver(WebDriver driver) throws NumberFormatException {
	this.driver=driver;
	
	int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
	this.wait=new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
}
//method to click an element

public void click(By by) {
	String elementDescription = getElementDescription(by);
	try {waitForElementToBeClickable(by);
		driver.findElement(by).click();
		ExtentManager.logStep("clicked an element: "+elementDescription);
		logger.info("clicked an element-->"  +elementDescription);
		
	} catch (Exception e) {
		// TODO Auto-generated catch block
		System.out.println("unable to click an element:" + e.getMessage());
		ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:", elementDescription+"_unable to click");
		
		logger.error("not clicked an element");
	}
}
//Method to get the description of an element using By locator
	public String getElementDescription(By locator) {
		// Check for null driver or locator to avoid NullPointerException
		if(driver == null) {
			return "driver not initalised";
		}if (locator == null) {
			return "Locator is null.";
		}
		try {
			
			// Find the element using the locator
						WebElement element = driver.findElement(locator);

						// Get element attributes
						String name = element.getDomProperty("name");
						String id = element.getDomProperty("id");
						String text = element.getText();
						String className = element.getDomProperty("class");
						String placeholder = element.getDomProperty("placeholder");

						// Return a description based on available attributes
						if (isNotEmpty(name)) {
							return "Element with name: " + name;
						} else if (isNotEmpty(id)) {
							return "Element with ID: " + id;
						} else if (isNotEmpty(text)) {
							return "Element with text: " + truncate(text, 50);
						} else if (isNotEmpty(className)) {
							return "Element with class: " + className;
						} else if (isNotEmpty(placeholder)) {
							return "Element with placeholder: " + placeholder;
						} else {
							return "Element located using: " + locator.toString();
						}
					} catch (Exception e) {
						// Log exception for debugging
						e.printStackTrace(); // Replace with a logger in a real-world scenario
						return "Unable to describe element due to error: " + e.getMessage();
					}
				}
		
	// Utility method to check if a string is not null or empty
		private boolean isNotEmpty(String value) {
			return value != null && !value.isEmpty();
		}

		// Utility method to truncate long strings
		private String truncate(String value, int maxLength) {
			if (value == null || value.length() <= maxLength) {
				return value;
			}
			return value.substring(0, maxLength) + "...";
		}
		
//method for entering values--avoiding the duplication
public void enterText(By by, String value) {
	
	
	
	try {
		waitForElementToBeVisible(by);
		WebElement element = driver.findElement(by);
		element.clear();
		element.sendKeys(value);
		logger.info("Entered text on " + getElementDescription(by) + "-->" + value);
	} catch (Exception e) {
		logger.error("text not entered:" + e.getMessage());
	}
}

//method for to get text

public String getText(By by) {
	
	try {
		waitForElementToBeVisible(by);
		return driver.findElement(by).getText();
	} catch (Exception e) {
		logger.error("unable to get the text:" + e.getMessage());
	}
	return " ";
}

//method to compare two text

public boolean  compareText(By by, String expectedText) {
	try {
		waitForElementToBeVisible(by);
		String actualText = driver.findElement(by).getText();
		
		if(expectedText.equals(actualText)) {
			
			logger.info("Texts are matching:" +actualText+" equals" +expectedText);
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text", "Text Verified Successfully! "+actualText+ " equals "+expectedText);
			
			return true;
		}
		else {
			logger.info("Texts are matching:" +actualText+" not equals" +expectedText);
			ExtentManager.logFailure(BaseClass.getDriver(), "Text Comparison Failed!", "Text Comparison Failed! "+actualText+ " not equals "+expectedText);
			
			return false;
		}
	} catch (Exception e) {
		logger.error("unable to comapre texts:" +e.getMessage());
	}
	return false;
	}
//method for displayed

public boolean displayed(By by) {
	try {
		waitForElementToBeVisible(by);
		logger.info("Element is displayed " + getElementDescription(by));
		ExtentManager.logStep("Element is displayed: "+getElementDescription(by));
		ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ", "Element is displayed: "+getElementDescription(by));
		
		return driver.findElement(by).isDisplayed();
		
//		if(isdisplayed) {
//			System.out.println("Element is visible");
//			return isdisplayed;
//			}
//			else {
//				return isdisplayed;
//			}
	} catch (Exception e) {
		// TODO Auto-generated catch block
		logger.error("element is not displayed:" +e.getMessage());
		ExtentManager.logFailure(BaseClass.getDriver(),"Element is not displayed: ","Elemenet is not displayed: "+getElementDescription(by));
		
		return false;
	}
		
		
	}
//wait for page to load	

public void waitForPageLoad(int timeOutInSec) {
	
	try {
		
		wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
				.executeScript("return document.readyState").equals("complete"));
		logger.info("page loaded successfully");
	}catch(Exception e) {
		logger.error("page did not load within" +timeOutInSec+ "seconds"+e.getMessage());
	}
}
	//scroll to an element

public void dcrollToElement(By by) {
	
	try {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		WebElement ele = driver.findElement(by);
		js.executeScript("argument[0].scrollIntoView(true);",ele);
	} catch (Exception e) {
		
		e.printStackTrace();
	}
}
//wait for element to be clickable
private void waitForElementToBeClickable(By by) {
	
	try {
		wait.until(ExpectedConditions.elementToBeClickable(by));
	} catch (Exception e) {
		System.out.println("element not  clickable:" + e.getMessage());
	}
}
//wait for element to be visible
private void waitForElementToBeVisible(By by) {
	
	try {
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	} catch (Exception e) {
		System.out.println("element is not visible:" + e.getMessage());
	}
}
	
}
