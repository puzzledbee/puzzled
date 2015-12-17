/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Fred
 */

@XmlRootElement
@XmlType(propOrder={"title","problemSource","notes","numItems","numCategories","scale","categories","clues"})

public class LogicProblem {

//    @XmlElement
    private StringProperty titleProperty = new SimpleStringProperty();
    @XmlElement
    private String problemSource;
    @XmlElement
    private String notes;
    
    @XmlElement
    private int numCategories; //is this necessary or can it not be recovered, what is the point???
    @XmlElement
    private int numItems; //is this necessary or can it not be recovered, what is the point???
    @XmlElement
    private List<Category> categories;
    
    private DoubleProperty scaleProperty = new SimpleDoubleProperty(1);
    
    @XmlElement
    private List<Clue> clues;
    
    //necessary for unmarshalling
    public LogicProblem(){
    }
    
    public LogicProblem(String title, int category_number, int item_number){
        System.out.println("constructor invoked");
        titleProperty.set(title);
        numCategories = category_number;
        numItems = item_number;
        categories = new ArrayList<Category>(numCategories);
        
        //categories.add("Age");
        //categories.add("test");
    }
    
    public LogicProblem(int category_number, int item_number){
        this("",category_number,item_number);
    }

    @XmlElement
    public String getTitle() {
        return titleProperty.getValue();
    }

    public void setTitle(String newTitle) {
        titleProperty.set(newTitle);
    }
    
    /**
     *
     * @return
     */

    public String getSource() {
        return problemSource;
    }
    
    public String getNotes() {
        return notes;
    }

    public int getNumItems(){
        return numItems;
    }
    
    public int getNumCategories(){
        return numCategories;
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
    
    public DoubleProperty getScaleProperty(){
        return scaleProperty;
    }
    
    public void setScale(double newScale){
        scaleProperty.set(newScale);
    }
    
    @XmlElement
    public double getScale(){
        return scaleProperty.get();
    }
        
    @Override
    public String toString(){
        String output = new String();
        output += "Title:" + this.getTitle()+"\n";
        output += "Scale:" + this.getScale()+"\n";
        output += "numItems:" + this.getNumItems()+"\n";
        output += "numItems:" + this.getNumCategories()+"\n";
        
        for(Category quark: categories){
             output += "category: " + quark + "\n";
             for (Item item: quark.getItems()){
                 output += "\titem: " + item.getText() + "\n";
             }
         }
        return output;
    }
}
