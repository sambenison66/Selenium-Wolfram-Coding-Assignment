/*
 * Author: Samuel Benison - samuel.jeyarajvictor@mavs.uta.edu
 * Designed for - Wolfram Coding Assignment
 */

package com.wolfram.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

import com.wolfram.data.WolframSignUpData;
import com.wolfram.factory.WolframLoginPage;
import com.wolfram.factory.WolframMainPage;
import com.wolfram.factory.WolframRegisterPage;
import com.wolfram.factory.WolframStartPage;

public class SignUp {
	  
	WebDriver driver;
	WolframStartPage wfStartPage;
	WolframLoginPage wfLoginPage;
	WolframMainPage wfMainPage;
	WolframRegisterPage wfRegisterPage;
		
  // Initialize Driver and Page Factory
  @BeforeClass(alwaysRun = true)
  public void initiateDriver() {
	  try{
		  driver = new FirefoxDriver();
		  driver.manage().deleteAllCookies();
		  driver.manage().window().maximize();
	  } catch (Exception e) {
		  System.out.println("Browser Not Working");
		  e.printStackTrace();
	  }
	  wfStartPage = PageFactory.initElements(driver, WolframStartPage.class );
	  wfLoginPage = PageFactory.initElements(driver, WolframLoginPage.class );
	  wfMainPage = PageFactory.initElements(driver, WolframMainPage.class );
	  wfRegisterPage = PageFactory.initElements(driver, WolframRegisterPage.class );
  }

  // Release the memory and close browser once everything is done 
  @AfterClass(alwaysRun = true)
  public void destroyDriver() {
	  this.driver.quit();
	  this.driver = null;
	  wfStartPage = null;
	  wfLoginPage = null;
	  wfMainPage = null;
	  wfRegisterPage = null;
  }
  
  // Load Wolfram Cloud page
  @Test(priority=1)
  public void loadPage() {
	  wfStartPage.loadPage("https://www.wolframcloud.com/", "Wolfram Cloud");
  }
  
  //Test that acts based on the data from dataprovider
  @Test(priority=2, dataProviderClass = WolframSignUpData.class, dataProvider="SignupData")
  public void createNewAccount(String id, String fname, String lname, 
		  							String pswd, String repeatPswd, String rowIndex) throws Exception {
	  
	  //User selects Wolfram PRogramming Cloud option
	  wfStartPage.loadLoginPage();
	  
	  //User click 'Regiser New Account' button
	  wfLoginPage.navigateToSignUpPage();
	  
	 //User enters Signup Credentials
	  wfRegisterPage.loadSignupFields(id, fname, lname, pswd, repeatPswd);
	  
	 // Get the Test Objective (+ve or -ve) before clicking Login button
	  Boolean testObjective = wfRegisterPage.getSignUpTestSheet(Integer.parseInt(rowIndex));
	  
	  //User clicks Create Wolfram ID button
	  Boolean loginStatus = wfRegisterPage.performRegister(testObjective);
	  
	  // This condition is basically decide whether to test the further steps or not
	  // For Positive TC Objective 'Yes', otherwise 'No'
	  // Also one other important condition is to test if the login action is actually successful or not
	  if(loginStatus == true && testObjective == true) {
		  
		// User clicks Sign Out
		 wfMainPage.performSignOut();
		 
	  } else {
		  
		  // Navigate back to home page before starting next test case
		  wfStartPage.navigateBacktoHome();
	  }
	  
  }
}
