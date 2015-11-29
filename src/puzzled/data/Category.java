/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Fred
 */
public class Category {
    private SimpleStringProperty text = new SimpleStringProperty();
    
    private ArrayList<Item> items = new ArrayList<Item>();
    
    public void setText(String myText){
        text.set(myText);
    }
    
    public String getText(){
        return text.getValue();
    }
    
    public ArrayList<Item> getItems(){
        return items;
    }
    
}
