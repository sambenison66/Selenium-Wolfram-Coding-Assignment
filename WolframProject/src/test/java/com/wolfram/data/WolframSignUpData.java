/*
 * Author: Samuel Benison - samuel.jeyarajvictor@mavs.uta.edu
 * Designed for - Wolfram Coding Assignment
 */

package com.wolfram.data;

import org.testng.annotations.DataProvider;

import com.wolfram.util.ExcelUtils;

public class WolframSignUpData {
	
	// Enter your Excel file path here.
	private static String FILE_PATH = "C://Users//Samuel//Workspace_New//WolframProject//src//main//resources//SignUp.xlsx";
	private static String SHEET_NAME = "DataSheet";
	
	@DataProvider (name = "SignupData")
    public static Object[][] loadSignupData() throws Exception{
 
         Object[][] sheetContent = ExcelUtils.getExcelSheet(FILE_PATH,SHEET_NAME);
 
         return (sheetContent);
 
	}
	
	public static String getFilePath() {
		return FILE_PATH;
	}

}
