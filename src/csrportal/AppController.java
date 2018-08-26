/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal;

import csrportal.helpers.AppProperties;
import csrportal.helpers.Mailer;
import csrportal.helpers.TableWidget;
import csrportal.models.Appointment;
import csrportal.models.Message;
import csrportal.models.Visitor;
import csrportal.models.Note;
import csrportal.views.AboutForm;
import csrportal.views.AppointmentForm;
import csrportal.views.ContentForm;
import csrportal.views.ExportForm;
import csrportal.views.VisitorForm;
import csrportal.views.HomeFrame;
import csrportal.views.ImportForm;
import csrportal.views.MessageForm;
import csrportal.views.NoteForm;
import csrportal.views.SearchForm;
import csrportal.views.SendEmail;
import csrportal.views.SettingsForm;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            Object[] obj = {
                v.currentPk(),
                v.getFormatedDate(),
                v.getFullName(),
                v.getReason(),
                v.getAttendingPerson() 
            };
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
                this.searchAppointmentTable(keyword);
                break;
            case 3:
                this.searchNoteTable(keyword);
                break;
            default:
                break;
         }
    }
    
    public void loadMessageTable(){
        String [] vistorsColumns = {"id","Time","For","Exceprt"};
        TableWidget tb = new TableWidget(vistorsColumns);
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
    
    public void loadAppointmentTable(){
        String [] vistorsColumns = {"id","Date","Time","Person","Meeting"};
        TableWidget tb = new TableWidget(vistorsColumns);
        List<Appointment> app= new Appointment().findAllBySql(calendarQuery("app_date"));
        for( Appointment a : app ){
            Object[] obj = { 
                a.currentPk(),
                a.getAppDate(),
                a.getAppTime(),
                a.getAppPerson(),
                a.getAppMeeting()};
           tb.addRow(obj);
        }
        getFrame().appointmentTable.setModel(tb.getModel());
        getFrame().appointmentTable.removeColumn(getFrame().appointmentTable.getColumnModel().getColumn(0));
    }
    
    public void loadNoteTable(){
        String [] notesColumns = {"id","Note","Tags","Date"};
        TableWidget tb = new TableWidget(notesColumns);
        List<Note> notes= new Note().findAllBySql(calendarQuery("note_date"));
        for( Note note : notes ){
            Object[] obj = { 
                note.currentPk(),
                note.getNoteContent(),
                note.getTags(),
                note.getFormatedDate()};
           tb.addRow(obj);
        }
        getFrame().noteTable.setModel(tb.getModel());
        getFrame().noteTable.removeColumn(getFrame().noteTable.getColumnModel().getColumn(0));
    }
    
    public void searchMessageTable( String keyword ){
        getFrame().DaysList.clearSelection();
        String [] vistorsColumns = {"id","Date","Time","For","Exceprt"};
        TableWidget tb = new TableWidget(vistorsColumns);
        List<Message> msg = new Message().findAllBySql(new Message().searchDb(keyword));
        for( Message m : msg ){
            Object[] obj = { 
                m.currentPk(),
                m.getFormatedDate(),
                m.getMessageTime(),
                m.getMessageFor(),
                m.getMessageNote()};
           tb.addRow(obj);
        }
        getFrame().messageTable.setModel(tb.getModel());
        getFrame().messageTable.removeColumn(getFrame().messageTable.getColumnModel().getColumn(0));
    }
    
    public void searchAppointmentTable( String keyword ){
        getFrame().DaysList.clearSelection();
        String [] vistorsColumns = {"id","Date","Time","Person","Meeting"};
        TableWidget tb = new TableWidget(vistorsColumns);
        List<Appointment> model = new Appointment().findAllBySql(new Appointment().searchDb(keyword));
        for( Appointment m : model ){
            Object[] obj = { 
                m.currentPk(),
                m.getFormatedDate(),
                m.getAppTime(),
                m.getAppPerson(),
                m.getAppMeeting()};
           tb.addRow(obj);
        }
        getFrame().appointmentTable.setModel(tb.getModel());
        getFrame().appointmentTable.removeColumn(getFrame().appointmentTable.getColumnModel().getColumn(0));
    }
       
    public void searchNoteTable( String keyword ){
        getFrame().DaysList.clearSelection();
        String [] notesColumns = {"id","Date","Time","Note","Tags"};
        TableWidget tb = new TableWidget(notesColumns);
        List<Note> model = new Note().findAllBySql(new Note().searchDb(keyword));
        for( Note note : model ){
            Object[] obj = { 
                note.currentPk(),
                note.getFormatedDate(),
                note.getNoteTime(),
                note.getNoteContent(),
                note.getTags()};
           tb.addRow(obj);
        }
        getFrame().noteTable.setModel(tb.getModel());
        getFrame().noteTable.removeColumn(getFrame().noteTable.getColumnModel().getColumn(0));
    }
    
    
    public Visitor findVisitor(int Id ){
        Visitor vs = new Visitor();
        vs.findByPk(Id);
        return vs;
    }
    
    public void editVisitor(int Id ){
        Visitor vs = findVisitor(Id);
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
    
    public void openNoteForm(){
        NoteForm nf = new NoteForm(getFrame(),true);
        nf.setNote(new Note());
        nf.setTitle("New Note");
        nf.setLocationRelativeTo(null);
        nf.setVisible(true);
    }
    
    public void openAppointmentForm(){
        AppointmentForm af = new AppointmentForm(getFrame(),true);
        af.setTitle("New Appointment");
        af.setLocationRelativeTo(null);
        af.loadAppointment(new Appointment());
        af.setVisible(true);
    }
    
    public void editAppointment(int Id){
        Appointment app = new Appointment();
        app.findByPk(Id);
        AppointmentForm appForm = new AppointmentForm(getFrame(),true);
        appForm.setLocationRelativeTo(null);
        appForm.setTitle("Appointment Details");
        appForm.loadAppointment(app);
        appForm.setVisible(true);
    }
    
    public void editNote(int Id){
        Note nt = new Note();
        nt.findByPk(Id);
        NoteForm nf = new NoteForm(getFrame(),true);
        nf.setLocationRelativeTo(null);
        nf.setTitle("Note Details");
        nf.setNote(nt);
        nf.setFormAttributes();
        nf.setVisible(true);
    }
    
    
    public void openSearchForm(){
        SearchForm fm = new SearchForm(getFrame(),true);
        fm.setTitle("Search Current Table");
        fm.setLocationRelativeTo(null);
        fm.setVisible(true);
    }
    
    public void openContentForm(){
        ContentForm cf = new ContentForm(getFrame(),true);
        cf.setTitle("CSR Portal Help");
        cf.setLocationRelativeTo(null);
        cf.setVisible(true);
    }
    
    public void openAboutForm(){
        AboutForm af =  new AboutForm(getFrame(),true);
        af.setTitle("About CSR Portal");
        af.setLocationRelativeTo(null);
        af.setVisible(true);
        
    }
    
    public void deleteVisitor(int Id){
        Visitor vs = new Visitor();
        vs.deleteByPk(Id);
    }
    
    public void deleteAppointment(int Id){
        Appointment app = new Appointment();
        app.deleteByPk(Id);
    }
    
    public void deleteMessages(int Id){
        Message msg = new Message();
        msg.deleteByPk(Id);
    }
    
    public void deleteNotes(int Id){
        Note nt = new Note();
        nt.deleteByPk(Id);
    }
    
    public String getSelectedSummary(){
        String output="";
        int currentTab = getFrame().getCurrentTab();
         switch(currentTab){
            case 0:
                output = this.getVisitorSummary();
                break;
            case 1:
                output = this.getMessageSummary();
                break;
            case 2:
                output = this.getAppointmentSummary();
                break;
            case 3:
                output = this.getNoteSummary();
                break;
            default:
                break;
         }
        return output;
    }
    
    public void sendTextToClipBoard(){
        String myString = this.getSelectedSummary();
        StringSelection stringSelection = new StringSelection(myString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
    
    public String getVisitorSummary(){
        int index = getFrame().visitorsTable.getSelectedRow();
        if(index >= 0 ){
            int visitorId = Integer.valueOf(getFrame().visitorsTable.getModel().getValueAt(index, 0).toString());
            Visitor vs = new Visitor();
            vs.findByPk(visitorId);
            return vs.getSummary();   
        }else{
            return "";
        }
    }
    
        
    public String getMessageSummary(){
        int index = getFrame().messageTable.getSelectedRow();
        if(index >= 0 ){
            int msgId = Integer.valueOf(getFrame().messageTable.getModel().getValueAt(index, 0).toString());
            Message msg = new Message();
            msg.findByPk(msgId);
            return msg.getSummary();   
        }else{
            return "";
        }
    }
    
    public String getAppointmentSummary(){
        int index = getFrame().appointmentTable.getSelectedRow();
        if(index >=0 ){
            int appId = Integer.valueOf(getFrame().appointmentTable.getModel().getValueAt(index, 0).toString());
            Appointment app = new Appointment();
            app.findByPk(appId);
            return app.getSummary();
        }else{
            return "";
        }
    }
    
    public String getNoteSummary(){
        int index = getFrame().noteTable.getSelectedRow();
        if(index >=0 ){
            int noteId = Integer.valueOf(getFrame().noteTable.getModel().getValueAt(index, 0).toString());
            Note note = new Note();
            note.findByPk(noteId);
            return note.getSummary();
        }else{
            return "";
        }
    }
    
    public void openSettingsForm(){
        SettingsForm setForm = new SettingsForm(getFrame(),true);
        setForm.setLocationRelativeTo(null);
        setForm.setTitle("Edit Settings");
        setForm.setContext(getFrame());
        setForm.setVisible(true);
        
    }

    
    public void startUpActions(){
  
    }
    
    public String defaultDateFormat(){
       AppProperties prop = new AppProperties();
       return prop.getDefaultDate();
    }
    
    public String defaultTimeFormat(){
        AppProperties prop = new AppProperties();
        return prop.getDefaultTime();
    }
    
    public void openExportForm(){
        ExportForm exportForm = new ExportForm(getFrame(),true);
        exportForm.setLocationRelativeTo(null);
        exportForm.setTitle("Export tables to excel");
        exportForm.setVisible(true);
    }
    
    public void openImportForm(){
        ImportForm importForm = new ImportForm(getFrame(),true);
        importForm.setLocationRelativeTo(null);
        importForm.setTitle("Import tables from excel");
        importForm.setVisible(true);
    }
    
    public void exportVisitors(String filename){
        Visitor vs = new Visitor();
        try {
            vs.saveToExcel(filename);
        } catch (IOException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exportMessages(String filename){
        Message msg = new Message();
        try {
            msg.saveToExcel(filename);
        } catch (IOException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exportAppointments(String filename){
        Appointment app = new Appointment();
        try {
            app.saveToExcel(filename);
        } catch (IOException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exportNotes(String filename){
        Note nt = new Note();
        try {
            nt.saveToExcel(filename);
        } catch (IOException ex) {
            Logger.getLogger(AppController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void importVisitors(String filename){
        File file = new File(filename);
        Visitor vs = new Visitor();
        vs.importFromExcel(file);
    }
    
    public void importMessages(String filename){
        File file = new File(filename);
        Message msg = new Message();
        msg.importFromExcel(file);
    }
    
    public void importAppointments(String filename){
        File file = new File(filename);
        Appointment app = new Appointment();
        app.importFromExcel(file);
    }
    
    public void importNotes(String filename){
        File file = new File(filename);
        Note nt = new Note();
        nt.importFromExcel(file);
    }
    
    public void exportSelectedMessages(List<Integer> ids, String filename){
        Message msg = new Message();
        try {
            msg.saveToExcel(ids, filename);
        } catch (IOException ex) {
            Logger.getLogger(HomeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exportSelectedVisitors(List<Integer>ids, String filename){
        Visitor vs = new Visitor();
        try{
            vs.saveToExcel(ids, filename);
        }catch (IOException ex) {
            Logger.getLogger(HomeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exportSelectedNotes(List<Integer>ids, String filename){
        Note note = new Note();
        try{
            note.saveToExcel(ids, filename);
        }catch (IOException ex) {
            Logger.getLogger(HomeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exportSelectedAppointments(List<Integer>ids, String filename){
        Appointment app = new Appointment();
        try{
            app.saveToExcel(ids, filename);
        }catch (IOException ex) {
            Logger.getLogger(HomeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
}
