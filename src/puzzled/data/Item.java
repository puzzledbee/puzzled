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
 * @author phiv
 */
@XmlRootElement
public class Item implements DataElement {
    
    private StringProperty nameProperty = new SimpleStringProperty();
//    @XmlTransient
    
    //it is necessary for a given item to have a relationship (pointer) to
    //its' parent category so that 
    //two Items can be compared and be different if they are of the same "name"
    //but are in different category
    //this is useful if for example two categories both use colours or times
    //e.g. arrival and departure times
    
    private Category parentCategory;
    
    //necessary for unmarshalling
    public Item() {
    }
    
    public Item(String mytext, Category category){
        nameProperty.set(mytext);
        this.parentCategory = category;
    }
    
    public void setParentCategory(Category arg_parent){
        this.parentCategory = arg_parent;
    }
    
    public Category getParentCategory() {
        return this.parentCategory;
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
        return parentCategory.getItems().indexOf(this);
    }
    public int getCatIndex() {
        return parentCategory.getParent().getCategoriesList().indexOf(parentCategory);
    }
    
    @Override
    public String toString(){
        //return this.nameProperty.getValue() + "(" + this.parent.toString() + ")";
        return this.nameProperty.getValue();
    }
}
