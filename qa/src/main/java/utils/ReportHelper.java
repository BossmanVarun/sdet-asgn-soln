package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReportHelper {

  private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

  public static ExtentReports initReport() {
    String reportName =
        String.format("testReport-%s", sdf.format(new Timestamp(new Date().getTime())));
    String filePath = "reports/" + reportName + ".html";
    File file = new File(filePath);
    try {
      if (file.createNewFile()) {
        log.info("File Created Successfully!");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    ExtentSparkReporter sparkReporter = new ExtentSparkReporter(filePath);
    ExtentReports extent = new ExtentReports();
    extent.attachReporter(sparkReporter);
    sparkReporter.config().setDocumentTitle("CustomerSvcReport");
    sparkReporter.config().setReportName(reportName);
    sparkReporter.config().setTheme(Theme.STANDARD);
    return extent;
  }

  public static void logRequestResponse(ExtentTest test, StringWriter requestWriter) {
    String[] requests = requestWriter.toString().split("Request method:");
    String[] responses = requestWriter.toString().split("HTTP/1.1");
    for (int i = 1; i < requests.length; i++) {
      test.log(
          Status.INFO, "<pre>Request: " + requests[i] + "\nResponse: " + responses[i] + "</pre>");
    }
  }
}
