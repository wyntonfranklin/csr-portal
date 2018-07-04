/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal;

import csrportal.views.HomeFrame;

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
        HomeFrame mainApp = new HomeFrame();
        mainApp.setLocationRelativeTo(null);
        mainApp.setVisible(true);
        mainApp.setTitle("CSR Portal");
    }
    
}
