/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package puzzled.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Fred
 */
public class Item {
    
    private StringProperty itemText = new SimpleStringProperty();
    
    public Item(String mytext){
        itemText.set(mytext);
    }
    
    public void setText(String newText) {
        itemText.set(newText);
    }
    
    public String getText() {
        return itemText.getValue();
    }
    
}
