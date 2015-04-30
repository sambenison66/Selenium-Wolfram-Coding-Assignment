/*
 * Author: Samuel Benison - samuel.jeyarajvictor@mavs.uta.edu
 * Designed for - Wolfram Coding Assignment
 */

package com.wolfram.factory;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.wolfram.base.BasePage;

public class WolframMainPage extends BasePage {
	
	// These variables are used while switching driver windows
	String handle;
	Set<String> handles;
	
	// Constructor assigns the driver to Base Page
	public WolframMainPage(WebDriver driver){
		super(driver);		
	}

	// Webelements required to perform the automation
	@FindBy(css = ".newNotebookBtn-dropdown") WebElement newDropDown;
	@FindBy(css = "#newFileMenu>#nb") WebElement newNoteBook;
	@FindBy(css = "#header-account") WebElement headerAccount;
	@FindBy(css = "#account-options>#account-signout") WebElement accountSignOut;
	@FindBy(css = "#programming-tile") WebElement homePageButton;
	
	// This Method is used to perform Create NoteBook action
	public void createNewNoteBook() throws Exception {
		
		// Click the New dropdown
		performButtonClickWithException(newDropDown, newNoteBook, "Unable to view dropdown options:");
				
		//For NewNoteBook click, I didn't use the default performButtonClick method
		// Since it switches to new tab, so I am creating a custom action below
		newNoteBook.click();
		
		// Get the details of current driver
		handle = driver.getWindowHandle();
		
		handles = driver.getWindowHandles();
		
		for(String hnd : handles) {
			
			if(!hnd.equals(handle)){
				driver.switchTo().window(hnd);
			}
		}
		
		driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
		// Once the driver is switched, all the ExplicitWait and FindElement variables are ineffective
		// For new driver, new variables are required
		
		// New Window sometimes takes more time to load, so gave the time as 25
		WebDriverWait wait_new = new WebDriverWait(driver, 25);
		WebElement deployButton = driver.findElement(By.cssSelector("#deploy-file"));
		
		// Wait for the page to load before taking any action
		wait_new.until(ExpectedConditions.elementToBeClickable(deployButton));
		
		WebElement editNBName = driver.findElement(By.cssSelector("#renameButton"));

		// Clicking the edit icon
		editNBName.click();

		Assert.assertEquals(driver.getTitle(), "(unnamed) - Wolfram Programming Cloud");
		
	}
	
	// This Method is used to verify the extension of the new NoteBook
	public void verifyNBExtension() throws Exception {
		
		// As mentioned in previous method, new driver requires new FindElements
		WebElement renameNBField = driver.findElement(By.id("toolbarRenameInputField"));
		
		// Checking the extension
		Assert.assertEquals(renameNBField.getAttribute("value"), ".nb");
		
		// Close the new driver window and switch to old driver
		driver.close();
		driver.switchTo().window(handle);
		
	}
	
	// This Method is used to perform Signout action
	public void performSignOut() throws Exception {
		
		// Clicks header option
		performButtonClickWithException(headerAccount, accountSignOut, "Unable to view account options:");
		
		// And selects Signout option
		performButtonClickWithException(accountSignOut, homePageButton, "Unable to perform Signout:");
		
		Assert.assertEquals(driver.getTitle(),"Wolfram Cloud");
		
	}
	
}
