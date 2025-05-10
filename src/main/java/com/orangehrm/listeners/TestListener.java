package com.orangehrm.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class TestListener implements ITestListener, IAnnotationTransformer {
//triggered when a test starts
	@Override
	public void onTestStart(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		//start logging in extent reports
		ExtentManager.startTest(testName);
		ExtentManager.logStep("Test started"+testName);
	}
	//triggered when test success
	@Override
	public void onTestSuccess(ITestResult result) {
		String testName = result.getMethod().getMethodName();

		if (!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passed Successfully!",
					"Test End: " + testName + " - ✔ Test Passed");
		} else {
			ExtentManager.logStepValidationForAPI("Test End: " + testName + " - ✔ Test Passed");
		}

	}
	
	@Override
	public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
		annotation.setRetryAnalyzer(RetryAnalyzer.class);
	}
//triggered when a test fails
	@Override
	public void onTestFailure(ITestResult result) {
		
		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();
		ExtentManager.logStep(failureMessage);
		if(!result.getTestClass().getName().toLowerCase().contains("api")) {
			ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!", "Test End: " + testName + " - ❌ Test Failed");
		}
		else {
			ExtentManager.logFailureAPI("Test End: " + testName + " - ❌ Test Failed");
		}
	}

	//triggered when a test skips
	@Override
	public void onTestSkipped(ITestResult result) {
		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("test skipped" +testName);
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
	}

	@Override
	public void onTestFailedWithTimeout(ITestResult result) {
		// TODO Auto-generated method stub
		ITestListener.super.onTestFailedWithTimeout(result);
	}

	
	// Triggered when a suite Starts
		@Override
		public void onStart(ITestContext context) {
			// Initialize the Extent Reports
			ExtentManager.getReporter();
		}
//triggered when suite ends
	@Override
	public void onFinish(ITestContext context) {
		//flush the extent reports
		ExtentManager.endTest();
		
	}
	

}
