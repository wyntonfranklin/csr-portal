/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author shady
 */
public class PortalCalendar {
    
    private Calendar cal;
    private Calendar calManager;
    private Date currentDate;
    private Date currentWeekDate;
    
    
    
    public PortalCalendar(){
        cal = Calendar.getInstance();
        calManager = Calendar.getInstance();
    }
    
    private void init(){
        
    }
    
    public Calendar getCalManager(){
        return calManager;
    }
    
    public Date getCurrentDate(){
       return getCal().getTime();
    }

    public Calendar getCal() {
        return cal;
    }

    public void setCal(Calendar cal) {
        this.cal = cal;
    }
    
    
    public void setCalendarWeek(Date dt ){
        currentDate = dt;
        cal.setTime(dt);
        currentWeekDate = calculateWeekDate(dt);
        //cal.set(Calendar.DAY_OF_MONTH, cal.getFirstDayOfWeek());
    }
    
    public Date getSelectedCurrentDate(){
        return currentDate;
    }
    
    public int getSelectedDayOfMonth(){
        Calendar tempCal = getCalManager();
        tempCal.setTime(getSelectedCurrentDate());
        return tempCal.get(Calendar.DAY_OF_MONTH);
    }
    
  
    
    public Date getCurrentWeekDate(){
        return currentWeekDate;
    }
    
    public int getDayOfMonth(Date aDate) {
        Calendar g_cal = Calendar.getInstance();
        g_cal.setTime(aDate);
        return g_cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public Date getToday(){
        Calendar g_cal = Calendar.getInstance();
        Date date = new Date();
        g_cal.setTime(date);
        return g_cal.getTime(); 
    }
    
  
    
    public void resetCalendar(){
        cal.set(getSelectedDayOfMonth(), cal.getFirstDayOfWeek());
    }
    
    public void setCalendarDate( int offset ){
        int correctOffset = offset+1;
        cal.add(Calendar.DATE, correctOffset);
        System.out.println(cal.getTime().toString());
    }
    

    public Date calculateWeekDate( Date dt ){
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(dt);
        tempCal.set(Calendar.DAY_OF_WEEK, tempCal.getFirstDayOfWeek());
        return tempCal.getTime();
    }
    
    public int currentWeekDay( Date dt ){
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTime(dt);
        return tempCal.get(Calendar.DAY_OF_WEEK); 
    }
    
}
