/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal;

import javax.swing.DefaultListModel;

/**
 *
 * @author shady
 */
public class AppController {
    

    public static DefaultListModel getWeekDaysModel(){
        DefaultListModel model = new DefaultListModel();
        String [] days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
        for (String day: days) {           
            model.addElement(day);
        }
        return model;
    }
    
}
