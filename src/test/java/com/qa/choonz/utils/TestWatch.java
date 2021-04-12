package com.qa.choonz.utils;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestWatch implements TestWatcher {
	
	public static ExtentReports report = new ExtentReports("Documentation/Choonz_test_Report.html", false);
	public static ExtentTest test;
	
	@Override
    public void testFailed(ExtensionContext extensionContext, Throwable cause) {
		test.log(LogStatus.FAIL, "test has failed due to " + cause);
        report.endTest(test);
    }

}
