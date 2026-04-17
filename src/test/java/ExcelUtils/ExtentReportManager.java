package ExcelUtils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;



public class ExtentReportManager implements ITestListener {
    ExtentSparkReporter sparkReporter;
    ExtentReports extent;
    ExtentTest test;

    public void onStart(ITestContext context) {
        sparkReporter = new ExtentSparkReporter("C:\\Users\\2480119\\IdeaProjects\\HotelProjectPomTekstack\\reports\\EntentReport.html");
        sparkReporter.config().setDocumentTitle("Extent Reports Demo");
        sparkReporter.config().setReportName("Report on Listeners");
        sparkReporter.config().setTheme(Theme.DARK);
        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Computer Name", "localhost");
        extent.setSystemInfo("Enviroment", "QA");
        extent.setSystemInfo("Tester Name", "Ashishhhhhh");
        extent.setSystemInfo("os", "Windows");
        extent.setSystemInfo("Browser Name", "chrome");
    }

    //	public void onTestStart(ITestResult result) {
//	    System.out.println("Test started");
//	  }
    public void onTestSuccess(ITestResult result) {
        test = extent.createTest(result.getName());
        test.log(Status.PASS, "Test case PASSED is: " + result.getName());

    }

    public void onTestFailure(ITestResult result) {
        test = extent.createTest(result.getName());
        test.log(Status.FAIL, "Test case FAILED is: " + result.getName());
        test.log(Status.FAIL, "Test case FAILED cause is: " + result.getThrowable());
    }

    public void onTestSkipped(ITestResult result) {
        test = extent.createTest(result.getName());
        test.log(Status.SKIP, "Test case SKIPPED is: " + result.getName());
    }

    public void onFinish(ITestContext context) {
        extent.flush();
    }
}