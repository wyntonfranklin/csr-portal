/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal.helpers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author shady
 */
public class ExcelSheet{
    
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private String sheetName="FirstSheet";
    private String fileName="NewExcelFile.xls";
    private int rowCounter =0;
    private int totalColumns=0;
    
    public ExcelSheet(){
        this.workbook = new HSSFWorkbook();
        this.createSheet();
    }
    
    public ExcelSheet(String sheet){
        this.workbook = new HSSFWorkbook(); 
        this.createSheet(sheet);
    }
    
    
    private void createSheet(){
        this.sheet = this.workbook.createSheet(sheetName);
    }
    
    private void createSheet(String name){
         this.sheet = this.workbook.createSheet(name); 
    }
    
    public String getSheetName(){
        return this.sheetName;
    }
    
    public void setFileName(String fname){
        this.fileName = fname;
    }
    
    public String getFileName(){
        return this.fileName;
    }
    
    public void setHeader(String[] rows){
         HSSFRow rowhead = getSheet().createRow((short)getCurrentRow());
         this.setTotalColumns(rows.length);
         for(int i=0; i<= rows.length-1; i++){
            rowhead.createCell(i).setCellValue(rows[i]); 
         }
        this.updateRowCounter();
    }
    
    public void addRow(String[] values){
        HSSFRow row = getSheet().createRow((short)getCurrentRow());
        for(int i=0; i<= this.getTotalColumns()-1; i++){
            row.createCell(i).setCellValue(values[i]);
        }
        this.updateRowCounter();
    }
    
    public void save() throws FileNotFoundException, IOException{
        FileOutputStream fileOut = new FileOutputStream(getFileName());
        this.getWorkBook().write(fileOut);
        fileOut.close();
        this.getWorkBook().close();
        System.out.println("Your excel file has been generated!");
  
    }
    
    public void setTotalColumns(int number){
        totalColumns = number;
    }
    
    public int getTotalColumns(){
        return totalColumns;
    }
    
    public HSSFSheet getSheet(){
        return sheet;
    }
    
    public int getCurrentRow(){
        return rowCounter;
    }
    
    public void updateRowCounter(){
       rowCounter = rowCounter+1;
    }
    
    public HSSFWorkbook getWorkBook(){
        return this.workbook;
    }
    
    
    public static void main(String[]args) {
        ExcelSheet exsheet = new ExcelSheet();
        String [] headers = {"Name","Job","Contact"};
        String [] row = {"wynton","programmer","454-3353"};
        String [] row2 = {"james","maanger","453-3533"};
        exsheet.setHeader(headers);
        exsheet.addRow(row);
        exsheet.addRow(row2);
        try {
            exsheet.save();
        } catch (IOException ex) {
            Logger.getLogger(ExcelSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
