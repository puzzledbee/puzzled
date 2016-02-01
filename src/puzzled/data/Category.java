/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author phiv
 */
@XmlRootElement
@XmlType(propOrder={"name","type","items"})
public class Category {
    
    //strings are used as decorator background colour through a binding
    public enum CategoryType {
        NORMAL ("transparent"), //no decorator shown
        NUMERICAL ("red"), //mathematical computation possible
        TIME ("green"), //time handling (force 24 hrs representation)
        DATE ("purple"), 
        ORDINAL ("orange"),   //requires interpretation of text into value e.g. days/months/ranking
        ORDINAL_WITH_WRAPAROUND ("brown");
        
        public String color;
        
        CategoryType(String color) {
            this.color = color;
        }
        
    }
    
//    @XmlElement
    private StringProperty nameProperty = new SimpleStringProperty();
    

    private LogicProblem parent;
    
    private ObjectProperty<CategoryType> categoryTypeProperty = new SimpleObjectProperty<CategoryType>(CategoryType.NORMAL);
    
    @XmlElement
    private List<Item> items = new ArrayList<Item>();
    
    //necessary for unmarshalling
    public Category(){
        
    }
    
    public Category(String myTitle,List<Item> myItems) {
        nameProperty.set(myTitle);
        items = myItems;
    }
    
    public Category(List<Item> myItems, String... catInfo){ //will set text and type
        items = myItems;
        nameProperty.set(catInfo[0].trim());
        if (catInfo.length>1) categoryTypeProperty.set(CategoryType.valueOf(catInfo[1].trim()));
    }
    
    public void setParent(LogicProblem arg_parent){
        this.parent = arg_parent;
    }
    
    @XmlTransient
    public LogicProblem getParent() {
        return parent;
    }
    
    public void setType(CategoryType type_arg) {
        categoryTypeProperty.set(type_arg);
    }
    public CategoryType getType(){
        return categoryTypeProperty.getValue();
    }
    
    public void setName(String myText){
        nameProperty.set(myText);
    }
    
    @XmlElement
    public String getName(){
        return this.nameProperty.getValue();
    }
    
    public StringProperty nameProperty(){
        return this.nameProperty;
    }
    
    
    
    public ObjectProperty<CategoryType> typeProperty() {
        return this.categoryTypeProperty;
    }
    
    @Override
    public String toString() {
        return "name="+nameProperty.getValue();
    }
    
    public List<Item> getItems(){
        return items;
    }
    
    public int getNumItems(){
        return items.size();
    }
}
