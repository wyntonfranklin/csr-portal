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
    
    
    public PortalCalendar(){
        cal = Calendar.getInstance();
    }
    
    private void init(){
        
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
        cal.setTime(dt);
        cal.set(Calendar.DAY_OF_MONTH, cal.getFirstDayOfWeek());
    }
    
    public void setCalendarDate(){
        
    }
    
    public int getDayOfMonth(Date aDate) {
        Calendar g_cal = Calendar.getInstance();
        g_cal.setTime(aDate);
        return g_cal.get(Calendar.DAY_OF_MONTH);
    }
    
    
    public void updateCalendar(){
        
    }
    
    public void resetCalendar(){
        cal.set(Calendar.DAY_OF_MONTH, cal.getFirstDayOfWeek());
    }
    
    public void setCalendar( int offset ){
        int correctOffset = offset+1;
        cal.add(Calendar.DATE, correctOffset);
        System.out.println(cal.getTime().toString());
    }
    
}
