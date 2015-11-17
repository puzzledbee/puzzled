/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Fred
 */
public class LogicProblem implements Serializable {
    private ArrayList<String> categories = new ArrayList<String>();
    private ArrayList<String> items = new ArrayList<String>();
    
    public LogicProblem(){
        System.out.println("constructor invoked");
        categories.add("Age");
        categories.add("test");
    } 
    
    public ArrayList<String> getCategories() {
        return categories;
    }
    
    @Override
    public String toString(){
        String output = new String();
        
        for(String quark: categories){
             output += "categories: " + quark + "\n";
         }
        return output;
    }
    
}
