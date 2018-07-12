/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal;

import csrportal.helpers.Mailer;
import csrportal.helpers.TableWidget;
import csrportal.models.Visitor;
import csrportal.views.AddVisitor;
import csrportal.views.HomeFrame;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 *
 * @author shady
 */
public class AppController {
    
    
    private HomeFrame fm;
    
    public AppController(){
        
    }
    
    public AppController(HomeFrame frame ){
        fm = frame;
    }
    
    public HomeFrame getFrame(){
        return fm;
    }
    
    
    public static DefaultListModel getWeekDaysModel(){
        DefaultListModel model = new DefaultListModel();
        String [] days = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};
        for (String day: days) {           
            model.addElement(day);
        }
        return model;
    }
    
    public void loadVisitorsTable(){
        String [] vistorsColumns = {"id","Time","Name","Reason","To"};
        TableWidget tb = new TableWidget(vistorsColumns);
        System.out.println(visitorsQuery());
        List<Visitor> lv = new Visitor().findAllBySql(visitorsQuery());
        for( Visitor v : lv ){
            Object[] obj = {v.currentPk(),v.getVisitTime(),v.getFullName(),v.getReason(),v.getAttendingPerson() };
           tb.addRow(obj);
        }
        getFrame().visitorsTable.setModel(tb.getModel());
        getFrame().visitorsTable.removeColumn(getFrame().visitorsTable.getColumnModel().getColumn(0));
    }
    
    public String visitorsQuery(){
        String query = "visit_date LIKE \"%{calendar_date}%\"";
        return query
                .replace("{calendar_date}", getFrame().getDateField());
    }
    
    
    public void searchTable( String keyword ){
        getFrame().DaysList.clearSelection();
        String [] vistorsColumns = {"id","Date","Name","Reason","To"};
        TableWidget tb = new TableWidget(vistorsColumns);
        Visitor vm = new Visitor();
        vm.searchDb(keyword);
        List<Visitor> lv = new Visitor().findAllBySql(vm.searchDb(keyword));
        for( Visitor v : lv ){
            Object[] obj = {v.currentPk(),v.getVisitDate(),v.getFullName(),v.getReason(),v.getAttendingPerson() };
            tb.addRow(obj);
        }
        getFrame().visitorsTable.setModel(tb.getModel());
        getFrame().visitorsTable.removeColumn(getFrame().visitorsTable.getColumnModel().getColumn(0));
    }
    
    public Visitor findVisitor(int Id ){
        Visitor vs = new Visitor();
        vs.findByPk(Id);
        return vs;
    }
    
    public void editVisitor(int Id ){
        Visitor vs = findVisitor(Id);
        System.out.println(vs.getFullName());
        AddVisitor vePopup = new AddVisitor(getFrame(), true, vs);
        vePopup.setLocationRelativeTo(null);
        vePopup.setTitle("Visitor Details");
        vePopup.loadVisitor();
        vePopup.setVisible(true);
    }
    
    public void sendMail(String[] to, String subject, String body){
        Mailer mail = new Mailer();
        mail.setMessage(body);
        mail.setSubject(subject);
        mail.setRecipient(to);
        mail.start();
    }
    
    
}
