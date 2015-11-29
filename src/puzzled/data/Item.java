/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package puzzled.data;

import javafx.beans.property.SimpleStringProperty;



/**
 *
 * @author Fred
 */
public class Item {
    
    private SimpleStringProperty text = new SimpleStringProperty();
    
    public void setText(String newText) {
        text.set(newText);
    }
    
    public String getText() {
        return text.getValue();
    }
    
}
