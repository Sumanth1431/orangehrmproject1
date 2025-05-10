package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;
import org.testng.annotations.DataProvider;

public class LoginPageTest extends BaseClass{
	private LoginPage loginpage;
	private HomePage homepage;
	
	@BeforeMethod
	public void setupPages() {
		loginpage = new LoginPage(getDriver());
		homepage  = new HomePage(getDriver());
System.out.println("hello");
}
	
	@Test(dataProvider="validLoginData", dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username, String password) {
		//ExtentManager.startTest("Valid Login Test"); //--This has been implemented in TestListener
		System.out.println("Running testMethod1 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		
		loginpage.login(username, password);
		ExtentManager.logStep("Verifying Admin tab is visible or not");
		Assert.assertTrue(homepage.isAdminTabVisible(),"Admin tab should be visible after successfull login ");
		ExtentManager.logStep("Validation Successful");
		
		homepage.logout();
		ExtentManager.logStep("Logged out Successfully!");
		staticWait(3);
	}
	
	@Test(dataProvider="inValidLoginData", dataProviderClass = DataProviders.class)
	public void inValidLoginTest(String username, String password) {
		//ExtentManager.startTest("In-valid Login Test!"); //--This has been implemented in TestListener
		System.out.println("Running testMethod2 on thread: " + Thread.currentThread().getId());
		ExtentManager.logStep("Navigating to Login Page entering username and password");
		loginpage.login(username, password);
		String expectedErrorMessage = "Invalid credentials";
		Assert.assertTrue(loginpage.toVerifyErroMesg(expectedErrorMessage),"Test Failed: Invalid error message");
		ExtentManager.logStep("Validation Successful");
		
	}
}