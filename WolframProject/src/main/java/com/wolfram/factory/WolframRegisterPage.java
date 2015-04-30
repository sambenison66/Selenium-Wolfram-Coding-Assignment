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
import com.wolfram.data.WolframSignUpData;
import com.wolfram.util.ExcelUtils;

public class WolframRegisterPage extends BasePage {

	// Constructor assigns the driver to Base Page
	public WolframRegisterPage(WebDriver driver){
		super(driver);		
	}
	
	// Webelements required to perform the automation
	@FindBy(css = "#email") WebElement progEmailField;
	@FindBy(css = "#firstname") WebElement progFnameField;
	@FindBy(css = "#lastname") WebElement progLnameField;
	@FindBy(css = "#password") WebElement progPswdField;
	@FindBy(css = "#password2") WebElement progRepPswdField;
	@FindBy(css = "#signIn") WebElement progSignupButton;
	@FindBy(css = "#header-account") WebElement nextPageElement;
	
	//Webelements of all the error message
	@FindBy(css = "#email+div.error") WebElement errorEmail;
	@FindBy(css = "#firstname+div.error") WebElement errorFname;
	@FindBy(css = "#lastname+div.error") WebElement errorLname;
	@FindBy(css = "#password+div.error") WebElement errorPwd;
	@FindBy(css = "#password2+div.error") WebElement errorRepPwd;
	@FindBy(css = "#errors") WebElement errorAlreadyExists;
	
	
	//This method is to send given data to Signup fields
	public void loadSignupFields(String id, String fname, String lname, 
									String pswd, String repeatPswd) throws Exception {
		loadField(progEmailField,id);
		loadField(progFnameField,fname);
		loadField(progLnameField,lname);
		loadField(progPswdField,pswd);
		loadField(progRepPswdField,repeatPswd);
	}
	
	// This method is to perform signup action (a little tricky method)
	// It had a conditional branch, i.e., positive and negative will have different actions
	// It is decided by the parameter testObjective
	public Boolean performRegister(Boolean testObjective) throws Exception {
		
		if(!testObjective) {
			performButtonClickWithException(progSignupButton,progSignupButton, "Negative Test Case for Register Failed:");
			// The below method call validate all the error messages applicable
			// Any mismatch to the expected result will throw AssertionError
			Boolean valError = validateErrorMsgs();
			return valError;
		} else {
			
			try{
				performButtonClick(progSignupButton,nextPageElement);
				return true;
			}catch(TimeoutException e){
				// It will generate a more reasonable failure report rather than TimeOut Exception
				Assert.assertTrue(!testObjective, "Positive Test Case for Register Failed:");
				return false;
			}
		}
		
	}
	
	// This method is to read TestSheet for the particular row of data and return the result
	public boolean getSignUpTestSheet(int rowNo) throws Exception {
		
		// Get the TestSheet row from the Excel Sheet
		String[] row = ExcelUtils.getRowContent(WolframSignUpData.getFilePath(), "TestSheet", rowNo);
		
		// Convert the array into boolean array
		convertStringToBool(row);
		
		// Look for a 'false' in the boolean array
		// If False is present in array, then it is Negative TestCase, else Positive
		Boolean testObjective = lookForNegativeTC();
		
		return testObjective;
	}
	
	// This method validates all the errors possible during the Signup process
	public Boolean validateErrorMsgs() throws Exception {
		
		//testSheet_Row[0] = Valid Email
		checkError(errorEmail, "Please enter a valid email address in the format someone@example.com.", testSheet_Row[0]);
		
		//testSheet_Row[1] = Valid Fname
		checkError(errorFname, "Please enter your first name.", testSheet_Row[1]);
		
		//testSheet_Row[2] = Valid Lname
		checkError(errorLname, "Please enter your last name.", testSheet_Row[2]);
		
		//testSheet_Row[3] = Password: Not Blank
		checkError(errorPwd, "Passwords do not match; please enter a password and re-enter to confirm.\n"
				+ "Passwords must contain at least 6 characters and no spaces.", testSheet_Row[3], testSheet_Row[5]);
		
		//testSheet_Row[5] = Password: Atleast 6 characters and no spaces
		checkError(errorPwd, "Passwords must contain at least 6 characters and no spaces.", testSheet_Row[5], testSheet_Row[3]);
		
		if(testSheet_Row[3]) {
			
			//testSheet_Row[4] = Re-enter Password: Not Blank
			checkError(errorRepPwd, "Passwords do not match", testSheet_Row[4], testSheet_Row[6]);
			
			//testSheet_Row[6] = Re-enter Password: Same as Password
			checkError(errorRepPwd, "Passwords do not match", testSheet_Row[6], testSheet_Row[4]);
		}
		
		checkIfAlreadyRegistered();
		
		return true;
	}
	
	// This method is to validate if all the given datas are valid but already exists in the database
	public void checkIfAlreadyRegistered() throws Exception {
		
		if(testSheet_Row[0] && testSheet_Row[1] && testSheet_Row[2] && testSheet_Row[3] &&
				testSheet_Row[4] && testSheet_Row[5] && testSheet_Row[6]) {
			
			if(testSheet_Row[7] == false) {
				
				String msg = errorAlreadyExists.getText();
				
				Assert.assertEquals(msg, "Email already registered, please choose another.", "Validation failed for existing email register:");
			}
		}
			
	}
}
