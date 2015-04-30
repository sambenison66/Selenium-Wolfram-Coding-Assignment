/*
 * Author: Samuel Benison - samuel.jeyarajvictor@mavs.uta.edu
 * Designed for - Wolfram Coding Assignment
 */

package com.wolfram.util;

import java.io.FileInputStream;

import java.io.FileNotFoundException;

import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;

import org.apache.poi.xssf.usermodel.XSSFRow;

import org.apache.poi.xssf.usermodel.XSSFSheet;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	private static XSSFSheet ExcelWSheet;

	private static XSSFWorkbook ExcelWBook;

	private static XSSFCell Cell;

	private static XSSFRow Row;

// Method to read the Excel sheet and return all the values of the sheet
public static Object[][] getExcelSheet(String FilePath, String SheetName) throws Exception {   

   String[][] sheetContent = null;

   try {

	   FileInputStream ExcelFile = new FileInputStream(FilePath);

	   ExcelWBook = new XSSFWorkbook(ExcelFile);

	   ExcelWSheet = ExcelWBook.getSheet(SheetName);

	   int startRow = 1; // Ignoring the Header

	   int startCol = 0;

	   int ci,cj;

	   int totalRows = ExcelWSheet.getLastRowNum();

	   int totalCols = getColumnCount();

	   sheetContent = new String[totalRows][totalCols+1];

	   ci=0;

	   for (int i=startRow;i<=totalRows;i++, ci++) {           	   

		  cj=0;

		   for (int j=startCol;j<totalCols;j++, cj++){

			   sheetContent[ci][cj]=getCellData(i,j);  

				}
		   
		   // Inserting Row Number as the last column
		   sheetContent[ci][cj] = Integer.toString(i);

			}

		}

	catch (FileNotFoundException e){

		System.out.println("Incorrect File Path. No Excel file found in given path");

		e.printStackTrace();

		}

	catch (IOException e){

		System.out.println("Could not read the Excel sheet, Check if it's corrupted");

		e.printStackTrace();

		}

	return(sheetContent);

	}

// Method to read the content of a Cell in Excel sheet
private static String getCellData(int RowNum, int ColNum) throws Exception {

	try{

		Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);

		int dataType = Cell.getCellType();

		if  (dataType == 3 || dataType == 5) {

			return "";
			
		} else if(dataType == 4) {
			
			String CellData = Boolean.toString(Cell.getBooleanCellValue());

			return CellData;
			
		} else{

			String CellData = Cell.getStringCellValue();

			return CellData;

		}
	}catch (Exception e){

		return "";

	}

   }

// Method to calculate the number of columns
  private static int getColumnCount() throws Exception {
	  
	  int colCount = 0;
	  Row = ExcelWSheet.getRow(0);
	  
	  colCount = Row.getPhysicalNumberOfCells();
	  
	  return colCount;
  }
  
  // Method to retrieve data for a single row from excel sheet
  public static String[] getRowContent(String FilePath, String SheetName, int rowNo) throws Exception {
	  
	  String[] rowContent = null;
	  
	  try {

		   FileInputStream ExcelFile = new FileInputStream(FilePath);

		   ExcelWBook = new XSSFWorkbook(ExcelFile);

		   ExcelWSheet = ExcelWBook.getSheet(SheetName);

		   int startCol = 0;

		   int cj;

		   int totalCols = getColumnCount();
		   
		   int totalRows = ExcelWSheet.getLastRowNum();
		   
		   rowContent = new String[totalCols];
		   
		   if(rowNo > totalRows) {
			   
			   rowContent = fillBlankValues(rowContent, totalCols);
			   
		   } else {          	   
	
				  cj=0;
	
				   for (int j=startCol;j<totalCols;j++, cj++){
	
					   rowContent[cj]=getCellData(rowNo,cj); 
	
					}
		   }

		}

		catch (FileNotFoundException e){

			System.out.println("Incorrect File Path. No Excel file found in given path");

			e.printStackTrace();

			}

		catch (IOException e){

			System.out.println("Could not read the Excel sheet, Check if it's corrupted");

			e.printStackTrace();

			}

		return(rowContent);
  }
  
  // Method to retun blank values when the row is null
  private static String[] fillBlankValues(String[] rowContent, int totalCols) throws Exception {
	  
	  for(int k=0; k<totalCols; k++) {
		  rowContent[k] = "";
	  }
	  
	  return (rowContent);
  }

}