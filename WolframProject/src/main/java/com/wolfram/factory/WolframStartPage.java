/*
 * Author: Samuel Benison - samuel.jeyarajvictor@mavs.uta.edu
 * Designed for - Wolfram Coding Assignment
 */

package com.wolfram.factory;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import com.wolfram.base.BasePage;

/* This is a page which loads as the startup when you navigate to Wolfram Cloud */
public class WolframStartPage extends BasePage {
	
	// Constructor assigns the driver to Base Page
	public WolframStartPage(WebDriver driver){
		super(driver);		
	}
	
	// Webelements required to perform the automation
	@FindBy(css = "#programming-tile") WebElement progCloudButton;
	@FindBy(css = "#signIn") WebElement progLoginButton;
	
	//This is the primary method to load the Wolfram Cloud 
	// It assigns the variable and calls the Base Page for action
	public void loadPage(String url, String title) {
		this.PAGE_URL = url;
		this.PAGE_TITLE = title;
		initiateLoadPage(progCloudButton);
	}
	
	// MEthod to execute when User selects Wolfram PRogramming Cloud option
	public void loadLoginPage() throws Exception{
		
		performButtonClickWithException(progCloudButton, progLoginButton, 
				"Unable to click Programming Cloud button:");
		
		Assert.assertEquals(driver.getTitle(),"Sign In - Wolfram Programming Cloud");
		
	}
	
	//This method is used to go back to home page from Login page
	// Used for Negative Test Cases
	public void navigateBacktoHome() throws Exception {
		
		driver.get(PAGE_URL);
		
		try{
			wait.until(ExpectedConditions.elementToBeClickable(progCloudButton));
		}catch(TimeoutException e){
			Assert.assertTrue(false, "Unable to navigate to home page:");
		}
		
		Assert.assertEquals(driver.getTitle(),PAGE_TITLE);
	}

}
