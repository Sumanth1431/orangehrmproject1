package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;

public class HomePage {
	private ActionDriver actiondriver;

	// Define locators using By class

	private By adminTab = By.xpath("//span[text()='Admin']");
	private By userIdButton = By.className("oxd-userdropdown-name");
	private By logout = By.xpath("//a[text()='Logout']");
	private By orangerHrmLogo = By.xpath("//div[@class='oxd-brand-banner']//img");

//method to check its visible

	public boolean isAdminTabVisible() {

		return actiondriver.displayed(adminTab);
	}

//method to check orangehrm logo its visible

	public boolean orangeHrmLoogo() {

		return actiondriver.displayed(orangerHrmLogo);
	}

//method to perform logout
	public void logout() {
		actiondriver.click(userIdButton);
		actiondriver.click(logout);
	}

	// Initialize the ActionDriver object by passing WebDriver instance
	public HomePage(WebDriver driver) {
		this.actiondriver = new ActionDriver(driver);
	}
}
