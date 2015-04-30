/*
 * Author: Samuel Benison - samuel.jeyarajvictor@mavs.uta.edu
 * Designed for - Wolfram Coding Assignment
 */

package com.wolfram.base;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

// BasePage methos contains all the common variables and methods
// The purpose of this methos is to reuse the function calls and avoid redundant codes
public class BasePage {
	
	public WebDriver driver;
	public WebDriverWait wait;
	
	public String PAGE_URL;
	public String PAGE_TITLE;
	
	public Boolean[] testSheet_Row;
	
	// Constructor Method
	protected BasePage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, 15);
	}
	
	// Method to initiate the Page Load action
	protected void initiateLoadPage(WebElement pageButton) {
		driver.get(PAGE_URL);
		wait.until(ExpectedConditions.elementToBeClickable(pageButton));
		Assert.assertEquals(driver.getTitle(),PAGE_TITLE);
	}
	
	// Method to fill the Web Element textfield with value
	protected void loadField(WebElement targetField, String content) {
		targetField.sendKeys(content);
		Assert.assertEquals(targetField.getAttribute("value"),content);
	}
	
	// Method to perform action click
	protected void performButtonClick(WebElement actionButton, WebElement loadElement) throws Exception {
		actionButton.click();
		wait.until(ExpectedConditions.elementToBeClickable(loadElement));
	}
	
	// Method to perform action click and handle the exceptions
	protected void performButtonClickWithException(WebElement actionButton, WebElement loadElement,
			String assertMsg) throws Exception {
		
		try {
			performButtonClick(actionButton, loadElement);
		} catch(TimeoutException e){
			// It will generate a more reasonable failure report rather than TimeOut Exception
			Assert.assertTrue(false, assertMsg);
		}
	}
	
	// Basic Error message method to compare the WebElement text to the passed message
	protected void verifyErrorMessage(WebElement errorLabel, String errorMsg) {
		
		try {
			String content = errorLabel.getText();
			Assert.assertEquals(content, errorMsg, "Error Message not matched:");
			
		} catch(NoSuchElementException e) {
			
			Assert.assertEquals("", errorMsg, "Unable to find the Error Message:");
		}
	}
	
	// Method to convert the String array to boolean array
	protected void convertStringToBool(String[] stringRow) throws Exception {
		
		int colCount = stringRow.length;
		testSheet_Row = new Boolean[colCount];
		
		for(int i=0; i<colCount; i++){
			
			if(stringRow[i].toLowerCase().equals("false")){
				testSheet_Row[i] = false;
			} else {
				testSheet_Row[i] = true;
			}
		}
	}
	
	// Method to look for 'false' in the boolean array
	protected Boolean lookForNegativeTC() {
		
		for(int i=0; i<testSheet_Row.length; i++){
			if(!testSheet_Row[i]){
				return false;
			}
		}
		
		return true;
	}
	
	// An advanced method to validate Error Message against the testing type
	protected void checkError(WebElement errorElement, String expError, Boolean testType) throws Exception {
		
		if(testType == false) {
			
			try {
				String msg = errorElement.getText();
				Assert.assertEquals(msg, expError);
			} catch(NoSuchElementException e) {
				Assert.assertFalse(true, "Error Msg Expected but found none :");
			}
			
		} else {
			
			try {
				String msg = errorElement.getText();
				Assert.assertTrue(false, "No Error Msg Expected but found [" + msg + "] :");
			} catch(NoSuchElementException e) {
				Assert.assertTrue(true, "No error msg as expected");
			}
		}
	}
	
	// This method is used to validate password which has more than one type of error messages
	protected void checkError(WebElement errorElement, String expError, Boolean testType1, Boolean testType2) throws Exception {
		
		if(testType1 == false) {
			
			try {
				String msg = errorElement.getText();
				Assert.assertEquals(msg, expError, "Error message contradicts with expected error:");
			} catch(NoSuchElementException e) {
				Assert.assertFalse(true, "Error Msg Expected but found none :");
			}
			
		} else {
			
			try {
				String msg = errorElement.getText();
				if(testType2 == true) {
					Assert.assertTrue(false, "No Error Msg Expected but found [" + msg + "] :");
				}
			} catch(NoSuchElementException e) {
				Assert.assertTrue(true, "No error msg as expected");
			}
		}
	}

}
