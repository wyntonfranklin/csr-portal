package csrportal.helpers;


import java.util.List;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author shady
 */
public class TableWidget {
    
    private DefaultTableModel md;
    private String [] colnames = {"ID","Name","Value"};
    
    
    public TableWidget(){
       initTableModel();
    }
    
    public TableWidget(String[] columnNames){
        colnames = columnNames;
        initTableModel();
    }
    
   private void initTableModel(){
        md = new DefaultTableModel( new Object [][] {}, colnames ) {
             @Override
                public boolean isCellEditable(int row, int column) {
                    //all cells false
                    return false;
                }
        };
    }
    
    public DefaultTableModel getModel(){
        return md;
    }
    
    public void setData(){
        
    }
    
    public void addRow( Object[] row ){
        getModel().addRow( row );
    }
    
    public void addRows(List<Object[]> rows ){
        for(int i=0; i<= rows.size()-1; i++){
            getModel().addRow(rows.get(i));
        }
    }
    
   
}
