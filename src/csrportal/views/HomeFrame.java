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
import csrportal.models.Message;
import csrportal.models.Note;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
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
    
    public AppController controller;
    
    
    public HomeFrame() {
        initComponents();
        controller = new AppController(this);
        initFunctions();
    }
 
    
    private void initFunctions(){
        pc = new PortalCalendar();
        pc.setCalendarWeek(Calendar.getInstance().getTime());
        setWeekDays();
        setSelectedDay(pc.getCurrentDate());
        addTabbedChangedListener();
    }
    
    public int getCurrentTab(){
        return this.currentTab;
    }

    
    public void addTabbedChangedListener(){
        jTabbedPane1.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
                currentTab = jTabbedPane1.getSelectedIndex();
               // setTableFilter();
                refreshTable();
                
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
    }
    
    public void setCalendarDate(Date dt){
        calendarView.setDate(dt);
        calendarDateChanged(); 
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
    
    
    public String getDateField(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(calendarView.getDate());
        return strDate;
    }
    
    public void refreshTable(){
        switch (currentTab) {
            case 0:
                controller.loadVisitorsTable();
                break;
            case 1:
                controller.loadMessageTable();
                break;
            case 2:
                controller.loadAppointmentTable();
                break;
            case 3:
                controller.loadNoteTable();
                break;
            default:
                break;
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
    
    
    public void onAddButtonPressed(){
        switch (currentTab) {
            case 0:
                controller.openVisitorForm();
                break;
            case 1:
                controller.openMessageForm();
                break; 
            case 2:
                controller.openAppointmentForm();
                break;
            case 3:
                controller.openNoteForm();
                break;
            default:
                break;
        }
    }
    
    
    public void onTodayButtonPressed(){
        this.setCalendarDate(pc.getToday());
        this.refreshTable();
    }
    
    public void onSendEmailButtonPressed(){
        controller.openSendEmail();
    }
    
    public void onSearchButtonPressend(){
        controller.openSearchForm();
    }
    
    public void copyRecord(){
        controller.sendTextToClipBoard();
    }
    
    public int getCurrentSelectedRow(){
        switch (currentTab) {
            case 0:
                return visitorsTable.getSelectedRow();
            case 1:
                return messageTable.getSelectedRow();
            case 2:
                return appointmentTable.getSelectedRow();
            case 3:
                return noteTable.getSelectedRow();
            default:
                break;
        }
        return 0;
    }
    
    public javax.swing.JTable getCurrentTable(){
        switch (currentTab) {
            case 0:
                return visitorsTable;
            case 1:
                return messageTable;
            case 2:
                return appointmentTable;
            case 3:
                return noteTable;
            default:
                break;
        }
       return visitorsTable;
    }
    
    public void editTable(){
        int index = this.getCurrentSelectedRow();
        int modelId = Integer.valueOf(this.getCurrentTable().getModel().getValueAt(index, 0).toString());
        this.openTableEditForm(modelId);
    }
    
    public void deleteTableRecord(){
        int index = this.getCurrentSelectedRow();
        int modelId = Integer.valueOf(this.getCurrentTable().getModel().getValueAt(index, 0).toString());
             switch (currentTab) {
            case 0:
                this.controller.deleteVisitor(modelId);
                break;
            case 1:
                this.controller.deleteMessages(modelId);
                break;
            case 2:
                this.controller.deleteAppointment(modelId);
                break;
            case 3:
                this.controller.deleteNotes(modelId);
               break;
            default:
                break;
        }
        this.refreshTable();
    }
    
    public void openTableEditForm( int objectId ){
        switch (currentTab) {
            case 0:
                this.controller.editVisitor(objectId);
                break;
            case 1:
                this.controller.editMessage(objectId);
                break;
            case 2:
                this.controller.editAppointment(objectId);
                break;
            case 3:
                this.controller.editNote(objectId);
                break;
            default:
                break;
        }
    }
    
    public void tableClicked(java.awt.event.MouseEvent evt){
        if( evt.getClickCount() == 2 ){
            this.editTable();
        }
        if(SwingUtilities.isRightMouseButton(evt)){
            this.tableLeftClicked(evt);
        }
    }
    
    public void tableLeftClicked(java.awt.event.MouseEvent evt){
        javax.swing.JTable table = this.getCurrentTable();
        int r = table.rowAtPoint(evt.getPoint());
        if (r >= 0 && r < table.getRowCount()) {
            table.setRowSelectionInterval(r, r);
        } else {
            table.clearSelection();
        }
        tableMenu.show(evt.getComponent(), evt.getX(), evt.getY());
    }
    
    public void exportCurrentSelection(){
        int [] rows = this.getCurrentTable().getSelectedRows();
        String errorMessage ="No rows selected.";
        if(rows.length>0){
            List<Integer> myList = new ArrayList<>();
            for(int i = 0; i < rows.length; i++){
                int id =  Integer.valueOf(this.getCurrentTable().getModel().getValueAt(rows[i], 0).toString());
                System.out.println(id);
                myList.add(id);
            }
            this.exportSelection(myList, "msg.xls");   
        }else{
            JOptionPane.showMessageDialog(null, errorMessage);   
        }
    }
    
    public void exportSelection(List<Integer>ids, String filename){
        switch (currentTab) {
            case 0:
                this.controller.exportSelectedVisitors(ids, filename);
                break;
            case 1:
                this.controller.exportSelectedMessages(ids, filename);
                break;
            case 2:
                this.controller.exportSelectedAppointments(ids, filename);
                break;
            case 3:
                this.controller.exportSelectedNotes(ids, filename);
                break;
            default:
        }
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
        tableMenu = new javax.swing.JPopupMenu();
        editMenuItem = new javax.swing.JMenuItem();
        rightCopyMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        jPanel2 = new javax.swing.JPanel();
        calendarView = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        DaysList = new javax.swing.JList<>();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        visitorsTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        messageTable = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        searchField1 = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        scorllPane1 = new javax.swing.JScrollPane();
        appointmentTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        searchField2 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        noteTable = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        searchField3 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        exportButton = new javax.swing.JButton();
        addNewButton = new javax.swing.JButton();
        todayButton = new javax.swing.JButton();
        sendEmailButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        noteMenuItem = new javax.swing.JMenuItem();
        importMenuItem = new javax.swing.JMenuItem();
        exportMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        quitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        copyMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        settingsMenuItem = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        contentMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        editMenuItem.setText("Edit");
        editMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editMenuItemActionPerformed(evt);
            }
        });
        tableMenu.add(editMenuItem);

        rightCopyMenuItem.setText("Copy");
        rightCopyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightCopyMenuItemActionPerformed(evt);
            }
        });
        tableMenu.add(rightCopyMenuItem);

        deleteMenuItem.setText("Delete");
        deleteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteMenuItemActionPerformed(evt);
            }
        });
        tableMenu.add(deleteMenuItem);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setPreferredSize(new java.awt.Dimension(491, 1013));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

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

        DaysList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                DaysListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(DaysList);

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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 842, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Visitors", null, jPanel4, "");

        messageTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                messageTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(messageTable);

        jLabel3.setText("Search this Table:");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField1)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(searchField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Messages", jPanel5);

        appointmentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                appointmentTableMouseClicked(evt);
            }
        });
        scorllPane1.setViewportView(appointmentTable);

        jLabel4.setText("Search this Table:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scorllPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField2)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(scorllPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Appointments", jPanel6);

        noteTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                noteTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(noteTable);

        jLabel5.setText("Search this Table:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField3)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Notes", jPanel7);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        exportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Excel_32.png"))); // NOI18N
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });

        addNewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/New File-32.png"))); // NOI18N
        addNewButton.setToolTipText("Add New Item");
        addNewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addNewButtonActionPerformed(evt);
            }
        });

        todayButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/Calendar-32.png"))); // NOI18N
        todayButton.setToolTipText("Show Today");
        todayButton.setDefaultCapable(false);
        todayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                todayButtonActionPerformed(evt);
            }
        });

        sendEmailButton.setIcon(new javax.swing.ImageIcon("/home/shady/Downloads/Email-32.png")); // NOI18N
        sendEmailButton.setToolTipText("Send Email");
        sendEmailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendEmailButtonActionPerformed(evt);
            }
        });

        searchButton.setIcon(new javax.swing.ImageIcon("/home/shady/Downloads/Search-32.png")); // NOI18N
        searchButton.setToolTipText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(addNewButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(todayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sendEmailButton, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exportButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addNewButton, javax.swing.GroupLayout.DEFAULT_SIZE, 56, Short.MAX_VALUE)
            .addComponent(todayButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(sendEmailButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(exportButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(searchButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        jMenu3.setText("New");

        jMenuItem1.setText("Add Visitor");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem1);

        jMenuItem2.setText("Add Message");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setText("Add Appointment");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);

        noteMenuItem.setText("Add Note");
        noteMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noteMenuItemActionPerformed(evt);
            }
        });
        jMenu3.add(noteMenuItem);

        jMenu1.add(jMenu3);

        importMenuItem.setText("Import");
        importMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(importMenuItem);

        exportMenuItem.setText("Export");
        exportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(exportMenuItem);
        jMenu1.add(jSeparator2);

        quitMenuItem.setText("Quit");
        quitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(quitMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        copyMenuItem.setText("Copy");
        copyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(copyMenuItem);
        jMenu2.add(jSeparator1);

        settingsMenuItem.setText("Preferences");
        settingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(settingsMenuItem);

        jMenuBar1.add(jMenu2);

        jMenu4.setText("Help");

        contentMenuItem.setText("Content");
        contentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                contentMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(contentMenuItem);

        aboutMenuItem.setText("About");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuItemActionPerformed(evt);
            }
        });
        jMenu4.add(aboutMenuItem);

        jMenuBar1.add(jMenu4);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane1)
                            .addComponent(calendarView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(calendarView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 923, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addNewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addNewButtonActionPerformed
        // TODO add your handling code here:
        onAddButtonPressed();
    }//GEN-LAST:event_addNewButtonActionPerformed

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

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        controller.openVisitorForm();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
        controller.openMessageForm();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        controller.openAppointmentForm();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuItemActionPerformed
        // TODO add your handling code here:
        this.copyRecord();
    }//GEN-LAST:event_copyMenuItemActionPerformed

    private void settingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsMenuItemActionPerformed
        // TODO add your handling code here:
        controller.openSettingsForm();
    }//GEN-LAST:event_settingsMenuItemActionPerformed

    private void quitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitMenuItemActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_quitMenuItemActionPerformed

    private void contentMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_contentMenuItemActionPerformed
        // TODO add your handling code here:
        this.controller.openContentForm();
    }//GEN-LAST:event_contentMenuItemActionPerformed

    private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
        // TODO add your handling code here:
        this.controller.openAboutForm();
    }//GEN-LAST:event_aboutMenuItemActionPerformed

    private void noteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noteMenuItemActionPerformed
        // TODO add your handling code here:
        this.controller.openNoteForm();
    }//GEN-LAST:event_noteMenuItemActionPerformed

    private void editMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editMenuItemActionPerformed
        // TODO add your handling code here:
        this.editTable();
    }//GEN-LAST:event_editMenuItemActionPerformed

    private void rightCopyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightCopyMenuItemActionPerformed
        // TODO add your handling code here:
        this.copyRecord();
    }//GEN-LAST:event_rightCopyMenuItemActionPerformed

    private void deleteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteMenuItemActionPerformed
        // TODO add your handling code here:
        this.deleteTableRecord();
    }//GEN-LAST:event_deleteMenuItemActionPerformed

    private void exportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMenuItemActionPerformed
        // TODO add your handling code here:
        this.controller.openExportForm();
    }//GEN-LAST:event_exportMenuItemActionPerformed

    private void importMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importMenuItemActionPerformed
        // TODO add your handling code here:
        this.controller.openImportForm();
    }//GEN-LAST:event_importMenuItemActionPerformed

    private void noteTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_noteTableMouseClicked
        // TODO add your handling code here:
        this.tableClicked(evt);
    }//GEN-LAST:event_noteTableMouseClicked

    private void appointmentTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_appointmentTableMouseClicked
        // TODO add your handling code here:
        this.tableClicked(evt);
    }//GEN-LAST:event_appointmentTableMouseClicked

    private void messageTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_messageTableMouseClicked
        // TODO add your handling code here:
        this.tableClicked(evt);
    }//GEN-LAST:event_messageTableMouseClicked

    private void visitorsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_visitorsTableMouseClicked
        // TODO add your handling code here:
        this.tableClicked(evt);
    }//GEN-LAST:event_visitorsTableMouseClicked

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed
        // TODO add your handling code here:
        this.exportCurrentSelection();
    }//GEN-LAST:event_exportButtonActionPerformed

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
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JButton addNewButton;
    public javax.swing.JTable appointmentTable;
    private org.jdesktop.swingx.JXDatePicker calendarView;
    private javax.swing.JMenuItem contentMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private org.jdesktop.swingx.plaf.DatePickerAddon datePickerAddon1;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenuItem editMenuItem;
    private javax.swing.JButton exportButton;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JMenuItem importMenuItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    public javax.swing.JTabbedPane jTabbedPane1;
    public javax.swing.JTable messageTable;
    private javax.swing.JMenuItem noteMenuItem;
    public javax.swing.JTable noteTable;
    private javax.swing.JMenuItem quitMenuItem;
    private javax.swing.JMenuItem rightCopyMenuItem;
    private javax.swing.JScrollPane scorllPane1;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JTextField searchField1;
    private javax.swing.JTextField searchField2;
    private javax.swing.JTextField searchField3;
    private javax.swing.JButton sendEmailButton;
    private javax.swing.JMenuItem settingsMenuItem;
    private javax.swing.JPopupMenu tableMenu;
    private javax.swing.JButton todayButton;
    public javax.swing.JTable visitorsTable;
    // End of variables declaration//GEN-END:variables
}
