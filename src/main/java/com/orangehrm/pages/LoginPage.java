package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;

public class LoginPage {

	private ActionDriver actiondriver;

//define locators using BY class
 
	private By userNameField = By.name("username");
	private By userPassword = By.cssSelector("input[type='password']");
	private By login = By.xpath("//button[text()=' Login ']");  
	private By errormesg = By.xpath("//p[text()='Invalid credentials']");

//Initialize the ActionDriver object by passing WebDriver instance
	public LoginPage(WebDriver driver) {
		this.actiondriver = new ActionDriver(driver);
	}

//method to perform login

	public void login(String userName, String password) {
		actiondriver.enterText(userNameField, userName);
		actiondriver.enterText(userPassword, password);
		actiondriver.click(login);
	}

//method to check if error mesg is displayed

	public boolean isErrorMesgDisplayed() {

		return actiondriver.displayed(errormesg);
	}

//methof ot get error text mesg

	public String getErrorText() {
		return actiondriver.getText(errormesg);
	}

//verify mesg is crt or not

	public boolean toVerifyErroMesg(String expectedError) {

		return actiondriver.compareText(errormesg, expectedError);
	}
}