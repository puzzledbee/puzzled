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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author phiv
 */

@XmlRootElement
@XmlType(propOrder={"title","source", "problemText", "notes", "scale",
    "categoriesList", "numberedClueList", "relationshipTable"})
//something is amiss
public class LogicProblem {
    //data members members
    private StringProperty titleProperty = new SimpleStringProperty();
    private StringProperty problemTextProperty = new SimpleStringProperty();
    private String problemSource;
    private String notes; //notes related to the problem
    private List<Category> categoriesList;
    
    private NumberedClueList numberedClueList = new NumberedClueList(); //extends ArrayList
    //this is in the PuzzledController now, passed to the clueTabController
    //private ObservableList<Clue> clues = FXCollections.observableList(numberedClueList);
    
    private List<Constraint> constraintList = new ArrayList<Constraint>();
    private HashMap<ItemPair,Relationship> relationshipTable;
        
    // utility members
    //visual scale
    private DoubleProperty scaleProperty = new SimpleDoubleProperty(1);
    private BooleanProperty dirtyLogicProperty = new SimpleBooleanProperty(false);
    private BooleanProperty dirtyFileProperty = new SimpleBooleanProperty(false);
    
    //necessary for unmarshalling
    public LogicProblem(){
    }
    
    
    public LogicProblem(String ... problemInfo){
        System.out.println("constructor invoked");
        this.titleProperty.set(problemInfo[0].trim()); //trim needed for loading from .lps
        if (problemInfo.length>1) this.problemSource = problemInfo[1].trim();
        if (problemInfo.length>2) this.notes = problemInfo[2].trim();
//        numCategories = category_number;
//        numItems = item_number;
        categoriesList = new ArrayList<Category>();
        
        this.dirtyLogicProperty.addListener( (e, oldvalue, newvalue) -> System.out.println("change triggered in LogicProblem"));
        //categories.add("Age");
        //categories.add("test");
    }

    
    @XmlElement //unnecessary?
    public void setScale(double newScale){
        this.scaleProperty.set(newScale);
    }
    public double getScale(){
        return this.scaleProperty.get();
    }
    public DoubleProperty scaleProperty() {
        return this.scaleProperty;
    }
    

    @XmlElement
    public void setTitle(String newTitle) {
        this.titleProperty.set(newTitle);
    }
    public String getTitle() {
        return this.titleProperty.getValue();
    }
    public StringProperty titleProperty() {
        return this.titleProperty;
    }
   
    
    @XmlElement
    public void setProblemText(String text) {
        this.problemTextProperty.set(text);
    }
    public String getProblemText() {
        return this.problemTextProperty.get();
    }
    public StringProperty problemTextProperty() {
        return this.problemTextProperty;
    }

    @XmlElement
    public NumberedClueList getNumberedClueList(){
        return this.numberedClueList;
    }
    
    @XmlElement
    public HashMap<ItemPair,Relationship> getRelationshipTable(){
        return this.relationshipTable;
    }
    
    @XmlElement
    public void setSource(String arg_source) {
        this.problemSource =arg_source;
    }
    public String getSource() {
        return this.problemSource;
    }
    
    @XmlElement
    public void setNotes(String arg_notes) {
        this.notes = arg_notes;
    }
    public String getNotes() {
        return this.notes;
    }
    
    @XmlElement
    public List<Category> getCategoriesList() {
        return this.categoriesList;
    }

    public void initializeRelationshipTable(){
        System.out.println("initializing relationshipTable");
        relationshipTable = new HashMap<ItemPair,Relationship>();
        
        ItemPair pair;
        
        //fix parent references
        for (Category cat : getCategoriesList()){
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
        for (Category cat1 : getCategoriesList()) {
            for (Category cat2 : getCategoriesList()){
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

    
  
    //public FilteredList<Pair<ClueNumber, Clue>> getFilteredClues() {
    //    return new FilteredList<>(clues, row -> row.getValue().getType() != Clue.ClueType.CONSTRAINT);
    //}
    

    
    public void clearInvestigate() {
        System.out.println("clearing previous styles");
        for (ItemPair key : this.relationshipTable.keySet()) this.relationshipTable.get(key).explainProperty().set(false);
    }
    //somehow does not get registered as an XmlElement
    public int getNumItems(){
        return this.categoriesList.get(0).getNumItems();
    }

    //somehow does not get registered as an XmlElement
    public int getNumCategories(){
        return this.categoriesList.size();
    }
    

    
    public HashSet<TreeSet<Category>> getCategoryPairs() {
        HashSet<TreeSet<Category>> categoryPairs = new HashSet();
        for (Category catA : categoriesList) {
            for (Category catB : categoriesList) {
                if (catA != catB) {
                    CategoryPair categoryPair = new CategoryPair(catA,catB);
                    categoryPairs.add(categoryPair);
                }
            }
        }
        return categoryPairs;
    }
    
    public void addCategory(Category newCategory) {
        categoriesList.add(newCategory);
    }

    @Override
    public String toString(){
        String output = new String();
        output += "Title:" + this.getTitle()+"\n";
        output += "Scale:" + this.getScale()+"\n";
        output += "numItems:" + this.getNumItems()+"\n";
        output += "numItems:" + this.getNumCategories()+"\n";
        
        for(Category quark: categoriesList){
             output += "category: " + quark + "\n";
             for (Item item: quark.getItems()){
                 output += "\titem: " + item.getName() + "\n";
             }
         }
        return output;
    }
    
    public List<Constraint> getConstraintList() {
        return constraintList;
    }
    
    public void addConstraint(Constraint constraint) {
        //String clueString = new String(pair.first().getName() 
        //        + ((relationship.getValue()==Relationship.ValueType.VALUE_YES)?" is ":" is not ") 
        //        + pair.last().getName());
        
//        System.out.println("constraint added\n"+ constraint);
        constraintList.add(constraint);
        //return constraint;
    }
    
    @XmlTransient
    public void setDirtyLogic(boolean dirtyness){
        System.out.println("logic problem logic set to dirty "+dirtyness);
        this.dirtyLogicProperty.set(dirtyness);
    }
    public boolean isLogicDirty(){
        return this.dirtyLogicProperty.getValue();
    }
    public BooleanProperty dirtyLogicProperty(){
        return this.dirtyLogicProperty;
    }
    
    @XmlTransient
    public void setDirtyFile(boolean dirtyness) {
        System.out.println("\n\nlogic problem file set to dirty "+dirtyness);
        this.dirtyFileProperty.set(dirtyness);
    }
    public boolean isFileDirty(){
        return this.dirtyFileProperty.getValue();
    }
    public BooleanProperty dirtyFileProperty(){
        return this.dirtyFileProperty;
    }
}
