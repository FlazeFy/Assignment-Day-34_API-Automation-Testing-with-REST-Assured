package core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApiTestListener implements ITestListener {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

    @Override
    public void onStart(ITestContext context) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

        extent = ExtentManager.getExtentReports();

        System.out.println("API Test Suite Started");
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        System.out.println("START: " + testName);

        ExtentTest test = extent.createTest(testName);
        extentTest.set(test);

        extentTest.get().log(Status.INFO, "Start");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        System.out.println("PASS: " + testName);

        long duration = result.getEndMillis() - result.getStartMillis();

        extentTest.get().log(Status.PASS, "Pass");
        extentTest.get().log(Status.INFO, "Time: " + duration + " ms");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        System.out.println("FAIL: " + testName);

        extentTest.get().log(Status.FAIL, "Fail");

        if (result.getThrowable() != null) {
            extentTest.get().log(Status.FAIL, result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String testName = result.getMethod().getMethodName();

        System.out.println("SKIP: " + testName);

        extentTest.get().log(Status.SKIP, "Skip");
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("API Test Suite Finished");

        System.out.println("Total: " + context.getAllTestMethods().length);
        System.out.println("Passed: " + context.getPassedTests().size());
        System.out.println("Failed: " + context.getFailedTests().size());
        System.out.println("Skipped: " + context.getSkippedTests().size());

        if (extent != null) {
            extent.flush();
            System.out.println("Report generated in /reports");
        }
    }
}