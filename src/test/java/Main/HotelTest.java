package Main;

import DataProvider.DataProviderClass;
import ExcelUtils.ExcelClass;
import ExcelUtils.ExtentReportManager;
import Home.HomePages;
import Pages.PagesTest;
import ScreenShotUtil.ScreenShot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.*;

@Listeners(ExtentReportManager.class)
public class HotelTest {


    public WebDriver driver = null;
    public static String excelPath;
    private int rowIndex = 1;
    public SoftAssert softAssert = null;

    public static Logger logger;

    public void setUpDriver(String dName) {
        logger = LogManager.getLogger(this.getClass());
        if (dName.equalsIgnoreCase("Chrome")) {
            driver = new ChromeDriver();
        } else if (dName.equalsIgnoreCase("Edge")) {
            driver = new EdgeDriver();
        } else if (dName.equalsIgnoreCase("Firefox")) {
            driver = new FirefoxDriver();
        } else {
            System.out.println("Wrong Browser Choice");
            return;  // ← stop here, don't call methods on null driver
        }
        driver.manage().window().maximize();
        driver.get("https://www.trivago.in/");
        driver.manage().deleteAllCookies();
    }


    @DataProvider(name = "hotelData")
    public Object[][] getObjectArray() throws IOException {
        excelPath = System.getProperty("user.dir") + "\\Resource\\HotelBooking.xlsx";
        return DataProviderClass.getHotelData(excelPath);
    }



    @Test(dataProvider = "hotelData")
    public void testHotelBooking(String browser, String destination,
                                 String checkInMonth, String checkInYear, String checkInDate,
                                 String checkOutMonth, String checkOutYear, String checkOutDate,
                                 String adultInput, String roomsAvailable, String expectedResult) throws Exception {

        excelPath = System.getProperty("user.dir") + "\\Resource\\HotelBooking.xlsx";
        String sheet = "Data1";
        softAssert = new SoftAssert();

        setUpDriver(browser);
        logger = LogManager.getLogger(this.getClass());
        logger.info("Setting up the browser");
        ScreenShot.resetStepCounter();

     
        Assert.assertNotNull(driver, "Invalid browser: " + browser);

        HomePages homePage = new HomePages(driver);

        try {
        
            Assert.assertTrue(homePage.isValidDestination(destination),
                    "Invalid destination: " + destination);

            int adults = Integer.parseInt(adultInput);
            int rooms = Integer.parseInt(roomsAvailable);

            homePage.enterDestination(destination);
            ScreenShot.capture(driver, "destination_selected");

            homePage.waitForSearchFormReady();
            homePage.navigateToCheckInMonth(checkInMonth, checkInYear);
            homePage.selectCheckInDate(checkInDate);
            homePage.navigateToCheckOutMonth(checkOutMonth, checkOutYear);
            homePage.selectCheckOutDate(checkOutDate);

            homePage.guestSelector();
            homePage.setRooms(rooms);
            homePage.setAdult(adults);
            ScreenShot.capture(driver, "search_clicked");
            homePage.clickApply();
            homePage.clickSearch();

            PagesTest resultsPage = new PagesTest(driver);
            resultsPage.sortByTopGuestRatings();
            resultsPage.waitForResults();
            ScreenShot.capture(driver, "results_sorted");

            List<Double> ratings = resultsPage.getRatings();
            boolean isSort = resultsPage.isSorted(ratings);
            String actualResult = isSort ? "Sorted" : "Not Sorted";

            String[][] hotelData = resultsPage.getAllDataHotels();
            Assert.assertNotNull(hotelData, "Hotel data is null");
            Assert.assertTrue(hotelData.length != 0, "Hotel data list is empty");

            int printCount = Math.min(5, hotelData.length);
            for (int j = 0; j < printCount; j++) {
                System.out.println(hotelData[j][0] + " | " + hotelData[j][1] + " | " + hotelData[j][2]);
            }

            ExcelClass.setCellData(excelPath, sheet, rowIndex, 11, actualResult);

            // Soft assert for non-blocking verifications
            softAssert.assertEquals(actualResult, expectedResult.trim(),
                    "Sort verification failed");

            writeExcelStatus(sheet, actualResult.equalsIgnoreCase(expectedResult.trim()));

            softAssert.assertAll();

        } catch (Throwable t) {
            // Log to Excel + Extent, then rethrow so TestNG marks it FAIL
            ExcelClass.setCellData(excelPath, sheet, rowIndex, 11, "EmptyList");
            ExcelClass.setCellData(excelPath, sheet, rowIndex, 12, "FAIL");
            ExcelClass.setRedColor(excelPath, sheet, rowIndex, 12);
            logger.error("Test failed for row " + rowIndex + ": " + t.getMessage(), t);
            throw t;   // ❗ CRITICAL: rethrow so Extent listener sees the failure
        } finally {
            rowIndex++;
        }
    }

    private void writeExcelStatus(String sheet, boolean pass) throws IOException {
        if (pass) {
            ExcelClass.setCellData(excelPath, sheet, rowIndex, 12, "PASS");
            ExcelClass.setGreenColor(excelPath, sheet, rowIndex, 12);
        } else {
            ExcelClass.setCellData(excelPath, sheet, rowIndex, 12, "FAIL");
            ExcelClass.setRedColor(excelPath, sheet, rowIndex, 12);
        }
    }


    @AfterMethod
    public void tearDown() {


        if (driver != null) {

            driver.quit();

        }
    }
}

