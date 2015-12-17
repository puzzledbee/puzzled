/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package puzzled.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Fred
 */
@XmlRootElement
public class Item {
    
    private StringProperty itemName = new SimpleStringProperty();
    
    //necessary for unmarshalling
    public Item() {
    }
    
    public Item(String mytext){
        itemName.set(mytext);
    }
    
    public void setText(String newText) {
        itemName.set(newText);
    }
    
    @XmlElement
    public String getText() {
        return itemName.getValue();
    }
    
}
