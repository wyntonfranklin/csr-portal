/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal.models;

import csrportal.helpers.DBModel;
import csrportal.helpers.SearchQuery;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shady
 */
public class Visitor extends DBModel {
    
    ResultSet rs;
    private int vistorId;
    private String firstName;
    private String lastName;
    private String mailingAddress;
    private String address;
    private String primaryContact;
    private String secondaryContact;
    private String reason;
    private String visitDate;
    private String visitTime;
    private String attendingPerson;
    private String email;
    
    public String [] table_columns = {"first_name","last_name","mailing_address","address","primary_contact","secondary_contact",
    "reason","visit_date","visit_time","attending_person","email"};
    
    public Visitor(){
        
    }
    
    public Visitor( ResultSet column ){
        setAttributes(column);
    }

    public int getVistorId() {
        return vistorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public String getAddress() {
        return address;
    }

    public String getPrimaryContact() {
        return primaryContact;
    }

    public String getSecondaryContact() {
        return secondaryContact;
    }

    public String getReason() {
        return reason;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public String getVisitTime() {
        return visitTime;
    }

    public String getAttendingPerson() {
        return attendingPerson;
    }

    public String[] getTable_columns() {
        return table_columns;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrimaryContact(String primaryContact) {
        this.primaryContact = primaryContact;
    }

    public void setSecondaryContact(String secondaryContact) {
        this.secondaryContact = secondaryContact;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public void setAttendingPerson(String attendingPerson) {
        this.attendingPerson = attendingPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getFullName(){
        return firstName + " " + lastName;
    }
    

    @Override
    public String getTableName() {
        return "Visitors";
    }
    
    @Override
    public String getPrimaryKey(){
        return "visitor_id";
    }
    
    @Override
    public String[] getColumns(){
        return table_columns;
    }
    
    @Override
    public int currentPk() {
        return this.getVistorId();
}
    
    @Override
    public void setAttributes(ResultSet set) {
        try{
            vistorId = set.getInt("visitor_id");
            firstName = set.getString("first_name");
            lastName = set.getString("last_name");
            mailingAddress = set.getString("mailing_address");
            address = set.getString("address");
            email = set.getString("email");
            primaryContact = set.getString("primary_contact");
            secondaryContact = set.getString("secondary_contact");
            reason = set.getString("reason");
            visitDate = set.getString("visit_date");
            visitTime = set.getString("visit_time");
            attendingPerson = set.getString("attending_person");
        }catch(SQLException e){
            
        }
    }

    @Override
    public List<Visitor> findAll() {
        List<Visitor> allresults = new ArrayList<>();
        rs = getAllColumns();
        try{
            while(rs.next()){
                Visitor tstClass = new Visitor();
                tstClass.setAttributes(rs);
                allresults.add(tstClass);
             }   
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return allresults;
    }
    

    @Override
    public List<Visitor> findAllBySql(String query) {
        List<Visitor> allresults = new ArrayList<>();
        rs = getAllColumnsBySql(query);
        try{
            while(rs.next()){
                Visitor tstClass = new Visitor();
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
        throw new UnsupportedOperationException("Not supported yet."); 
    }


    @Override
    public void save() {
        try {
            PreparedStatement dbModel = getSaveStatement();
            dbModel.setString(1, firstName);
            dbModel.setString(2, lastName);
            dbModel.setString(3, mailingAddress);
            dbModel.setString(4, address);
            dbModel.setString(5, primaryContact);
            dbModel.setString(6, secondaryContact);
            dbModel.setString(7, reason);
            dbModel.setString(8, visitDate);
            dbModel.setString(9, visitTime);          
            dbModel.setString(10, attendingPerson);
            dbModel.setString(11,email);
            dbModel.executeUpdate();
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());   
        }; 
    }

    @Override
    public Object[] toTableRow() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public List<Object[]> getTableRows() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
    
    public String searchDb( String value ){
        SearchQuery sq = new SearchQuery();
        sq.likeQuery("first_name", value, SearchQuery.OP_OR);
        sq.likeQuery("last_name", value, SearchQuery.OP_OR);
        sq.likeQuery("mailing_address", value, SearchQuery.OP_OR);
        sq.likeQuery("attending_person", value, SearchQuery.OP_OR);
        sq.likeQuery("email", value, SearchQuery.OP_OR);
        sq.likeQuery("reason", value, SearchQuery.OP_OR);
        sq.likeQuery("address", value, SearchQuery.OP_OR);
        System.out.println(sq.getSearchQuery());
        return sq.getSearchQuery();
    }
    
}
