package listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import java.io.PrintStream;
import java.io.StringWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.WriterOutputStream;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.ReportHelper;

@Slf4j
public class TestNgListener implements ITestListener {

  private ExtentReports report;
  private ExtentTest currentTest;
  private StringWriter reqWriter;
  private StringWriter resWriter;
  private PrintStream reqStream;

  @Override
  public void onStart(ITestContext context) {
    report = ReportHelper.initReport();
  }

  @Override
  public void onTestStart(ITestResult result) {
    currentTest = report.createTest(result.getName());
    reqWriter = new StringWriter();
    resWriter = new StringWriter();
    reqStream = new PrintStream(new WriterOutputStream(reqWriter, "UTF-8"), true);
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    currentTest.pass(result.getName());
    currentTest.log(
        Status.PASS, MarkupHelper.createLabel(result.getName() + " PASSED ", ExtentColor.GREEN));
    ReportHelper.logRequestResponse(currentTest, reqWriter);
  }

  @Override
  public void onTestFailure(ITestResult result) {
    currentTest.log(
        Status.FAIL, MarkupHelper.createLabel(result.getName() + " FAILED ", ExtentColor.RED));
    currentTest.fail(result.getThrowable());
    ReportHelper.logRequestResponse(currentTest, reqWriter);
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    currentTest.log(
        Status.SKIP, MarkupHelper.createLabel(result.getName() + " SKIPPED ", ExtentColor.AMBER));
    currentTest.fail(result.getThrowable());
    ReportHelper.logRequestResponse(currentTest, reqWriter);
  }

  @Override
  public void onFinish(ITestContext context) {
    report.flush();
    ReportHelper.openReport();
  }
}
