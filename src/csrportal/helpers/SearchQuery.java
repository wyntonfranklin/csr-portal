/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csrportal.helpers;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author shady
 */
public class SearchQuery {
    
    private String colname;
    private String colType;
    public static String OP_OR = "OR";
    public static String OP_AND = "AND";
    private String spacer = " ";
    private List<String> queryString;
    private List<String> queryOperators;
    
    public SearchQuery(){
        queryString = new ArrayList<>();
        queryOperators = new ArrayList<>();
    }
    
    public void likeQuery( String col, String value, String operator ){
        String q = col + " LIKE '%" + value + "%'";
        this.addQuery(q, operator );
    }
    
    public void equalQuery( String col, String value, String operator ){
        String q = col + "=" + value;
        this.addQuery(q, operator );
    }
    
    public void addQuery( String q, String op ){
        queryString.add(q);
        queryOperators.add(op);
    }
    
    public String getSearchQuery(){
        String fullQuery = ""; 
        int counter = 0;
        int maxParams = queryString.size()-1;
        for( String s : queryString ){
            if(counter > 0 && counter < maxParams){
                fullQuery += queryOperators.get(counter) + spacer + s + spacer;   
            }else if( counter == maxParams){
                fullQuery += queryOperators.get(counter) + spacer + s + spacer;
            }else{
                fullQuery += s + spacer; 
            }
            counter += 1;
        }
        return fullQuery;
    }
    
    
}
