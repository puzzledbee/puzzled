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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 *
 * @author Fred
 */
@XmlRootElement
@XmlType(propOrder={"name","type","items"})
public class Category {
    
    public enum CategoryType {
        NORMAL,
        NUMERICAL, //mathematical computation possible
        TIME, //time handling (force 24 hrs representation)
        TEXT_NUMERICAL,   //requires interpretation of text into value e.g. days/months/ranking
        TEXT_NUMERICAL_WITH_WRAPAROUND
    }
    
//    @XmlElement
    private StringProperty nameProperty = new SimpleStringProperty();
    

    private  LogicProblem parent;
    
    private CategoryType categoryType = CategoryType.NORMAL;
    @XmlElement
    private List<Item> items = new ArrayList<Item>();
    
    //necessary for unmarshalling
    public Category(){
        
    }
    
    public Category(String myTitle,List<Item> myItems) {
        nameProperty.set(myTitle);
        items = myItems;
    }
    
    public void setParent(LogicProblem arg_parent){
        this.parent = arg_parent;
    }
    
    @XmlTransient
    public LogicProblem getParent() {
        return parent;
    }
    
    public void setType(CategoryType type_arg) {
        categoryType = type_arg;
    }
    public CategoryType getType(){
        return categoryType;
    }

    
    
    
    public void setName(String myText){
        nameProperty.set(myText);
    }
    
    @XmlElement
    public String getName(){
        return nameProperty.getValue();
    }
    
    public StringProperty nameProperty(){
        return nameProperty;
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
