/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fred
 */
public class LogicProblem implements Serializable {
    private StringProperty problemTitle = new SimpleStringProperty();
    private String problemSource;
    
    private List<Category> categories;
    private int numCategories;
    private int numItems;
    
    
    private List<Clue> clues;
    private BooleanProperty ordinal = new SimpleBooleanProperty(false);
    
    public LogicProblem(String title, int category_number, int item_number){
        System.out.println("constructor invoked");
        problemTitle.set(title);
        numCategories = category_number;
        numItems = item_number;
        categories = new ArrayList<Category>(numCategories);
        //categories.add("Age");
        //categories.add("test");
    }
    
    public LogicProblem(int category_number, int item_number){
        this("",category_number,item_number);
    }
    
    
    public List<Category> getCategories() {
        return categories;
    }
    
    public void addCategory(Category newCategory) {
        categories.add(newCategory);
    }
    
    public void addClue(Clue newClue){
        clues.add(newClue);
    }
    
    public void removeClue(Clue oldClue){
        clues.remove(oldClue);
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
