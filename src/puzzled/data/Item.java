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
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Fred
 */
@XmlRootElement
public class Item {
    
    private StringProperty nameProperty = new SimpleStringProperty();
    @XmlTransient
    private Category parent;
    
    //necessary for unmarshalling
    public Item() {
    }
    
    public Item(String mytext){
        nameProperty.set(mytext);
    }
    
    public void setParent(Category arg_parent){
        this.parent = arg_parent;
    }
    
    public void setName(String newText) {
        nameProperty.set(newText);
    }
    
    @XmlElement
    public String getName() {
        return nameProperty.getValue();
    }
    
    public StringProperty nameProperty(){
        return nameProperty;
    }
    
    public int getItemIndex() {
        return parent.getItems().indexOf(this);
    }
    public int getCatIndex() {
        return parent.getParent().getCategories().indexOf(parent);
    }
    
}
