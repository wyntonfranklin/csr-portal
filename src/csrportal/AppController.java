/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal;

import csrportal.helpers.Mailer;
import csrportal.helpers.TableWidget;
import csrportal.models.Message;
import csrportal.models.Visitor;
import csrportal.views.VisitorForm;
import csrportal.views.HomeFrame;
import csrportal.views.MessageForm;
import csrportal.views.SearchForm;
import csrportal.views.SendEmail;
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
        System.out.println(calendarQuery("visit_date"));
        List<Visitor> lv = new Visitor().findAllBySql(calendarQuery("visit_date"));
        for( Visitor v : lv ){
            Object[] obj = {v.currentPk(),v.getVisitTime(),v.getFullName(),v.getReason(),v.getAttendingPerson() };
           tb.addRow(obj);
        }
        getFrame().visitorsTable.setModel(tb.getModel());
        getFrame().visitorsTable.removeColumn(getFrame().visitorsTable.getColumnModel().getColumn(0));
    }
    
    public String calendarQuery( String attr ){
        String query = attr + " LIKE \"%{calendar_date}%\"";
        return query
                .replace("{calendar_date}", getFrame().getDateField());
    }
    
    public void searchVisitorTable( String keyword ){
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
    
    public void searchTable( String keyword ){
         int currentTab = getFrame().getCurrentTab();
         switch(currentTab){
            case 0:
                this.searchVisitorTable(keyword);
                break;
            case 1:
                this.searchMessageTable(keyword);
                break;
            case 2:
                break;
            default:
                break;
         }
    }
    
    public void loadMessageTable(){
        String [] vistorsColumns = {"id","Time","For","Exceprt"};
        TableWidget tb = new TableWidget(vistorsColumns);
        System.out.println(calendarQuery("message_date"));
        List<Message> msg = new Message().findAllBySql(calendarQuery("message_date"));
        for( Message m : msg ){
            Object[] obj = { 
                m.currentPk(),
                m.getMessageTime(),
                m.getMessageFor(),
                m.getMessageNote()};
           tb.addRow(obj);
        }
        getFrame().messageTable.setModel(tb.getModel());
        getFrame().messageTable.removeColumn(getFrame().messageTable.getColumnModel().getColumn(0));
    }
    
    public void searchMessageTable( String keyword ){
        getFrame().DaysList.clearSelection();
        String [] vistorsColumns = {"id","Time","For","Exceprt"};
        TableWidget tb = new TableWidget(vistorsColumns);
        List<Message> msg = new Message().findAllBySql(new Message().searchDb(keyword));
        for( Message m : msg ){
            Object[] obj = { 
                m.currentPk(),
                m.getMessageTime(),
                m.getMessageFor(),
                m.getMessageNote()};
           tb.addRow(obj);
        }
        getFrame().messageTable.setModel(tb.getModel());
        getFrame().messageTable.removeColumn(getFrame().messageTable.getColumnModel().getColumn(0));
    }
    
    public Visitor findVisitor(int Id ){
        Visitor vs = new Visitor();
        vs.findByPk(Id);
        return vs;
    }
    
    public void editVisitor(int Id ){
        Visitor vs = findVisitor(Id);
        System.out.println(vs.getFullName());
        VisitorForm vePopup = new VisitorForm(getFrame(), true, vs);
        vePopup.setLocationRelativeTo(null);
        vePopup.setTitle("Visitor Details");
        vePopup.loadVisitor();
        vePopup.setVisible(true);
    }
    
    public void editMessage( int Id ){
        Message msg = new Message();
        msg.findByPk(Id);
        MessageForm msgForm = new MessageForm(getFrame(),true);
        msgForm.setLocationRelativeTo(null);
        msgForm.setTitle("Message Details");
        msgForm.loadMessage(msg);
        msgForm.setVisible(true);
    }
    
    public void sendMail(String[] to, String subject, String body){
        Mailer mail = new Mailer();
        mail.setMessage(body);
        mail.setSubject(subject);
        mail.setRecipient(to);
        mail.start();
    }
    
    public void openVisitorForm(){
        VisitorForm visitorForm = new VisitorForm(getFrame(), true, new Visitor());
        visitorForm.setTitle("Add Visitor");
        visitorForm.setLocationRelativeTo(null);
        visitorForm.setVisible(true);
    }
    
    public void openSendEmail(){
        SendEmail emailForm = new SendEmail(getFrame(),true);
        emailForm.setTitle("Send Email");
        emailForm.setLocationRelativeTo(null);
        emailForm.setVisible(true);
    }
    
    public void openMessageForm(){
        MessageForm mf = new MessageForm(getFrame(),true);
        mf.loadMessage(new Message());
        mf.setTitle("New Message");
        mf.setLocationRelativeTo(null);
        mf.setVisible(true);
    }
    
    
    public void openSearchForm(){
        SearchForm fm = new SearchForm(getFrame(),true);
        fm.setTitle("Search Current Table");
        fm.setLocationRelativeTo(null);
        fm.setVisible(true);
    }
    
    public String getSelectedSummary(){
        String output="";
        int currentTab = getFrame().getCurrentTab();
         switch(currentTab){
            case 0:
                output = this.getVisitorSummary();
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                break;
         }
        return output;
    }
    
    public String getVisitorSummary(){
        int index = getFrame().visitorsTable.getSelectedRow();
        int visitorId = Integer.valueOf(getFrame().visitorsTable.getModel().getValueAt(index, 0).toString());
        if(index >= 0 ){
            Visitor vs = new Visitor();
            vs.findByPk(visitorId);
            return vs.getSummary();   
        }else{
            return "";
        }
    }
    
    
}
