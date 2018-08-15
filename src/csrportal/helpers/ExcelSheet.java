/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal.helpers;

import csrportal.models.Note;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

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
    FileInputStream fileInputStream;
    
    public ExcelSheet(){
        this.workbook = new HSSFWorkbook();
        this.createSheet();
    }
    
    public ExcelSheet(String sheet){
        this.workbook = new HSSFWorkbook(); 
        this.createSheet(sheet);
    }
    
    public ExcelSheet(File excelsheet){
        try {
            fileInputStream=new FileInputStream(excelsheet);
            this.workbook = new HSSFWorkbook(fileInputStream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ExcelSheet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ExcelSheet.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setSheet(0);
    }
    
    private void setSheet(int value){
        this.sheet=this.workbook.getSheetAt(value);
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
    
    public Iterator getSheetRows(){
        return this.sheet.iterator();
    }
    
    
    
    public static void main(String[]args) {
        ExcelSheet excel = new ExcelSheet(new File("NewExcelFile.xls"));
        excel.setSheet(0);
        Iterator rowIterator = excel.getSheetRows();
        rowIterator.next();
        Note nt = new Note();
        while (rowIterator.hasNext())
        {
            Row row=(Row) rowIterator.next();
            nt.setNoteContent(row.getCell(0).getStringCellValue());
            nt.setNoteDate(row.getCell(1).getStringCellValue());
            nt.setNoteTime(row.getCell(2).getStringCellValue());
            nt.setTags(row.getCell(3).getStringCellValue());
            nt.save();
            System.out.println("/n");
        }
    }
}
