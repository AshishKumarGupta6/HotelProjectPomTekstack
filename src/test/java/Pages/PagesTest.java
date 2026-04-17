package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PagesTest {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    @FindBy(xpath = "//button[@name='sorting_selector']")
    WebElement sortButton;

    @FindBy(xpath = "//input[@data-testid='sorting-index-3']")
    WebElement ratingOption;

    @FindBy(xpath = "//button[@data-testid='filters-popover-apply-button']")
    WebElement applyFilterBtn;

    public PagesTest(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    public void sortByTopGuestRatings() {
        WebElement sortBtn = wait.until(ExpectedConditions.elementToBeClickable(sortButton));
        js.executeScript("arguments[0].click();", sortBtn);

        WebElement ratingOpt = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@data-testid='sorting-index-3']")));
        js.executeScript("arguments[0].click();", ratingOpt);

        WebElement applyBtn = wait.until(ExpectedConditions.elementToBeClickable(applyFilterBtn));
        js.executeScript("arguments[0].click();", applyBtn);

        // Wait for sort popover to close
        wait.until(ExpectedConditions.invisibilityOf(applyBtn));
    }

    public void waitForResults() throws InterruptedException {
        Thread.sleep(3000);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//span[contains(@itemprop,'ratingValue')]")));
    }

    public List<Double> getRatings() {
        List<WebElement> elements = wait.until(ExpectedConditions
                .visibilityOfAllElementsLocatedBy(By.xpath("//span[contains(@itemprop,'ratingValue')]")));

        List<Double> ratings = new ArrayList<>();

        for (WebElement e : elements) {
            String text = e.getText().trim();
            if (!text.isEmpty()) {
                try {
                    ratings.add(Double.parseDouble(text));
                } catch (NumberFormatException ex) {
                    // skip non-numeric ratings
                }
            }
        }

        return ratings;
    }

    public boolean isSorted(List<Double> list) {
        List<Double> sorted = new ArrayList<>(list);
        sorted.sort(Collections.reverseOrder());
        return list.equals(sorted);
    }

    public String[][] getAllDataHotels() {
        List<WebElement> names = driver.findElements(By.xpath("//span[@itemprop='name']"));
        List<WebElement> ratingsEl = driver.findElements(By.xpath("//span[@itemprop='ratingValue']"));
        List<WebElement> prices = driver.findElements(By.xpath("//div[@itemprop='price']"));

        int count = Math.min(5, Math.min(names.size(), Math.min(ratingsEl.size(), prices.size())));
        String[][] result = new String[count][3];

        for (int i = 0; i < count; i++) {
            result[i][0] = names.get(i).getText();
            result[i][1] = ratingsEl.get(i).getText();
            result[i][2] = prices.get(i).getText();
        }
        return result;
    }
}
