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
public class Appointment extends DBModel{
    
    private int appointmentId;
    private String appDate;
    private String appTime;
    private String appPerson;
    private String appMeeting;
    private String appEmail;
    private String appContact;
    private String appDetails;
    
    public String [] table_columns = {"app_date","app_time","app_person","app_meeting","contact_email","contact_phone","reason"};

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAppDate() {
        return appDate;
    }

    public void setAppDate(String appDate) {
        this.appDate = appDate;
    }

    public String getAppTime() {
        return appTime;
    }

    public void setAppTime(String appTime) {
        this.appTime = appTime;
    }

    public String getAppPerson() {
        return appPerson;
    }

    public void setAppPerson(String appPerson) {
        this.appPerson = appPerson;
    }

    public String getAppMeeting() {
        return appMeeting;
    }

    public void setAppMeeting(String appMeeting) {
        this.appMeeting = appMeeting;
    }

    public String getAppEmail() {
        return appEmail;
    }

    public void setAppEmail(String appEmail) {
        this.appEmail = appEmail;
    }

    public String getAppContact() {
        return appContact;
    }

    public void setAppContact(String appContact) {
        this.appContact = appContact;
    }

    public String getAppDetails() {
        return appDetails;
    }

    public void setAppDetails(String appDetails) {
        this.appDetails = appDetails;
    }
    
    public String getFormatedDate(){
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");
        String strDate="";
        try {
            strDate = dateFormat.format(originalFormat.parse(this.getAppDate()));
        } catch (ParseException ex) {
            Logger.getLogger(Message.class.getName()).log(Level.SEVERE, null, ex);
        }
        return strDate;
    }
    
    

    @Override
    public String getTableName() {
        return "Appointments";
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
            appointmentId = set.getInt("id");
            appDate = set.getString("app_date");
            appTime = set.getString("app_time");
            appPerson  =set.getString("app_person");
            appMeeting = set.getString("app_meeting");
            appContact = set.getString("contact_phone");
            appEmail = set.getString("contact_email");
            appDetails = set.getString("reason");
        }catch(SQLException e){
            
        }
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> allresults = new ArrayList<>();
        rs = getAllColumns();
        try{
            while(rs.next()){
                Appointment tstClass = new Appointment();
                tstClass.setAttributes(rs);
                allresults.add(tstClass);
             }   
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return allresults;
    }


    @Override
    public List<Appointment> findAllBySql(String query) {
        List<Appointment> allresults = new ArrayList<>();
        rs = getAllColumnsBySql(query);
        try{
            while(rs.next()){
                Appointment tstClass = new Appointment();
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
        return this.getAppointmentId();
    }

    @Override
    public void save() {
        try {
            PreparedStatement dbModel = getSaveStatement();
            dbModel.setString(1, appDate);
            dbModel.setString(2, appTime);
            dbModel.setString(3, appPerson);
            dbModel.setString(4, appMeeting);
            dbModel.setString(5, appContact);
            dbModel.setString(6, appEmail);
            dbModel.setString(7,appDetails);
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
        sq.likeQuery("app_meeting", value, SearchQuery.OP_OR);
        sq.likeQuery("app_person", value, SearchQuery.OP_OR);
        sq.likeQuery("contact_email", value, SearchQuery.OP_OR);
        sq.likeQuery("contact_phone", value, SearchQuery.OP_OR);
        sq.likeQuery("reason", value, SearchQuery.OP_OR);
        System.out.println(sq.getSearchQuery());
        return sq.getSearchQuery();
    }
    
    public String getSummary(){
        String output = "";
        output += "Person: " + this.getAppPerson() + "\n";
        output += "Meeting: " + this.getAppMeeting() + "\n";
        output += "Date: " + this.getAppDate() + "\n";
        output += "Time: " + this.getAppTime() + "\n";
        return output;
    }
    
    public void saveToExcel(String filename) throws IOException{
        String [] header = this.getColumns();
        ExcelSheet excel = new ExcelSheet();
        excel.setHeader(header);
        excel.setFileName(filename);
        List<Appointment> app = this.findAll();
        for( Appointment appointment : app ){
             String[] rows ={
                appointment.getAppDate(),
                appointment.getAppTime(),
                appointment.getAppPerson(),
                appointment.getAppMeeting(),
                appointment.getAppEmail(),
                appointment.getAppContact(),
                appointment.getAppDetails()
            };
            excel.addRow(rows);
        }
        excel.save();
    }

    
}
