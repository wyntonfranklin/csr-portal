/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

/**
 *
 * @author shady
 */
public class ViewController {
    
    private javax.swing.JFrame view;
    
    public ViewController(){
        
    }
    
    public ViewController(javax.swing.JFrame mainView){
        view = mainView;
    }
    
    public void setView(javax.swing.JFrame frame ){
        view = frame;
    }
    
    public javax.swing.JFrame getView(){
        return view;
    }
    
    public void defaultSettings(){
       getView().setLocationRelativeTo(null);
    }
    
    public void showView(){
        getView().setVisible(true);
    }
    
    public void setViewTitle(String title ){
        getView().setTitle(title);
    }
    
}
