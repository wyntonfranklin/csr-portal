/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal.helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shady
 */
public class AppProperties {
    
    public Properties saveProps;
    public String defaultDate="yyyy-MM-dd";
    public String defaultTime="hh:mm a";
    public String isAppLoaded ="false";
    public String hostName="";
    public String userName="";
    public String password="";
    public String fileLocation = "csr.db";
    
    
    
    public AppProperties(){
       saveProps = new Properties();
       init();
    }
    
    public void init(){
        this.loadAttributes();
    }

    public String getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(String defaultDate) {
        this.defaultDate = defaultDate;
    }

    public String getDefaultTime() {
        return defaultTime;
    }

    public void setDefaultTime(String defaultTime) {
        this.defaultTime = defaultTime;
    }

    public String getIsAppLoaded() {
        return isAppLoaded;
    }

    public void setIsAppLoaded(String isAppLoaded) {
        this.isAppLoaded = isAppLoaded;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
    
    
    
    public Properties getProperties(){
        return saveProps;
    }
    
    public void save(){
        this.assignAttributes();
        try {
            getProperties().storeToXML(new FileOutputStream("settings.xml"), "");
        } catch (IOException ex) {
            Logger.getLogger(AppProperties.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void assignAttributes(){
         getProperties().setProperty("defaultDate", this.getDefaultDate()); 
         getProperties().setProperty("defaultTime", this.getDefaultTime());
         getProperties().setProperty("isAppLoaded", this.getIsAppLoaded());
         getProperties().setProperty("host",this.getHostName());
         getProperties().setProperty("username", this.getUserName());
         getProperties().setProperty("password",this.getPassword());
         getProperties().setProperty("dbFile",this.getFileLocation());
    }
    
    public String getProperty(String value){
        try {
            getProperties().loadFromXML(new FileInputStream("settings.xml"));
        } catch (IOException ex) {
           // Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        }
       // String result = loadProps.getProperty(value);
        return "";
    }
    
    private void loadAttributes(){
        try {
            getProperties().loadFromXML(new FileInputStream("settings.xml"));
        } catch (IOException ex) {
            this.save();
        }
        this.setDefaultDate(getProperties().getProperty("defaultDate"));
        this.setDefaultTime(getProperties().getProperty("defaultTime"));
        this.setIsAppLoaded(getProperties().getProperty("isAppLoaded"));
        this.setPassword(getProperties().getProperty("password"));
        this.setUserName(getProperties().getProperty("username"));
        this.setHostName(getProperties().getProperty("host"));
        this.setFileLocation(getProperties().getProperty("dbFile"));
    }
    
    public void resetDefaults(){
        AppProperties nProp = new AppProperties();
        nProp.setHostName("");
        nProp.setPassword("");
        nProp.setUserName("");
        nProp.setFileLocation("csr.db");
        nProp.save();
    }
}
