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

import com.wolfram.factory.WolframLoginPage;
import com.wolfram.factory.WolframMainPage;
import com.wolfram.factory.WolframStartPage;
import com.wolfram.data.WolframSignInData;

public class CreateNB {
  
	WebDriver driver;
	WolframStartPage wfStartPage;
	WolframLoginPage wfLoginPage;
	WolframMainPage wfMainPage;
		
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
  }

  // Release the memory and close browser once everything is done 
  @AfterClass(alwaysRun = true)
  public void destroyDriver() {
	  this.driver.quit();
	  this.driver = null;
	  wfStartPage = null;
	  wfLoginPage = null;
	  wfMainPage = null;
  }
  
  // Load Wolfram Cloud page
  @Test(priority=1)
  public void loadPage() {
	  wfStartPage.loadPage("https://www.wolframcloud.com/", "Wolfram Cloud");
  }
  
  //Test that acts based on the data from dataprovider
  @Test(priority=2, dataProviderClass = WolframSignInData.class, dataProvider="SigninData")
  public void createNotebook(String id, String pwd, String rowIndex) throws Exception {
	  
	//User selects Wolfram PRogramming Cloud option
	  wfStartPage.loadLoginPage();
	  
	//User enters Login Credentials
	 wfLoginPage.loadLoginFields(id, pwd);
	  
	 // Get the Test Objective (+ve or -ve) before clicking Login button
	  Boolean testObjective = wfLoginPage.getSignInTestSheet(Integer.parseInt(rowIndex));
	  
	  //User clicks Login button
	  Boolean loginStatus = wfLoginPage.performLogin(testObjective);
	  
	  // This condition is basically decide whether to test the further steps or not
	  // For Positive TC Objective 'Yes', otherwise 'No'
	  // Also one other important condition is to test if the login action is actually successful or not
	  if(loginStatus == true && testObjective == true) {
		  
		  // User selects 'Create NoteBook' option in New dropdown
		  wfMainPage.createNewNoteBook();
		  
		  // User verifies the extension of new unnamed notebook (in New Window)
		  wfMainPage.verifyNBExtension();
		  
		  // User clicks Sign Out
		  wfMainPage.performSignOut();
		  
	  } else {
		  
		  // Navigate back to home page before starting next test case
		  wfStartPage.navigateBacktoHome();
	  }
	  
  }
 
}
