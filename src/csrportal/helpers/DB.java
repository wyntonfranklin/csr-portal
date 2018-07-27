package csrportal.helpers;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shady
 */
public class DB {
    
    private static String url = "jdbc:sqlite:csr.db";    
    private static String driverName = "jdbc:sqlite";   
    private static String username = "";   
    private static String password = "";
    private static Connection con;
    private static String urlstring;
    
    
    public DB(){
        
    }
    
    public static void setUrl( String db_url ){
        url = db_url;
    }
    
    
    public static void setDriverName(){
        
    }
    
    public Connection getConnection() {
            try {
                
                con = DriverManager.getConnection(url);
                
            } catch (SQLException ex) {
                // log an exception. fro example:
               // System.out.println("Failed to create the database connection."); 
               System.out.println(ex.getMessage()); 
            }
        return con;
    }
    
    
    public ResultSet queryAll( String query ){
        Statement st;
        ResultSet rs=null;
        try{
            st = getConnection().createStatement();
            rs = st.executeQuery(query);
            //closeConnetion();
        }catch( SQLException e){
            System.out.println(e.getMessage());
        }
        return rs;
    }
    
    public void createTable( String sql ){
        Statement st;
        try{
            st = getConnection().createStatement();
            st.execute(sql);
        }catch( SQLException e){
            System.out.println(e.getMessage());
        }
    }
    
  
    
    public void updateDb(){
        
    }
    
    public void insertDb(){
        
    }
    
   
    
    
    
      /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
         DB db = new DB();
         db.getConnection();
         
    }
     
    
     
    
  
}
