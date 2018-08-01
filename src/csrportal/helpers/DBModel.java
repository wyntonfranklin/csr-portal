package csrportal.helpers;


import csrportal.helpers.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shady
 */
public abstract class DBModel {
    
    private final String qFindAll = "SELECT * FROM {tablename}";
    private final String qFindByPk = "SELECT * FROM {tablename} WHERE {primary_key}={pid}";
    private final String qRaw ="SELECT * FROM {tablename} WHERE {condition}";
    private final String qUpdate = "UPDATE {tablename} SET {attributes} WHERE {condition}";
    private final String qInsert = "INSERT INTO {tablename} ({columns}) values({values})";
    private final String qDelete ="DELETE FROM {tablename} WHERE {condition}";
    private final String qDeleteByPk ="DELETE FROM {tablename} WHERE {primary_key}={pid}";
    private final String qDeleteAll ="DELETE FROM {tablename}";
    public String tableName = "books";
    public String primaryKey ="id";
    public ResultSet rs;
    public String [] columns;
    public String [] colTypes;
    PreparedStatement ps;
    
    
    
    public DBModel(){
        
    }
    
    public DBModel(ResultSet column){
        
    }
  
    public ResultSet getAllColumns(){
        return new DB().queryAll(findAllQuery());
    }
    
    public ResultSet getAllColumnsBySql( String query ){
        return new DB().queryAll(findAllBySqlQuery(query));
    }
    
    public ResultSet getColumn( int id ){
       return new DB().queryAll(findByPkQuery(id));
    }
    
    public ResultSet getData( String query ){
        return new DB().queryAll(query);
    }
    
    public void executeQuery( String query ){
        DB db = new DB();
        db.executeQuery(query);
    }
    
    public ResultSet search(){
        return new DB().queryAll("");
    }
    
    public void deleteRecordByPk(int Id){
        String sql = this.deleteByPkQuery(Id);
        new DB().executeQuery(sql);
    }
    
    public String getTableName(){
        return tableName;
    }
    
    public String getPrimaryKey(){
        return primaryKey;
    }
    
    public void setTableName( String table_name ){
        tableName = table_name;
    }
    
    public String [] getColumns(){
        return columns;
    }
    
    public String[] getColTypes(){
        return colTypes;
    }
    
    public ResultSet getQueryResults(){
        return rs;
    }
    
    public void createListFromResultSet(){
        ResultSet allColumns = getQueryResults();
        
    }
    
    public String findAllQuery(){
        return qFindAll
                .replace("{tablename}",getTableName());
    }
    
    public String findAllBySqlQuery( String query ){
        return qRaw
                .replace("{tablename}",getTableName())
                .replace("{condition}", query);
    }
    
    public String findByPkQuery( int key ){
        String holder;
        holder = qFindByPk
                .replace("{tablename}",getTableName())
                .replace("{primary_key}", getPrimaryKey())
                .replace("{pid}", String.valueOf(key));
        return holder;
    }
    
    public String findBySqlQuery( String conditions ){
        return qRaw
                .replace("{tablename}", getTableName())
                .replace("{condition}", conditions);
    }
    
    public String updateByAttributesQuery( String attributes ){
        return qUpdate
                .replace("{tablename}", getTableName())
                .replace("{attributes}", attributes);
    }
    
    public String deleteByPkQuery( int key ){
        return qDeleteByPk
                .replace("{tablename}", getTableName())
                .replace("{primary_key}", getPrimaryKey())
                .replace("{pid}", String.valueOf(key));
    }
    
    public PreparedStatement getInsertStatement() throws SQLException{
        return ps = new DB().getConnection().prepareStatement(insertQuery());
    }
    
    public PreparedStatement getUpdateStatement() throws SQLException{
        return ps = new DB().getConnection().prepareStatement(updateQuery());
    }
    
    public void setUpdateValues(){
        String [] cols = getColumns();
        String [] types = getColTypes();
        for(int i=1; i<= cols.length; i++){
            if(types[i].equals("int")){
               // ps.setInt(i, cols[i]);
            }
        }
    }
    
    
    public String insertQuery(){
        return qInsert
                .replace("{tablename}", getTableName())
                .replace("{columns}", getColumnInsertString())
                .replace("{values}", addQuestionMarks(getColumns().length, ","));
    }
    
    public String getInsertQuery(){
        return insertQuery();
    }
    
    public String updateQuery(){
        return qUpdate
                .replace("{tablename}",getTableName())
                .replace("{attributes}", getUpdateAttributes())
                .replace("{condition}",getUpdateCondition());
    }
    
    public String getUpdateAttributes(){
        return  getColumnUpdateString();
    }
    
    public String getUpdateCondition(){
        return getPrimaryKey() + "=" + currentPk();
    }
    
    public String getColumnInsertString(){
        return addDelimiter(getColumns(),"",", ");
    }
    
    public String getColumnUpdateString(){
        return addDelimiter(getColumns(),"=?",", ");
    }
    
    public String addDelimiter( String[] text, String content, String delimiter ){
        String output="";
        for(int i=0; i<= text.length-1; i++){
            if(i < text.length-1){
                output += text[i] + content + delimiter;        
            }else{
                output += text[i] + content;   
            }         
        }
        return output;
    }
    
    public PreparedStatement getSaveStatement() throws SQLException{
        if(isNewRecord()){
            return getInsertStatement();
        }else{
            return getUpdateStatement();
        }
    }
    
    public String addQuestionMarks( int count, String delimiter ){
        String output ="";
        String mark = "?";
        for(int i=0; i<= count-1; i++){
            if( i < count-1 ){
                output += mark + delimiter;        
            }else{
                output += mark;   
            }       
        }
        return output;
    }
    
    public Boolean isNewRecord(){
        if(currentPk() <= 0 ){
            return true;
        }
        return false;
    }
    
    public void update(){
        save();
    }
    
    public void insert(){
        save();
    }
    
    public void deleteByPk(int id){
        String query = deleteByPkQuery(id);
        System.out.println(query);
        new DB().executeQuery(query);
    }
    
    
    
  //  public void updateModel(Strin)
   
    public abstract void setAttributes(ResultSet set);
    
    public abstract List<?> findAll();
    
    public abstract List<?> findAllBySql( String query );
    
    public abstract void findByPk( int id );
    
    public abstract void findBySql( String query);
    
    public abstract int currentPk();
    
    public abstract void save();
    
    public abstract Object[] toTableRow();
    
    public abstract List<Object[]> getTableRows();
    
    
   
    
    
    
    

    
}
