/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal;

import csrportal.helpers.AppProperties;
import csrportal.views.HomeFrame;
import csrportal.views.SettingsForm;
import csrportal.views.StartUpForm;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shady
 */
public class CSRPortal {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        CSRPortal cs = new CSRPortal();
        if(cs.isAppLoaded().contains("false")){
            cs.openSetupForm();
        }else{
            cs.startApp();
        }
    }
    

    
    public String isAppLoaded(){
        AppProperties props = new AppProperties();
        String result = props.getIsAppLoaded();
        return result;
    }
    
    public void openSetupForm(){
       StartUpForm fm = new StartUpForm(null,true);
       fm.setLocationRelativeTo(null);
       fm.setVisible(true);
    }
    
    public void startApp(){
        HomeFrame mainApp = new HomeFrame();
        mainApp.setLocationRelativeTo(null);
        mainApp.setVisible(true);
        mainApp.setTitle("CSR Portal");   
    }
    
    
}
