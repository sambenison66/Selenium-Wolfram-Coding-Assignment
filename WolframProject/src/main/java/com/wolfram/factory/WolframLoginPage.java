/*
 * Author: Samuel Benison - samuel.jeyarajvictor@mavs.uta.edu
 * Designed for - Wolfram Coding Assignment
 */

package com.wolfram.factory;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.wolfram.base.BasePage;
import com.wolfram.data.WolframSignInData;
import com.wolfram.util.ExcelUtils;

public class WolframLoginPage extends BasePage {
	
		// Constructor assigns the driver to Base Page
		public WolframLoginPage(WebDriver driver){
			super(driver);		
		}
		
		// Webelements required to perform the automation
		@FindBy(css = "#email") WebElement progEmailField;
		@FindBy(css = "#password") WebElement progPswdField;
		@FindBy(css = "#signIn") WebElement progLoginButton;
		@FindBy(css = "#createAccount") WebElement createAccountButton;
		@FindBy(css = ".error") WebElement loginErrorMsg;
		@FindBy(css = ".newNotebookBtn-dropdown") WebElement nextPageElement;
		@FindBy(css = "#signIn") WebElement signupPageElement;
		
		//This method is to send given data to Login fields
		public void loadLoginFields(String username, String password) {
			loadField(progEmailField,username);
			loadField(progPswdField,password);	
		}
		
		// This method is to perform login action (a little tricky method)
		// It had a conditional branch, i.e., positive and negative will have different actions
		// It is decided by the parameter testObjective
		public Boolean performLogin(Boolean testObjective) throws Exception {
			
			if(!testObjective) {
				try{
					performButtonClick(progLoginButton,progLoginButton);
					
					verifyErrorMessage(loginErrorMsg, "Password or Wolfram ID entered was not correct.");
				} catch(TimeoutException e){
					// It will generate a more reasonable failure report rather than TimeOut Exception
					Assert.assertFalse(!testObjective, "Negative Test Case for Login:");
					return false;
				}
				
				
			} else {
				
				try{
					performButtonClick(progLoginButton,nextPageElement);
				}catch(TimeoutException e){
					// It will generate a more reasonable failure report rather than TimeOut Exception
					Assert.assertTrue(!testObjective, "Positive Test Case for Login:");
					return false;
				}
			}
			
			return true;
			
		}
		
		// This method is to read TestSheet for the particular row of data and return the result
		public boolean getSignInTestSheet(int rowNo) throws Exception {
			
			// Get the TestSheet row from the Excel Sheet
			String[] row = ExcelUtils.getRowContent(WolframSignInData.getFilePath(), "TestSheet", rowNo);
			
			// Convert the array into boolean array
			convertStringToBool(row);
			
			// Look for a 'false' in the boolean array
			// If False is present in array, then it is Negative TestCase, else Positive
			Boolean testObjective = lookForNegativeTC();
			
			return testObjective;
		}
		
		
		// This method is to navigate to sign up page from login page
		public void navigateToSignUpPage() throws Exception {
			
			performButtonClickWithException(createAccountButton, signupPageElement, "Unable to load Signup Page");
			
			Assert.assertEquals(driver.getTitle(), "Sign Up - Wolfram Programming Cloud");
			
		}

}
