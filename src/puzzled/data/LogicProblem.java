/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Fred
 */

@XmlRootElement
@XmlType(propOrder={"title","source","notes","scale","categories","clues"})
//something is amiss
public class LogicProblem {

//    @XmlElement
    private StringProperty titleProperty = new SimpleStringProperty();
    
    private String problemSource;
    @XmlElement
    private String notes;
    
//    @XmlElement
//    private int numCategories; //is this necessary or can it not be recovered, what is the point???
//    @XmlElement
//    private int numItems; //is this necessary or can it not be recovered, what is the point???
    @XmlElement
    private List<Category> categories;
    
    private DoubleProperty scaleProperty = new SimpleDoubleProperty(1);
    
    
    private List<Clue> clueList = new ArrayList<Clue>();
    @XmlElement
    private ObservableList<Clue> clues = FXCollections.observableList(clueList);
    
    //necessary for unmarshalling
    public LogicProblem(){
    }
    
    private HashMap<TreeSet<Item>,Relationship> relationshipTable;
    
    public LogicProblem(String title){
        System.out.println("constructor invoked");
        titleProperty.set(title);
//        numCategories = category_number;
//        numItems = item_number;
        categories = new ArrayList<Category>();
        
        //categories.add("Age");
        //categories.add("test");
    }
    


    @XmlElement
    public String getTitle() {
        return titleProperty.getValue();
    }

    public void setTitle(String newTitle) {
        titleProperty.set(newTitle);
    }
    
    @XmlTransient
    public HashMap<TreeSet<Item>,Relationship> getRelationshipTable(){
        return relationshipTable;
    }
    
    /**
     *
     * @return
     */
    @XmlElement //unnecessary
    public String getSource() {
        return problemSource;
    }
    
    public void generateRelationships(){
        
        System.out.println("generating relationshipTable");
        relationshipTable = new HashMap<TreeSet<Item>,Relationship>();
        
        TreeSet<Item> pair = new TreeSet<Item>(Comparator.comparing(p1 -> p1.getCatIndex()));
        
        //fix parent references
        for (Category cat : getCategories()){
            cat.setParent(this);
            for (Item item : cat.getItems()) {
                item.setParent(cat);
            }
        }
        
//        for (Category cat : getCategories()){
//             for (Item item : cat.getItems()) {
//                System.out.println("item:"+ item.getName()+" catIndex:"+item.getCatIndex()+"  itemIndex:"+item.getItemIndex());
//            }
//        }
        
//        int i=1;
        for (Category cat1 : getCategories()) {
            for (Category cat2 : getCategories()){
                if (cat1 != cat2) {
                    for (Item item1 : cat1.getItems()) {
                        for (Item item2 : cat2.getItems()){
                            
                            
                            pair = new TreeSet<Item>(Comparator.comparing(p1 -> p1.getCatIndex()));
                            pair.add(item1);
                            pair.add(item2);
//                            System.out.println("joining "+pair.first().getName()+" <-> "+ pair.last().getName());
                            if (!relationshipTable.containsKey(pair)){
                                relationshipTable.put(pair,new Relationship());
                            }
                        }
                    }
                }
            }
            
        }
        
        
//        testSet.add(this.getCategories().get(4).getItems().get(1));
//        testSet.add(this.getCategories().get(3).getItems().get(2));
//        positionGridCell(testSet);
    }

    
    public void setSource(String arg_source) {
        this.problemSource =arg_source;
    }

    public ObservableList<Clue> getClues() {
        return clues;
    }
    
    public String getNotes() {
        return notes;
    }
    
    //somehow does not get registered as an XmlElement
    public int getNumItems(){
        return categories.get(0).getNumItems();
    }
    
    //somehow does not get registered as an XmlElement
    public int getNumCategories(){
        return categories.size();
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

    @XmlElement //unnecessary
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
                 output += "\titem: " + item.getName() + "\n";
             }
         }
        return output;
    }
}
