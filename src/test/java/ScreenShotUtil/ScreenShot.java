package ScreenShotUtil;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class ScreenShot {

    private static final String SCREENSHOT_DIR = System.getProperty("user.dir") + File.separator + "Screenshots";
    private static final AtomicInteger stepCounter = new AtomicInteger(0);


    public static void resetStepCounter() {
        stepCounter.set(0);
    }

    public static String capture(WebDriver driver, String stepName) {
        try {
            Path dir = Paths.get(SCREENSHOT_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            int step = stepCounter.incrementAndGet();
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = String.format("Step%02d_%s_%s.png", step, stepName, timestamp);

            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Path destPath = dir.resolve(fileName);
            Files.copy(srcFile.toPath(), destPath, StandardCopyOption.REPLACE_EXISTING);

            return destPath.toString();

        } catch (IOException e) {
            return null;
        }
    }
}
