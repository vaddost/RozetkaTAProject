package tests;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CartPageTests {

    static final long DEFAULT_TIMEOUT = 30;
    static final String BASE_PATH = "https://rozetka.com.ua/";

    @Test
    public void checkUserCanAddProductsToCart(){

        //Header locators
        String cartCounterXpath = "//button[@opencart]/descendant::span[contains(@class, 'counter')]";
        String logoLinkCSS = "a.header__logo";
        String menuButtonId = "fat-menu";
        String gameCategoryMenuLinkCSS = "fat-menu a.menu-categories__link[href*='game-zone']";
        String xboxLinkXpath = "//div[@class='menu__main-cats']//a[contains(text(), 'Xbox')]";
        String cartButtonCSS = "rz-cart > button";

        //Home page locators
        String categoriesMenuCSS = "ul.menu-categories_type_main";
        String firstCategoryLinkCSS = ":scope > li:first-child a";
        String phonesCategoryLinkCSS = ":scope > li a[href*='telefony-tv-i-ehlektronika']";

        //CLP locators
        String notebooksTileLinkXpath = "//div[contains(@class, 'tile-cats')]/a[contains(@href, '/notebooks/') " +
                "and contains(@class, 'tile-cats__heading')]";
        String xiaomiPhonesBrandXpath = "//a[contains(@class, 'portal-brands') and contains(@href, 'xiaomi')]";

        //PLP locators
        String sellStatusFilterXpath =
                "//div[@data-filter-name='sell_status']//a[contains(@href, 'sell_status=available')]/input";
        String catalogSelectionLinkCSS = "a.catalog-selection__link";
        String sortDropdownSelectClass = "select-css";
        String productTileBuyButtonCSS = "ul.catalog-grid > li:first-child button.buy-button > svg";
        String mobilePhonesFilterLinkXpath =
                "//a[contains(@class, 'categories-filter__link') and contains(@href, 'mobile-phones')]";
        String readyForShippingFilterInputXpath = "//div[@data-filter-name='gotovo-k-otpravke']//input";
        String firstProductTileTitleLinkCSS = "ul.catalog-grid > li:first-child a.goods-tile__heading";
        String addToCartNotificationCSS = "div.notification__wrapper";
        String cartModalCSS = "div.modal__holder";
        String closeButtonModalCSS = ":scope button.modal__close";

        //PLP variables
        String brandFilterId = "Acer";
        String sortOptionValue = "2: expensive";

        //PDP locators
        String productPageBuyButtonCSS = "app-buy-button > button.button_with_icon";

        //Cart modal locators
        String cartItemsCSS = "ul.cart-list > li";
        String cartSumPriceXpath =
                "//div[@class='cart-receipt__sum-price']/span[contains(@class, 'currency')]/preceding-sibling::span";

        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.get(BASE_PATH);

        try {
            WebElement categoryMenu = waitUntilElementIsVisible(driver, By.cssSelector(categoriesMenuCSS));
            categoryMenu.findElement(By.cssSelector(firstCategoryLinkCSS)).click();

            WebElement notebooksTileLink = waitUntilElementIsVisible(driver, By.xpath(notebooksTileLinkXpath));
            Actions actions = new Actions(driver);
            actions.moveToElement(notebooksTileLink).build();
            notebooksTileLink.click();

            WebElement brandFilter = waitUntilElementIsVisible(driver, By.id(brandFilterId));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", brandFilter);

            WebElement catalogSelectionLink = waitUntilElementIsVisible(
                    driver, By.cssSelector(catalogSelectionLinkCSS));
            assertEquals(catalogSelectionLink.getText(), brandFilterId,
                    "The selection link text is not " + brandFilterId);

            Select sortDropdownSelect = new Select(driver.findElement(By.className(sortDropdownSelectClass)));
            sortDropdownSelect.selectByValue(sortOptionValue);
            assertEquals(
                    sortDropdownSelect.getFirstSelectedOption().getAttribute("value"),
                    sortOptionValue
            );

            ((JavascriptExecutor) driver).executeScript("arguments[0].click()",
                    driver.findElement(By.xpath(sellStatusFilterXpath)));

            WebElement buyButton = waitUntilElementIsClickable(
                    driver,
                    By.cssSelector(productTileBuyButtonCSS)
            );
            actions = new Actions(driver);
            actions.moveToElement(buyButton).perform();
            buyButton.click();

            WebElement cartCounter = waitUntilElementIsVisible(
                    driver,
                    By.xpath(cartCounterXpath)
            );

            assertEquals(Integer.parseInt(cartCounter.getText()), 1);

            waitUntilElementIsNotVisible(driver, driver.findElement(By.cssSelector(addToCartNotificationCSS)));

            driver.findElement(By.cssSelector(logoLinkCSS)).click();

            categoryMenu = waitUntilElementIsVisible(driver, By.cssSelector(categoriesMenuCSS));
            WebElement phonesCategoryLink = categoryMenu.findElement(By.cssSelector(phonesCategoryLinkCSS));
            ((JavascriptExecutor) driver).executeScript(
                    "window.scroll(" + (-phonesCategoryLink.getLocation().x - 100) + ","
                            + (-phonesCategoryLink.getLocation().y - 100) + ");"
            );
            phonesCategoryLink.click();

            waitUntilElementIsVisible(driver, By.xpath(xiaomiPhonesBrandXpath)).click();

            waitUntilElementIsVisible(driver, By.xpath(mobilePhonesFilterLinkXpath)).click();
            ((JavascriptExecutor) driver).executeScript("arguments[0].click()",
                    waitUntilElementIsVisible(driver, By.xpath(readyForShippingFilterInputXpath)));
            waitUntilElementIsClickable(driver, By.cssSelector(firstProductTileTitleLinkCSS)).click();

            waitUntilElementIsVisible(driver, By.cssSelector(productPageBuyButtonCSS)).click();
            WebElement cartModal = waitUntilElementIsVisible(driver, By.cssSelector(cartModalCSS));
            cartModal.findElement(By.cssSelector(closeButtonModalCSS)).click();
            waitUntilElementIsNotVisible(driver, cartModal);
            cartCounter =driver.findElement(By.xpath(cartCounterXpath));
            assertEquals(Integer.parseInt(cartCounter.getText()), 2);
            driver.findElement(By.cssSelector(logoLinkCSS)).click();

            waitUntilElementIsVisible(driver, By.id(menuButtonId)).click();
            actions = new Actions(driver);
            actions.moveToElement(
                    waitUntilElementIsVisible(driver, By.cssSelector(gameCategoryMenuLinkCSS))
            ).perform();

            WebElement xboxLink = waitUntilElementIsVisible(driver, By.xpath(xboxLinkXpath));
            actions.moveToElement(xboxLink).perform();
            xboxLink.click();

            ((JavascriptExecutor) driver).executeScript("arguments[0].click()",
                    waitUntilElementIsVisible(driver, By.xpath(sellStatusFilterXpath)));
            waitUntilElementIsVisible(driver, By.cssSelector(productTileBuyButtonCSS)).click();
            driver.findElement(By.cssSelector(cartButtonCSS)).click();

            waitUntilElementIsVisible(driver, By.cssSelector(cartModalCSS));

            assertEquals(driver.findElements(By.cssSelector(cartItemsCSS)).size(), 3);
            assertTrue(Integer.parseInt(driver.findElement(By.xpath(cartSumPriceXpath)).getText()) > 40000);



        } finally {
            driver.close();
        }
    }

    WebElement waitUntilElementIsClickable(WebDriver driver, By locator){
        Wait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .pollingEvery(Duration.ofSeconds(5))
                .ignoring(StaleElementReferenceException.class);
        return wait.until(
                ExpectedConditions.elementToBeClickable(driver.findElement(locator))
        );
    }

    WebElement waitUntilElementIsVisible(WebDriver driver, By locator){
        return new WebDriverWait(driver, DEFAULT_TIMEOUT).until(
                ExpectedConditions.presenceOfElementLocated(locator)
        );
    }

    void waitUntilElementIsNotVisible(WebDriver driver, WebElement element){
        new WebDriverWait(driver, DEFAULT_TIMEOUT).until(
                ExpectedConditions.invisibilityOf(element)
        );
    }
}
