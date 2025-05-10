package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass{
	@Test
	public void dummyTest()  {
		//ExtentManager.startTest("DummyTest2 Test"); //--This has been implemented in TestListener
		String title = getDriver().getTitle();
		ExtentManager.logStep("verifying the title");
		assert title.equals("OrangeHRM") : " Test failed-not matched" ;
		System.out.println("title matched");
		
		
		 ExtentManager.logStep("Validation Successful");
		
		
	}

}
