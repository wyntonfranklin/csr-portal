/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal.views;

import csrportal.AppController;
import csrportal.PortalCalendar;
import csrportal.models.Visitor;
import csrportal.helpers.TableWidget;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.RowFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author shady
 */
public class HomeFrame extends javax.swing.JFrame {

    /**
     * Creates new form HomeFrame
     * 
     */
    
    private int currentTab = 0;
    
    public PortalCalendar pc;
    
    private TableRowSorter<TableModel> rowSorter;
    
    
    public HomeFrame() {
        initComponents();
        initFunctions();
    }
 
    
    private void initFunctions(){
        pc = new PortalCalendar();
        pc.setCalendarWeek(Calendar.getInstance().getTime());
        setWeekDays();
        setSelectedDay(pc.getCurrentDate());
        addTabbedChangedListener();
      //  setTableFilter();
       // setSearchInputListener();
    }    
    
    public void addTabbedChangedListener(){
        jTabbedPane1.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
                System.out.println("Tab: " + jTabbedPane1.getSelectedIndex());
                currentTab = jTabbedPane1.getSelectedIndex();
                setTableFilter();
            }
        });
    }
    
    public void setWeekDays(){
        DaysList.setModel(AppController.getWeekDaysModel());
    }
    
    public void setCalendarWeek(Date dt ){
        pc.setCalendarWeek(dt);
    }
    
    public void setSelectedDay(Date dt){
        calendarView.setDate(dt);
       // calendarDateChanged();
    }
    
    public void setCalendarDate(Date dt){
        calendarView.setDate(dt);
        calendarDateChanged(); 
    }
    
    
    public void setDateToday(){
        
    }
    
    public void setTableFilter(){
        if( currentTab == 0 ){
            rowSorter = new TableRowSorter<>(visitorsTable.getModel());
            visitorsTable.setRowSorter(rowSorter);   
        }
    }
    
    public void setSearchInputListener(){
        searchField.getDocument().addDocumentListener(new DocumentListener(){

            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                   rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = searchField.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // rowSorter.setRowFilter(null);//To change body of generated methods, choose Tools | Templates.
            }

        });
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
        visitorsTable.setModel(tb.getModel());
        visitorsTable.removeColumn(visitorsTable.getColumnModel().getColumn(0));
    }
    
    public String visitorsQuery(){
        String query = "visit_date LIKE \"%{calendar_date}%\"";
        return query
                .replace("{calendar_date}", getDateField());
    }
    
    public String getDateField(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(calendarView.getDate());
        return strDate;
    }
    
    public void refreshTable(){
        if( currentTab == 0 ){
            loadVisitorsTable();   
        }
    }
    
    
     public void setCalendarDate( int offset ){
        Calendar tempCal =  Calendar.getInstance();
        tempCal.setTime(pc.getCurrentWeekDate());
        tempCal.add(Calendar.DATE, offset);
        setSelectedDay(tempCal.getTime());
    }
     
    public void calendarDateChanged(){
        pc.setCalendarWeek(calendarView.getDate());   
        DaysList.setSelectedIndex(pc.currentWeekDay(calendarView.getDate())-1);
        this.refreshTable();
    }
    
    public Visitor findVisitor(int Id ){
        Visitor vs = new Visitor();
        vs.findByPk(Id);
        return vs;
    }
    
    public void editVisitor(int Id ){
        Visitor vs = findVisitor(Id);
        System.out.println(vs.getFullName());
        AddVisitor vePopup = new AddVisitor(this, true, vs);
        vePopup.setLocationRelativeTo(null);
        vePopup.setTitle("Visitor Details");
        vePopup.loadVisitor();
        vePopup.setVisible(true);
    }
    
    public void onAddButtonPressed(){
        if( currentTab == 0 ){
            openVisitorForm();
        }
    }
    
    public void openVisitorForm(){
        AddVisitor visitorForm = new AddVisitor(this, true, new Visitor());
        visitorForm.setTitle("Add Visitor");
        visitorForm.setLocationRelativeTo(null);
        visitorForm.setVisible(true);
    }
    
    public void openSendEmail(){
        SendEmail emailForm = new SendEmail(this,true);
        emailForm.setTitle("Send Email");
        emailForm.setLocationRelativeTo(null);
        emailForm.setVisible(true);
    }
    
    public void openSearchForm(){
        SearchForm fm = new SearchForm(this,true);
        fm.setTitle("Search Current Table");
        fm.setLocationRelativeTo(null);
        fm.setVisible(true);
    }
    
    public void onTodayButtonPressed(){
        if( currentTab == 0 ){
            this.setCalendarDate(pc.getToday());
        }
    }
    
    public void onSendEmailButtonPressed(){
        if( currentTab == 0 ){
            openSendEmail();
        }
    }
    
    public void onSearchButtonPressend(){
        openSearchForm();
    }
    
    public void searchTable(){
        Visitor vm = new Visitor();
        vm.searchDb("hello");
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        datePickerAddon1 = new org.jdesktop.swingx.plaf.DatePickerAddon();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DaysList = new javax.swing.JList<>();
        calendarView = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        sendEmailButton = new javax.swing.JButton();
        todayButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        visitorsTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        DaysList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DaysListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(DaysList);

        calendarView.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                calendarViewMouseClicked(evt);
            }
        });
        calendarView.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                calendarViewPropertyChange(evt);
            }
        });
        calendarView.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                calendarViewVetoableChange(evt);
            }
        });

        jLabel1.setText("Days of the Week");

        jButton1.setLabel("Add");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setLabel("Import");

        jButton3.setText("Export");

        sendEmailButton.setText("Email");
        sendEmailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendEmailButtonActionPerformed(evt);
            }
        });

        todayButton.setText("Today");
        todayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todayButtonActionPerformed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(searchButton)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sendEmailButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(todayButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(sendEmailButton)
                .addGap(18, 18, 18)
                .addComponent(todayButton)
                .addGap(18, 18, 18)
                .addComponent(searchButton)
                .addContainerGap(171, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(calendarView, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 213, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(calendarView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        visitorsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                visitorsTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(visitorsTable);

        jLabel2.setText("Search this Table:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(6, 6, 6)
                .addComponent(searchField))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Visitors", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 441, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Messages", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 479, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 441, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Appointments", jPanel6);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 484, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        onAddButtonPressed();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void visitorsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visitorsTableMouseClicked
        // TODO add your handling code here:
        int index = visitorsTable.getSelectedRow();
        if( evt.getClickCount() == 2 ){
            int evtId = Integer.valueOf(visitorsTable.getModel().getValueAt(index, 0).toString());
            this.editVisitor(evtId);
        }
    }//GEN-LAST:event_visitorsTableMouseClicked

    private void DaysListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_DaysListMouseClicked
        // TODO add your handling code here:
         if( evt.getClickCount() == 2 ){
            int currentDay = DaysList.getSelectedIndex(); 
            setCalendarDate(currentDay);
         }
    }//GEN-LAST:event_DaysListMouseClicked

    private void calendarViewMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calendarViewMouseClicked
        // TODO add your handling code here:
        if( evt.getClickCount() == 1){
            pc.setCalendarWeek(calendarView.getDate());
     
        }
    }//GEN-LAST:event_calendarViewMouseClicked

    private void calendarViewPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_calendarViewPropertyChange
        // TODO add your handling code here:
            //System.out.println("calendar clicked");
            if( calendarView.getDate() != null ){
                calendarDateChanged();
            }
    }//GEN-LAST:event_calendarViewPropertyChange

    private void calendarViewVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_calendarViewVetoableChange
        // TODO add your handling code here:
    }//GEN-LAST:event_calendarViewVetoableChange

    private void todayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_todayButtonActionPerformed
        // TODO add your handling code here:
        this.onTodayButtonPressed();
    }//GEN-LAST:event_todayButtonActionPerformed

    private void sendEmailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendEmailButtonActionPerformed
        // TODO add your handling code here:
        onSendEmailButtonPressed();
    }//GEN-LAST:event_sendEmailButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
        onSearchButtonPressend();
    }//GEN-LAST:event_searchButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JList<String> DaysList;
    private org.jdesktop.swingx.JXDatePicker calendarView;
    private org.jdesktop.swingx.plaf.DatePickerAddon datePickerAddon1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JButton sendEmailButton;
    private javax.swing.JButton todayButton;
    private javax.swing.JTable visitorsTable;
    // End of variables declaration//GEN-END:variables
}
