/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;




/**
 *
 * @author Fred
 */
public class Category {
    
    public enum CategoryType {
        NORMAL,
        NUMERICAL, //mathematical computation possible
        TIME, //time handling
        TEXT_NUMERICAL   //requires interpretation of text into value e.g. days/months/ranking
    }
    
    
    private StringProperty categoryName = new SimpleStringProperty();
    private CategoryType categoryType = CategoryType.NORMAL;
    private List<Item> items = new ArrayList<Item>();
    
    public Category(String myTitle,List<Item> myItems) {
        categoryName.set(myTitle);
        items = myItems;
    }
    public void setType(CategoryType type_arg) {
        categoryType = type_arg;
    }
    public CategoryType getType(){
        return categoryType;
    }
    
    public void setText(String myText){
        categoryName.set(myText);
    }
    
    public String getText(){
        return categoryName.getValue();
    }
    
    @Override
    public String toString() {
        return "name="+categoryName.getValue();
    }
    
    public List<Item> getItems(){
        return items;
    }
    
}
