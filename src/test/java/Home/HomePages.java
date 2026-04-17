package Home;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePages {
    public WebDriver driver;
    public WebDriverWait wait;
    JavascriptExecutor js;

    public HomePages(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
        this.js = (JavascriptExecutor) driver;
    }

    @FindBy(id = "input-auto-complete")
    WebElement destinationInput;

    @FindBy(xpath = "//section[@data-testid='guest-selector-popover']")
    WebElement guestSelectorBtn;

    @FindBy(xpath = "//button[@data-testid='search-button-with-loader']")
    WebElement searchButton;

    @FindBy(xpath = "//input[@data-testid='adults-amount']")
    WebElement adultsInput;

    @FindBy(xpath = "//button[@data-testid='adults-amount-plus-button']")
    WebElement adultsPlusBtn;

    @FindBy(xpath = "//button[@data-testid='adults-amount-minus-button']")
    WebElement adultsMinusBtn;

    @FindBy(xpath = "//input[@data-testid='rooms-amount']")
    WebElement roomsInput;

    @FindBy(xpath = "//button[@data-testid='rooms-amount-plus-button']")
    WebElement roomsPlusBtn;

    @FindBy(xpath = "//button[@data-testid='rooms-amount-minus-button']")
    WebElement roomsMinusBtn;

    @FindBy(xpath = "//button[@data-testid='guest-selector-apply']")
    WebElement applyBtn;


    By checkInMonthHeader = By.xpath("//button[@data-testid='search-button-with-loader']/following-sibling::div//h3[contains(@class,'_9z0Y6L')]");


    By nextButton = By.xpath("//div[@data-testid='calendar-popover']//button[@data-testid='calendar-button-next']");


    public boolean isValidDestination(String destination) {
        if (destination == null || destination.trim().isEmpty()) return false;

        for (char c : destination.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                System.out.println("Invalid character found: " + c);
                return false;
            }
        }
        return true;
    }


    public void enterDestination(String destination) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(destinationInput));
        input.clear();
        input.sendKeys(destination);
        List<WebElement> suggestions = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//ul[@data-testid='auto-complete-suggestion-list']/li")));
        System.out.println("Suggestions found: " + suggestions.size());
        wait.until(ExpectedConditions.elementToBeClickable(suggestions.get(0))).click();
    }


    public void waitForSearchFormReady() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@data-testid='search-form']//fieldset/button")));
    }

    private void clickNextMonth() {
        WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
        js.executeScript("arguments[0].click();", nextBtn);
    }


    public void navigateToCheckInMonth(String checkInMonth, String checkInYear) {
        while (true) {
            WebElement header = wait.until(ExpectedConditions.presenceOfElementLocated(checkInMonthHeader));
            String foundMonth = header.getText();
            System.out.println("Searching month: " + foundMonth);

            if (foundMonth.contains(checkInMonth) && foundMonth.contains(checkInYear)) {
                System.out.println("Check-in month found: " + foundMonth);
                break;
            } else {
                clickNextMonth();
            }
        }
    }

    public void selectCheckInDate(String checkInDate) throws InterruptedException {

        Thread.sleep(500);
        List<WebElement> checkInDates = driver.findElements(
                By.xpath("(//button[@data-testid='calendar-button-next']/following-sibling::div/div)[1]//button/time"));

        for (WebElement el : checkInDates) {
            if (checkInDate.equals(el.getText())) {
                js.executeScript("arguments[0].click();", el);
                System.out.println("Check-in date clicked: " + checkInDate);
                break;
            }
        }
    }

    boolean checkOutInLeftPanel = false;

    public void navigateToCheckOutMonth(String checkOutMonth, String checkOutYear) {
        while (true) {
            WebElement leftHeader = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[@data-testid='search-button-with-loader']/following-sibling::div//h3[contains(@class,'_9z0Y6L')]")));

            WebElement rightHeader = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[@data-testid='search-button-with-loader']/following-sibling::div//h3[contains(@class,'_2GKzZx')]")));

            String leftText = leftHeader.getText();
            String rightText = rightHeader.getText();

            if (leftText.contains(checkOutMonth) && leftText.contains(checkOutYear)) {
                checkOutInLeftPanel = true;
                System.out.println("Check out month found in left panel: " + checkOutMonth);
                break;
            } else if (rightText.contains(checkOutMonth) && rightText.contains(checkOutYear)) {
                checkOutInLeftPanel = false;
                System.out.println("Check out month found in right panel: " + checkOutMonth);
                break;
            }
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(nextButton));
            js.executeScript("arguments[0].click();", nextBtn);
        }
    }

    public void selectCheckOutDate(String checkOutDate) throws InterruptedException {

        Thread.sleep(500);
        String checkOutDivIndex = checkOutInLeftPanel ? "1" : "2";
        List<WebElement> checkOutDates = driver.findElements(
                By.xpath("(//button[@data-testid='calendar-button-next']/following-sibling::div/div)[" + checkOutDivIndex + "]//button[not(contains(@class,'r2bCKu'))]/time"));

        for (WebElement el : checkOutDates) {
            if (checkOutDate.equals(el.getText())) {
                js.executeScript("arguments[0].click();", el);
                System.out.println("Check-out date clicked: " + checkOutDate + " from div " + checkOutDivIndex);
                break;
            }
        }
    }
    public void guestSelector() {
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//section[@data-testid='guest-selector-popover']"))).click();
    }

//    public void setAdults (int adults) throws InterruptedException {
//        Thread.sleep(500); // wait for popover to fully load
//        while (true) {
//            String adultVal = wait.until(ExpectedConditions.visibilityOf(adultsInput)).getAttribute("value");
//            int currentAdults = Integer.parseInt(adultVal);
//            if (adults == currentAdults) {
//                System.out.println("Adult member are matched " + adults);
//                break;
//            } else if (adults > currentAdults) {
//                adultsPlusBtn.click();
//                Thread.sleep(300); // wait for DOM update
//            } else {
//                adultsMinusBtn.click();
//                Thread.sleep(300); // wait for DOM update
//            }
//        }
//    }


    public void setAdult(int adults) {

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//section[@data-testid='guest-selector-popover']")));

        while (true) {

            String value = wait.until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//input[@data-testid='adults-amount']")))
                    .getAttribute("value");

            int current = Integer.parseInt(value);

            if (current == adults) break;

            if (current < adults) {
                WebElement plusBtn = driver.findElement(By.xpath("//button[@data-testid='adults-amount-plus-button']"));
                js.executeScript("arguments[0].click();", plusBtn);
            } else {
                WebElement minusBtn = driver.findElement(By.xpath("//button[@data-testid='adults-amount-minus-button']"));
                js.executeScript("arguments[0].click();", minusBtn);
            }
        }

    }


    public void setRooms(int rooms) throws InterruptedException {
        Thread.sleep(500);
        while (true) {
            String roomData = wait.until(ExpectedConditions.visibilityOf(roomsInput)).getAttribute("value");
            int currRooms = Integer.parseInt(roomData);
            if (rooms == currRooms) {
                System.out.println("room are matched");
                break;
            } else if (rooms > currRooms) {
                roomsPlusBtn.click();
                Thread.sleep(300); // wait for DOM update
            } else {
                roomsMinusBtn.click();
                Thread.sleep(300); // wait for DOM update
            }
        }
    }


    public void clickApply() {
        wait.until(ExpectedConditions.visibilityOf(applyBtn)).click();
    }

    public void clickSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }
}
