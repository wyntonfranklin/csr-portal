/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal.models;

import csrportal.helpers.DBModel;
import csrportal.helpers.ExcelSheet;
import csrportal.helpers.SearchQuery;
import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author shady
 */
public class Note extends DBModel {
    
    private int noteId;
    private String noteContent;
    private String noteDate;
    private String noteTime;
    private String tags;
    
    public String [] table_columns = {"note","note_date","note_time","tags"};
    
    public Note(){
        
    }
    
    public Note( ResultSet column ){
        setAttributes(column);
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteTime() {
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public String getFormatedDate(){
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        String strDate="";
        try {
            strDate = dateFormat.format(originalFormat.parse(this.getNoteDate()));
        } catch (ParseException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strDate;
    }
    
    @Override
    public String getTableName() {
        return "Notes";
    }
    
    @Override
    public String getPrimaryKey(){
        return "id";
    }
    
    @Override
    public String[] getColumns(){
        return table_columns;
    }
    
   
    @Override
    public void setAttributes(ResultSet set) {
        try{
            noteId = set.getInt("id");
            noteContent = set.getString("note");
            noteDate = set.getString("note_date");
            noteTime  =set.getString("note_time");
            tags = set.getString("tags");
        }catch(SQLException e){
            
        }
    }
    

    @Override
    public List<Note> findAllBySql(String query) {
        List<Note> allresults = new ArrayList<>();
        rs = getAllColumnsBySql(query);
        try{
            while(rs.next()){
                Note tstClass = new Note();
                tstClass.setAttributes(rs);
                allresults.add(tstClass);
             }   
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return allresults;
    }
    
    @Override
    public List<Note> findAll() {
        List<Note> allresults = new ArrayList<>();
        rs = getAllColumns();
        try{
            while(rs.next()){
                Note tstClass = new Note();
                tstClass.setAttributes(rs);
                allresults.add(tstClass);
             }   
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return allresults;
    }


    @Override
    public void findByPk(int id) {
     rs = getColumn(id);
         try{
            while(rs.next()){
                setAttributes(rs);
             }   
        }catch(SQLException e){
            System.out.println(e.getMessage());
        } 
    }

    @Override
    public void findBySql(String query) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int currentPk() {
        return this.getNoteId(); //To change body of generated methods, choose Tools | Templates.
    }

     @Override
    public void save() {
        try {
            PreparedStatement dbModel = getSaveStatement();
            dbModel.setString(1, noteContent);
            dbModel.setString(2, noteDate);
            dbModel.setString(3, noteTime);
            dbModel.setString(4, tags);
            dbModel.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());   
        }; 
    }

    @Override
    public Object[] toTableRow() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Object[]> getTableRows() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public String searchDb( String value ){
        SearchQuery sq = new SearchQuery();
        sq.likeQuery("note", value, SearchQuery.OP_OR);
        sq.likeQuery("tags", value, SearchQuery.OP_OR);
        return sq.getSearchQuery();
    }

    
    public String getSummary(){
        String output = "";
        output += "Note: " + this.getNoteContent() + "\n";
        return output;
    }
    
    public void saveToExcel(String filename) throws IOException{
        String [] header = this.getColumns();
        ExcelSheet excel = new ExcelSheet();
        excel.setHeader(header);
        excel.setFileName(filename);
        List<Note> ls = this.findAll();
        for( Note note : ls ){
             String[] rows ={
                note.getNoteContent(),
                note.getNoteDate(),
                note.getNoteTime(),
                note.getTags()
            };
            excel.addRow(rows);
        }
        excel.save();
    }
    
    public void saveToExcel(List<Integer> values, String filename) throws IOException{
        String [] header = this.getColumns();
        ExcelSheet excel = new ExcelSheet();
        excel.setHeader(header);
        excel.setFileName(filename);
        for(int i =0; i<=values.size()-1; i++){
            Note note = new Note();
            note.findByPk(values.get(i));
            String[] rows ={
                note.getNoteContent(),
                note.getNoteDate(),
                note.getNoteTime(),
                note.getTags()
            };
            excel.addRow(rows);
        }
        excel.save();
    }
    
    public void importFromExcel(File excelfile){
        ExcelSheet excel = new ExcelSheet(excelfile);
        Iterator rowIterator = excel.getSheetRows();
        rowIterator.next();
        while (rowIterator.hasNext())
        {
            Row row=(Row) rowIterator.next();
            Note nt = new Note();
            nt.setNoteContent(row.getCell(0).getStringCellValue());
            nt.setNoteDate(row.getCell(1).getStringCellValue());
            nt.setNoteTime(row.getCell(2).getStringCellValue());
            nt.setTags(row.getCell(3).getStringCellValue());
            nt.save();
        }
    }

}
