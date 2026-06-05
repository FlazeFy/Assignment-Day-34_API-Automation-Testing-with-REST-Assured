package core;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import java.io.File;

public class ExtentManager {
    private static ExtentReports extent;

    public static ExtentReports getExtentReports() {

        if (extent == null) {

            File dir = new File("reports");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            ExtentSparkReporter spark = new ExtentSparkReporter("reports/api-report.html");

            extent = new ExtentReports();
            extent.attachReporter(spark);
        }

        return extent;
    }
}