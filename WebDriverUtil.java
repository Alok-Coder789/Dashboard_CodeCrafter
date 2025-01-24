package com.eva.vtiger.genericMethods;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.google.common.io.Files;
import io.github.bonigarcia.wdm.WebDriverManager;

public class WebDriverUtil {

	private WebDriver driver;
	private ExtentReports extReport;
	private ExtentTest extTest;
	
	public WebDriverUtil(String testCaseName) {
		DateFormat df = new SimpleDateFormat("dd/mm/yyyy___hh:mm:ss");
		df.format(new Date());
		
		ExtentSparkReporter reporter = new ExtentSparkReporter("AutomationReport.html");
		extReport = new ExtentReports();
		extReport.attachReporter(reporter);
		extTest = extReport.createTest(testCaseName);
	}
	
	public void flushReport() {
		extReport.flush();
	}
	
	public void initHtmlReport(String reportPath, String title, String reportName) {

		ExtentSparkReporter report = new ExtentSparkReporter("reports//testReport.html");

		report.config().setDocumentTitle(title);
		report.config().setReportName(reportName);
	}
	
	public WebDriver getDriver() {
		if (driver==null) {
			extTest.log(Status.FAIL, "");
		}
		return driver;
	}
	
	public WebDriver setDriver(WebDriver driver) {
		this.driver=driver;
		return driver;
	}

	public WebDriver launchBrowser(String browserName) {

		if (browserName.equalsIgnoreCase("chrome")) {
			WebDriverManager.chromedriver().setup();
			driver = new ChromeDriver();
			extTest.log(Status.INFO, "Chrome browser is launched successfully");
		} else if (browserName.equalsIgnoreCase("Firefox")) {
			WebDriverManager.firefoxdriver().setup();
			driver = new FirefoxDriver();
			extTest.log(Status.INFO, "FireFox browser is launched successfully");
		} else if (browserName.equalsIgnoreCase("Edge")) {
			WebDriverManager.edgedriver().setup();
			driver = new EdgeDriver();
			extTest.log(Status.INFO, "Edge browser is launched successfully");
		} else if (browserName.equalsIgnoreCase("Safari")) {
			WebDriverManager.safaridriver().setup();
			driver = new SafariDriver();
			extTest.log(Status.INFO, "Safari browser is launched successfully");
		} else {
			extTest.log(Status.FAIL, "Browser is not launched, please check again!!");
		}
		return driver;
	}

	public void openUrl(String url) {

		try {
			driver.get("http://localhost:8888/");
			extTest.log(Status.INFO, "URL (" + url + ") is opened, successfully................");
		} catch (Exception e) {
			extTest.log(Status.FAIL, "Url opening is failed.........");
			takeScreenshot();
			e.printStackTrace();
		}
	}

	public void setImplicitWait(int maxTimeOut) {

		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(maxTimeOut));

	}

	public WebElement getWebElement(String locatorValue, String locatorType, String elementName) {

		WebElement we = null;
		if (locatorType.equalsIgnoreCase("xpath")) {
			we = driver.findElement(By.xpath(locatorValue));
		} else if (locatorType.equalsIgnoreCase("id")) {
			we = driver.findElement(By.id(locatorValue));
		} else if (locatorType.equalsIgnoreCase("name")) {
			we = driver.findElement(By.name(locatorValue));
		} else if (locatorType.equalsIgnoreCase("linkText")) {
			we = driver.findElement(By.linkText(locatorValue));
		} else if (locatorType.equalsIgnoreCase("class")) {
			we = driver.findElement(By.className(locatorValue));
		} else if (locatorType.equalsIgnoreCase("css")) {
			we = driver.findElement(By.cssSelector(locatorValue));
		} else if (locatorType.equalsIgnoreCase("partialLinkText")) {
			we = driver.findElement(By.partialLinkText(locatorValue));
		} else {
			extTest.log(Status.FAIL, elementName + " locator type is wrong, please check......");
		}
		return we;
	}

	public boolean checkElement(WebElement we, String elementName) {

		boolean status = false;
		if (we.isDisplayed() == true) {
			extTest.log(Status.INFO, elementName + " is visible");
			if (we.isEnabled() == true) {
				extTest.log(Status.INFO, elementName + " is enable");
				status = true;
			} else {
				extTest.log(Status.FAIL, elementName + " is disabled");
			}
		} else {
			extTest.log(Status.FAIL, elementName + " is not visible");
		}
		return status;
	}

	public void sendkeys(String locatorValue, String locatorType, String value, String elementName) {

		try {
			WebElement we = getWebElement(locatorValue, locatorType, elementName);
			boolean st = checkElement(we, elementName);
			if (st == true) {
				we.sendKeys(value);
				extTest.log(Status.INFO, value + " is send in " + elementName + ", successfully");
			}
		} catch (Exception e) {
			TakesScreenshot tss = (TakesScreenshot) driver;
			File fileObj = tss.getScreenshotAs(OutputType.FILE);
		}
	}

	public void click(String locatorValue, String locatorType, String elementName) {

		try {
			WebElement we = getWebElement(locatorValue, locatorType, elementName);
			boolean st = checkElement(we, elementName);
			if (st == true) {
				we.click();
				extTest.log(Status.INFO, elementName + " is clicked successfully");
			}
		} catch (Exception e) {
			TakesScreenshot tss = (TakesScreenshot) driver;
			File fileObj = tss.getScreenshotAs(OutputType.FILE);
		}
	}

	public void takeScreenshot() {

		TakesScreenshot tss = (TakesScreenshot) driver;
		File fileFrom = tss.getScreenshotAs(OutputType.FILE);
		File fileTo = new File("C:\\Users\\Admin\\Desktop\\" + "vtiger.png");
		try {
			Files.copy(fileFrom, fileTo);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/********************** Text related methods ************************/

	public String getInnerText(String locatorValue, String locatorType, String elementName) {

		String innerText = null;
		try {
			WebElement we = getWebElement(locatorValue, locatorType, elementName);
			boolean st = checkElement(we, elementName);
			if (st == true) {
				innerText = we.getText();
			}
		} catch (Exception e) {
			takeScreenshot();
			e.printStackTrace();
		}
		return innerText;
	}

	public String getAttribute(String locatorValue, String locatorType, String attributeName, String elementName) {

		String attributeValue = null;
		try {
			WebElement we = getWebElement(locatorValue, locatorType, elementName);
			boolean st = checkElement(we, elementName);
			if (st == true) {
				attributeValue = we.getAttribute(attributeName);
			}
		} catch (Exception e) {
			takeScreenshot();
			e.printStackTrace();
		}
		return attributeValue;
	}

	public String getTitle(String pagename) {

		String title = driver.getTitle();
		System.out.println("Successfully return the page title : " + pagename);
		takeScreenshot();
		return title;
	}

	public int getElementHeight(WebElement we, String elementName) {

		Dimension dimension = we.getSize();
		int heightOfElement = dimension.getHeight();
		return heightOfElement;
	}

	public int getElementWidth(WebElement we, String elementName) {

		Dimension dimension = we.getSize();
		int widthOfElement = dimension.getWidth();
		return widthOfElement;
	}

	/******************* Alert/Popup related methods **********************/

	public void alertAccept() {

		Alert alertAccept = driver.switchTo().alert();
		try {
			String text = alertAccept.getText();
			System.out.println("Alert text is - " + text);
			alertAccept.accept();
			extTest.log(Status.INFO, "Alert is accepted");
		} catch (Exception e) {
			takeScreenshot();
			throw e;
		}
	}

	public void alertDismiss() {

		Alert alertDissmiss = driver.switchTo().alert();
		try {
			String text = alertDissmiss.getText();
			System.out.println("Alert text is - " + text);
			alertDissmiss.dismiss();
			extTest.log(Status.INFO, "Alert is dissmissed");
		} catch (Exception e) {
			takeScreenshot();
			throw e;
		}
	}

	/******************** Drop down related methods **********************/

	public void selectByTextFromDropdown(String locatorValue, String locatorType, String textToSelect, String elementName) {

		try {
			WebElement we = getWebElement(locatorValue, locatorType, elementName);
			boolean st = checkElement(we, elementName);
			if (st == true) {
				Select slt = new Select(we);
				slt.selectByVisibleText(textToSelect);
				extTest.log(Status.INFO, textToSelect + " is selected in " + elementName);
			} else {
				extTest.log(Status.INFO, "Text selection is not selected in " + elementName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			takeScreenshot();
		}
	}

	public void selectByIndexFromDropdown(String locatorValue, String locatorType, int indexToSelect, String elementName) {

		try {
			WebElement we = getWebElement(locatorValue, locatorType, elementName);
			boolean st = checkElement(we, elementName);
			if (st == true) {
				Select slt = new Select(we);
				slt.selectByIndex(indexToSelect);
				extTest.log(Status.INFO, indexToSelect + " is selected in " + elementName);
			} else {
				extTest.log(Status.INFO, "Text selection is not selected in " + elementName);
			}
		} catch (Exception e) {
			takeScreenshot();
		}
	}

	public void selectByValueAttributeFromDropdown(String locatorValue, String locatorType, String valueAttribute, String elementName) {

		try {
			WebElement we = getWebElement(locatorValue, locatorType, elementName);
			boolean st = checkElement(we, elementName);
			if (st == true) {
				Select slt = new Select(we);
				slt.selectByValue(valueAttribute);
				extTest.log(Status.INFO, valueAttribute + " is selected in " + elementName);
			} else {
				extTest.log(Status.INFO, "Text selection is failed in " + elementName);
			}
		} catch (Exception e) {
			takeScreenshot();
			e.printStackTrace();
		}
	}

	public String getSelectTextFromDropdown(String locatorValue, String locatorType, String elementName) {

		String selectedText = null;
		try {
			WebElement we = getWebElement(locatorValue, locatorType, elementName);
			Select slt = new Select(we);
			WebElement weSelectedOption = slt.getFirstSelectedOption();
			selectedText = weSelectedOption.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return selectedText;
	}

	public String getAllOptionsFromDropdown(String locatorValue, String locatorType, String elementName) {

		String allOption = null;
		try {
			WebElement we = getWebElement(locatorValue, locatorType, elementName);
			Select dropdown = new Select(we);
			List<WebElement> options = dropdown.getOptions();
			for (WebElement option : options) {
				allOption = allOption+", " + option.getText();
			}
			extTest.log(Status.INFO, elementName + " is found all options. Where All options are - " + allOption);
		} catch (Exception e) {
			extTest.log(Status.INFO, elementName + " is not found all options, please check.....");
			e.printStackTrace();
		}
		return allOption;
	}

	/******************* Windows related methods ***********************/

	public void switchToWindowByUrl(String windowUrl) {

		Set<String> windowHandles = driver.getWindowHandles();
		for (String windowHandle : windowHandles) {
			driver.switchTo().window(windowHandle);
			String url = driver.getCurrentUrl();
			if (url.contains(windowUrl)) {
				extTest.log(Status.INFO, "Switch to window by url - " + url + ", successfully");
				break;
			}
		}
	}

	public void switchToWindowByTitle(String windowTitle) {

		Set<String> windowHandles = driver.getWindowHandles();
		for (String windowHandle : windowHandles) {
			driver.switchTo().window(windowHandle);
			String title = driver.getTitle();
			if (title.equalsIgnoreCase(windowTitle)) {
				extTest.log(Status.INFO, "Switch to window by title - " + title + ", successfully");
				break;
			}
		}
	}

	public void closeCurrentWindow() {

		driver.close();
		extTest.log(Status.INFO, "Current window is closed successfully");
	}

	public void closeAllWindow() {

		driver.quit();
		extTest.log(Status.INFO, "All windows are closed successfully");
	}

	/********************* iframe related methods *************************/

	public void switchToFrameByIndex(int indexNumber) {

		driver.switchTo().frame(indexNumber);
	}

	public void switchToFrameByFrameWebElement(WebElement we) {

		WebElement weFrame = null;
		;
		try {
			driver.switchTo().frame(weFrame);
		} catch (Exception e) {
			takeScreenshot();
			throw e;
		}
	}

	public void switchToFrameByName(WebElement we) {

		WebElement weFrameName = null;
		;
		try {
			driver.switchTo().frame(weFrameName);
		} catch (Exception e) {
			takeScreenshot();
			throw e;
		}
	}

	public void switchToFrameById(WebElement we) {

		WebElement weFrameId = null;
		try {
			driver.switchTo().frame(weFrameId);
		} catch (Exception e) {
			takeScreenshot();
			throw e;
		}
	}

	public void switchToParentFrame() {

		driver.switchTo().parentFrame();
	}

	/***************************** Actions related methods	 *****************************/

	public void actionMouseover(String locatorValue, String locatorType, String elementName) {

		Actions act = new Actions(driver);
		try {
			WebElement we=getWebElement(locatorValue, locatorType, elementName);
			act.moveToElement(we).build().perform();
            extTest.log(Status.INFO, "Mouse hover performed on "+elementName+" successfully");
		} catch (ElementNotInteractableException e) {
            extTest.log(Status.INFO, "Mouse hover is not performed on "+elementName+", because ElementNotInteractableException occured and try with javascript");
			JavascriptExecutor js = (JavascriptExecutor) driver;
//			js.executeScript("mouseover code by javascript", we);
		} catch (Exception e) {
			e.printStackTrace();
			takeScreenshot();
			throw e;
		}
	}

	public void actionDragAndDrop(WebElement weDrag, WebElement weDrop, String dragElement, String dropElement) {

		Actions act = new Actions(driver);
		try {
			act.dragAndDrop(weDrag, weDrop).build().perform();
			System.out.println(dragElement + " is replaced to the place on " + dropElement + " successfully");
		} catch (StaleElementReferenceException e) {
			act.dragAndDrop(weDrag, weDrop).build().perform();
			System.out.println(dragElement + " is replaced to the place on " + dropElement + " successfully");
		} catch (ElementNotInteractableException e) {
			System.out.println("Element is hidden, trying javascript");
			JavascriptExecutor js = (JavascriptExecutor) driver;
		} catch (Exception e) {
			System.out.println("Due to Exception " + dragElement + " and " + dropElement + ", action is not performed");
			takeScreenshot();
			throw e;
		}
	}

	public void actionClick(WebElement we, String elementName) {

		Actions act = new Actions(driver);
		try {
			act.click(we).build().perform();
			System.out.println(elementName + " is clicked successfully");
		} catch (ElementNotInteractableException e) {
			System.out.println("Element is hidden, trying javascript");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", we);
		} catch (Exception e) {
			System.out.println("Exception occured which is not handled");
			takeScreenshot();
			throw e;
		}
	}

	public void actionRightClick(WebElement we, String elementName) {

		Actions act = new Actions(driver);
		try {
			act.contextClick(we).build().perform();
			System.out.println("Right click is performed on " + elementName + " successfully");
		} catch (ElementNotInteractableException e) {
			System.out.println("Element is hidden, trying javascript");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("Right click code in javascript", we);
		} catch (Exception e) {
			System.out.println("Exception occured which is not handled");
			takeScreenshot();
			throw e;
		}
	}

	public void actionDoubleClick(WebElement we, String elementName) {

		Actions act = new Actions(driver);
		try {
			act.doubleClick(we).build().perform();
			System.out.println("Double click is performed on " + elementName + " successfully");
		} catch (ElementNotInteractableException e) {
			System.out.println("Element is hidden, trying javascript");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("Double click code in javascript", we);
		} catch (Exception e) {
			System.out.println("Exception occured which is not handled");
			takeScreenshot();
			throw e;
		}
	}

	public void actionSendkeys(String locatorValue, String locatorType, String value, String elementName) {

		Actions act = new Actions(driver);
		try {
			WebElement we=getWebElement(locatorValue, locatorType, elementName);
			act.sendKeys(we, value).build().perform();
            extTest.log(Status.INFO, value+" is send in "+elementName+" successfully");
		} catch (ElementNotInteractableException e) {
			extTest.log(Status.INFO, "Element is hidden, trying javascript");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("Actions Sendkeys code in javascript");
		} catch (Exception e) {
			extTest.log(Status.INFO, "Exception occured which is not handled");
			takeScreenshot();
			throw e;
		}
	}

	/************************ Wait related methods *****************************/

	public boolean waitForElementVisibility(WebElement we) {

		boolean findStatus = false;
		try {
			WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(100));
			wt.until(ExpectedConditions.invisibilityOf(we));
		} catch (TimeoutException e) {
			takeScreenshot();
			e.printStackTrace();
			findStatus = false;
		}
		return findStatus;
	}

	public boolean waitForElementInvisibility(WebElement we) {

		boolean findStatus = true;
		try {
			WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(100));
			wt.until(ExpectedConditions.invisibilityOf(we));
		} catch (TimeoutException e) {
			takeScreenshot();
			e.printStackTrace();
			findStatus = false;
		}
		return findStatus;
	}

	public void waitForElementEnabled(WebElement we) {

		try {
			WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(100));
			wt.until(ExpectedConditions.elementToBeClickable(we));
		} catch (TimeoutException e) {
			takeScreenshot();
			e.printStackTrace();
		}
	}

	public void waitForElementDisabled(WebElement we) {

		try {
			WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(100));
			wt.until(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(we)));
		} catch (TimeoutException e) {
			takeScreenshot();
			e.printStackTrace();
		}
	}

	public void waitForTextInElement(WebElement we, String textToWait) {

		try {
			WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(100));
			wt.until(ExpectedConditions.textToBePresentInElement(we, textToWait));
		} catch (TimeoutException e) {
			takeScreenshot();
			e.printStackTrace();
		}
	}

	public void waitForElementPresenceInHtmlDOM(WebElement we) {

		try {
			WebDriverWait wt = new WebDriverWait(driver, Duration.ofSeconds(100));
			wt.until(ExpectedConditions.presenceOfElementLocated(null));
		} catch (TimeoutException e) {
			takeScreenshot();
			e.printStackTrace();
		}
	}

	/**************************** Java script related methods	 *******************************/

	public void jsSendkeys(WebElement we, String value) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].value='" + value + "'", we);
	}

	public void jsClick(WebDriver driver, WebElement we) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", we);
	}

	public void jsScrollVertically(int scrollAmount) {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0, " + scrollAmount + ")");
	}

	public void jsScrollToBottum() {

		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.body.scrollHeight");
	}

	/****************************** Validation or Testing related methods	 *****************************/

	public void validateInnerText(String locatorValue, String locatorType, String expectedValue, String elementName) {

		String actualValue = getInnerText(locatorValue, locatorType, elementName);
		if (actualValue.equalsIgnoreCase(expectedValue)) {
			extTest.log(Status.PASS, elementName + " validation Passed. Where actual - " + actualValue + ", expected - "+ expectedValue);
		} else {
			extTest.log(Status.FAIL, elementName + " validation Failed. Where actual - " + actualValue + ", expected - "+ expectedValue);
		}
	}

	public void validateAttributeValue(String locatorValue, String locatorType, String attributeName, String expectedText, String elementName) {

		String actualText = getAttribute(locatorValue, locatorType, attributeName, elementName);
		if (actualText.equalsIgnoreCase(expectedText)) {
			extTest.log(Status.PASS, elementName + " attribute validation passed. Where actual - " + actualText+ ", expected - " + expectedText);
		} else {
			extTest.log(Status.FAIL, elementName + " attribute validation failed. Where actual - " + actualText+ ", expected - " + expectedText);
		}
	}

	public void validatePageTitle(String locatorValue, String locatorType, String expectedTitle, String pageName) {

		String actualTitle = driver.getTitle();
		if (actualTitle.equals(expectedTitle)) {
			extTest.log(Status.PASS, pageName + " title validation passed. Where actual - " + actualTitle+ ", expected - " + expectedTitle);
		} else {
			extTest.log(Status.FAIL, pageName + " title validation failed. Where actual - " + actualTitle+ ", expected - " + expectedTitle);
		}
	}

	public void validatePageURL(String expectedURL, String pageName) {

		String actualURL = driver.getCurrentUrl();
		if (actualURL.equals(expectedURL)) {
			extTest.log(Status.PASS,
					pageName + " url validation passed. Where actual - " + actualURL + ", expected - " + expectedURL);
		} else {
			extTest.log(Status.FAIL,
					pageName + " url validation failed. Where actual - " + actualURL + ", expected - " + expectedURL);
		}
	}

	public void validateDropdownSelectedText(String locatorValue, String locatorType, String expectedSelectedText,
			String elementName) {

		String actualSelectedText = getInnerText(locatorValue, locatorType, elementName);
		if (actualSelectedText.equalsIgnoreCase(expectedSelectedText)) {
			extTest.log(Status.PASS, elementName + " dropdown selected text validation passed. Where actual - "+ actualSelectedText + ", expected - " + expectedSelectedText);
		} else {
			extTest.log(Status.FAIL, elementName + " dropdown selected text validation failed. Where actual - "+ actualSelectedText + ", expected - " + expectedSelectedText);
		}
	}

	public void validateDropdownOption(WebElement we, int expectedOptionCount, String elementName) {

		Select sltObj = new Select(we);
		int actualOptionCount = sltObj.getOptions().size();
		if (actualOptionCount == expectedOptionCount) {
			extTest.log(Status.PASS, elementName + " dropdown option count validation passed. Where actual - "+ actualOptionCount + ", expected - " + expectedOptionCount);
		} else {
			extTest.log(Status.FAIL, elementName + " dropdown option count validation failed. Where actual - "+ actualOptionCount + ", expected - " + expectedOptionCount);
		}
	}

	public void validateElementIsEnable(String locatorValue, String locatorType, String elementName) {

		WebElement we = getWebElement(locatorValue, locatorType, elementName);
		boolean status = we.isEnabled();
		if (status == true) {
			extTest.log(Status.PASS, elementName + " is Enabled");
		} else {
			extTest.log(Status.FAIL, elementName + " is Disabled");
		}
	}

	public void validateElementIsDisable(String locatorValue, String locatorType, String elementName) {

		WebElement we = getWebElement(locatorValue, locatorType, elementName);
		boolean elementStatus = we.isEnabled();
		if (elementStatus == false) {
			extTest.log(Status.PASS, elementName + " is Disable.");
		} else {
			extTest.log(Status.FAIL, elementName + " is Enable");
		}
	}

	public void validateElementIsVisible(String locatorValue, String locatorType, String elementName) {

		WebElement we = getWebElement(locatorValue, locatorType, elementName);
		boolean status = we.isDisplayed();
		if (status == true) {
			extTest.log(Status.PASS, elementName + " is visible");
		} else {
			extTest.log(Status.FAIL, elementName + " is not visible");
		}
	}
	public void validateElementIsInvisible(String locatorValue, String locatorType, String elementName) {

		WebElement we = getWebElement(locatorValue, locatorType, elementName);
		boolean status = we.isDisplayed();
		if (status == false) {
			extTest.log(Status.PASS, elementName + " is not visible");
		} else {
			extTest.log(Status.FAIL, elementName + " is visible");
		}
	}
	public void validateCheckBoxChecked(String locatorValue, String locatorType, String elementName) {

		WebElement we = getWebElement(locatorValue, locatorType, elementName);
		boolean elementStatus = we.isSelected();
		if (elementStatus == true) {
			extTest.log(Status.PASS, elementName + " is Checked");
		} else {
			extTest.log(Status.FAIL, elementName + " is Unchecked");
		}
	}

	public void validateCheckBoxUnchecked(WebElement we, String elementName) {

		boolean elementStatus = we.isSelected();
		if (elementStatus == false) {
			extTest.log(Status.PASS, elementName + " is Unchecked");
		} else {
			extTest.log(Status.FAIL, elementName + " is Checked");
		}
	}

}
