/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.util.Pair;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.javatuples.Triplet;
import puzzled.processor.Parser;

/**
 *
 * @author phiv
 */

@XmlRootElement
@XmlType(propOrder={"title","source","notes","text","scale","categories","clues"})
//something is amiss
public class LogicProblem {

//    @XmlElement //why are we not saving the title in the XML again?
    private StringProperty titleProperty = new SimpleStringProperty();
    
    private StringProperty problemTextProperty = new SimpleStringProperty();
    private String problemSource;
    
    @XmlElement
    private String notes;
    
//    @XmlElement
//    private int numCategories; //is this necessary or can it not be recovered, what is the point???
//    @XmlElement
//    private int numItems; //is this necessary or can it not be recovered, what is the point???
    @XmlElement
    private List<Category> categories;

    @XmlElement
    private NumberedClueList numberedClueList = new NumberedClueList();
    //private List<Clue> clueList = new ArrayList<Clue>();
    private ObservableList<Pair<ClueNumber, Clue>> clues = FXCollections.observableList(numberedClueList);;
    
    private HashMap<ItemPair,Relationship> relationshipTable;
    
    //visual scale
    private DoubleProperty scaleProperty = new SimpleDoubleProperty(1);
    
    private BooleanProperty dirtyLogicProperty = new SimpleBooleanProperty(false);
    private BooleanProperty dirtyFileProperty = new SimpleBooleanProperty(false);
    
    
    
    //necessary for unmarshalling
    public LogicProblem(){
    }
    
    
    
    
    public LogicProblem(String ... problemInfo){
        System.out.println("constructor invoked");
        titleProperty.set(problemInfo[0].trim()); //trim needed for loading from .lps
        if (problemInfo.length>1) this.problemSource = problemInfo[1].trim();
        if (problemInfo.length>2) this.notes = problemInfo[2].trim();
//        numCategories = category_number;
//        numItems = item_number;
        categories = new ArrayList<Category>();
        
        this.dirtyLogicProperty.addListener( (e, oldvalue, newvalue) -> System.out.println("change triggered in LogicProblem"));
        //categories.add("Age");
        //categories.add("test");
    }
    
    public BooleanProperty dirtyLogicProperty(){
        return this.dirtyLogicProperty;
    }
    
    @XmlTransient
    public boolean isLogicDirty(){
        return this.dirtyLogicProperty.getValue();

    }
    public void setLogicDirty(boolean dirtyness){
        System.out.println("logic problem logic set to dirty "+dirtyness);
        this.dirtyLogicProperty.set(dirtyness);
    }
    
     public BooleanProperty dirtyFileProperty(){
        return this.dirtyFileProperty;
    }
    
    public void setFileDirty(boolean dirtyness) {
        System.out.println("\n\nlogic problem file set to dirty "+dirtyness);
        this.dirtyFileProperty.set(dirtyness);
    }
     
    @XmlTransient
    public boolean isFileDirty(){
        return this.dirtyFileProperty.getValue();
    }
    
    public DoubleProperty scaleProperty() {
        return this.scaleProperty;
    }

    @XmlElement
    public String getTitle() {
        return titleProperty.getValue();
    }
    
    public StringProperty titleProperty() {
        return titleProperty;
    }
   
    public StringProperty problemTextProperty() {
        return problemTextProperty;
    }

    public void setTitle(String newTitle) {
        titleProperty.set(newTitle);
    }
    
    public NumberedClueList getNumberedClueList(){
        return numberedClueList;
    }
    
    @XmlTransient
    public HashMap<ItemPair,Relationship> getRelationshipTable(){
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
    
    @XmlElement //unnecessary
    public String getText() {
        return problemTextProperty.get();
    }
    
    public void generateRelationships(){
        
        System.out.println("generating relationshipTable");
        relationshipTable = new HashMap<ItemPair,Relationship>();
        
        ItemPair pair;
        
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

                            pair = new ItemPair(item1,item2);
                            
//                            System.out.println("joining "+pair.first().getName()+" <-> "+ pair.last().getName());
                            if (!relationshipTable.containsKey(pair)){
                                relationshipTable.put(pair,new Relationship(this,pair));
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

    public void setText(String text) {
        this.problemTextProperty.set(text);
    }
  
    //public FilteredList<Pair<ClueNumber, Clue>> getFilteredClues() {
    //    return new FilteredList<>(clues, row -> row.getValue().getType() != Clue.ClueType.CONSTRAINT);
    //}
    
    public String getNotes() {
        return notes;
    }
     
    public void clearInvestigate() {
        System.out.println("clearing previous styles");
        for (ItemPair key : this.relationshipTable.keySet()) this.relationshipTable.get(key).investigateProperty().set(false);
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
    
    public HashSet<TreeSet<Category>> getCategoryPairs() {
        HashSet<TreeSet<Category>> categoryPairs = new HashSet();
        for (Category catA : categories) {
            for (Category catB : categories) {
                if (catA != catB) {
                    CategoryPair categoryPair = new CategoryPair(catA,catB);
                    categoryPairs.add(categoryPair);
                }
            }
        }
        return categoryPairs;
    }
    
    public void addCategory(Category newCategory) {
        categories.add(newCategory);
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
