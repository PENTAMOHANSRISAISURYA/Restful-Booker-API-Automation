package org.restpractice.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class ExtentReportListener implements ITestListener {

    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        ExtentSparkReporter sparkReporter =
                new ExtentSparkReporter("target/extent-report/index.html");
        sparkReporter.config().setDocumentTitle("Restful Booker API Test Report");
        sparkReporter.config().setReportName("API Automation Results");
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setEncoding("utf-8");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Application", "Restful Booker");
        extent.setSystemInfo("Base URL", "https://restful-booker.herokuapp.com");
        extent.setSystemInfo("Framework", "REST Assured + TestNG");
        extent.setSystemInfo("Environment", "QA");
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(
                result.getMethod().getMethodName(),
                result.getMethod().getDescription());
        extentTest.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        extentTest.get().log(Status.PASS, "Test Passed ");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        extentTest.get().log(Status.FAIL, "Test Failed : " + result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        extentTest.get().log(Status.SKIP, "Test Skipped : " + result.getThrowable());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
        System.out.println("\n📊 Extent Report: target/extent-report/index.html");
    }
}
