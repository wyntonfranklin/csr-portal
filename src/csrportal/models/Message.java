/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal.models;

import csrportal.helpers.DBModel;
import csrportal.helpers.ExcelSheet;
import csrportal.helpers.SearchQuery;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shady
 */
public class Message extends DBModel {
    
    private int messageId;
    private String messageFor;
    private String messageNote;
    private String messageDate;
    private String messageTime;
    
    public String [] table_columns = {"message_for","note","message_date","message_time"};
    
    public Message(){
        
    }
    
    public Message( ResultSet column ){
        setAttributes(column);
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getMessageFor() {
        return messageFor;
    }

    public void setMessageFor(String messageFor) {
        this.messageFor = messageFor;
    }

    public String getMessageNote() {
        return messageNote;
    }

    public void setMessageNote(String messageNote) {
        this.messageNote = messageNote;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
    
    public String getFormatedDate(){
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        String strDate="";
        try {
            strDate = dateFormat.format(originalFormat.parse(this.getMessageDate()));
        } catch (ParseException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strDate;
    }
    
    @Override
    public String getTableName() {
        return "Messages";
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
    public int currentPk() {
        return this.getMessageId();
    }
    
    
    @Override
    public void setAttributes(ResultSet set) {
        try{
            messageId = set.getInt("id");
            messageFor = set.getString("message_for");
            messageNote = set.getString("note");
            messageDate  =set.getString("message_date");
            messageTime = set.getString("message_time");
        }catch(SQLException e){
            
        }
    }

    @Override
    public List<Message> findAll() {
        List<Message> allresults = new ArrayList<>();
        rs = getAllColumns();
        try{
            while(rs.next()){
                Message tstClass = new Message();
                tstClass.setAttributes(rs);
                allresults.add(tstClass);
             }   
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return allresults;
    }

    @Override
    public List<Message> findAllBySql(String query) {
        List<Message> allresults = new ArrayList<>();
        rs = getAllColumnsBySql(query);
        try{
            while(rs.next()){
                Message tstClass = new Message();
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
    public void save() {
        try {
            PreparedStatement dbModel = getSaveStatement();
            dbModel.setString(1, messageFor);
            dbModel.setString(2, messageNote);
            dbModel.setString(3, messageDate);
            dbModel.setString(4, messageTime);
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
        sq.likeQuery("message_for", value, SearchQuery.OP_OR);
        sq.likeQuery("note", value, SearchQuery.OP_OR);
        System.out.println(sq.getSearchQuery());
        return sq.getSearchQuery();
    }
    
    public String getSummary(){
        String output = "";
        output += "Message For: " + this.getMessageFor() + "\n";
        output += "Details: " + this.getMessageNote() + "\n";
        return output;
    }
    
    public void saveToExcel(String filename) throws IOException{
        String [] header = this.getColumns();
        ExcelSheet excel = new ExcelSheet();
        excel.setHeader(header);
        excel.setFileName(filename);
        List<Message> msg = this.findAll();
        for( Message message : msg ){
             String[] rows ={
                message.getMessageFor(),
                message.getMessageNote(),
                message.getMessageDate(),
                message.getMessageTime()
            };
            excel.addRow(rows);
        }
        excel.save();
    }

    
}
