package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass{
	private LoginPage loginpage;
	private HomePage homepage;
	
	@BeforeMethod
	public void setupPages() {
		loginpage = new LoginPage(getDriver());
		homepage  = new HomePage(getDriver());

}
	@Test
	public void toVerfiyorangeHRMLogo() {
		ExtentManager.startTest("Home Page Verify Logo Test"); //--This has been implemented in TestListener
				ExtentManager.logStep("Navigating to Login Page entering username and password");
				
		
		loginpage.login("admin", "admin123");
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homepage.orangeHrmLoogo(),"logo is not visible");
		ExtentManager.logStep("Validation Successful");
		
		ExtentManager.logStep("Logged out Successfully!");
	}

}
