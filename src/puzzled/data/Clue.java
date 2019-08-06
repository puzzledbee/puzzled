/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author phiv
 */

@XmlRootElement
@XmlType(propOrder={"text","type"})

public class Clue extends Dependable implements Comparable<Clue> {
    //private HashSet<Dependable> successors = new HashSet<Dependable>();
    //private HashSet<Dependable> predecessors = new HashSet<Dependable>();
    //private BooleanProperty investigateProperty = new SimpleBooleanProperty(false);

    public enum ClueType {
        NORMAL,
        SPECIAL //needs re-assessment, see notebook for SpecialClue    
        //CONSTRAINT
    }
    
    //private ClueNumber clueNumber;
    //properties are necessary for the TableView
    private ObjectProperty<ClueNumber> clueNumberProperty = new SimpleObjectProperty<>();
    //properties are necessary for the TableView
    private StringProperty clueTextProperty = new SimpleStringProperty();
    
    private ClueType clueType = ClueType.NORMAL;
     //whether the clue is enabled or disabled
    private BooleanProperty activeProperty = new SimpleBooleanProperty(true); 
    
    
    //necessary for iterator?
    public Clue(){
    }
       
    public Clue(ClueNumber clueNumber, String clueText, ClueType arg_clueType) {
        clueTextProperty.set(clueText);
        clueNumberProperty.set(clueNumber);
        clueType = arg_clueType;
    }
    
    public Clue(ClueNumber clueNumber, String ... clueInfo) { //clue text and clue type
        clueTextProperty.set(clueInfo[0].trim());
        if (clueInfo.length>1) clueType = ClueType.valueOf(clueInfo[1].trim());
        clueNumberProperty.set(clueNumber);
    }
    
    
    @XmlElement //?
    public String getClueText(){
        return this.clueTextProperty.get();
    }

    public String displayClue(){
        return this.getClueNumber().toString() + " -> " + this.clueTextProperty.get();
    }
    
    public void setClueText(String arg_text) {
        clueTextProperty.set(arg_text);
    }
    
    public void setClueType(ClueType arg_type) {
        clueType = arg_type;
    }
    
    //used for ClueNumber computation, alternatively, calls to this method
    //could be replaced by calls to getClueNumberProperty().get()
    public ClueNumber getClueNumber() {
        return clueNumberProperty.get();
    }
    
    //necessary for the TableView celldata column assignment
    public ObjectProperty<ClueNumber> clueNumberProperty() {
        return clueNumberProperty;
    }
    
   
    //necessary for the TableView
    public StringProperty clueTextProperty() {
        return clueTextProperty;
    }
    
    
    //
//    public String getClueNumberAsString() {
//        return (this.getClueNumber() == null)?"":this.getClueNumber().getStringProperty().get();
//    }
    
    
    @XmlElement
    public ClueType getClueType(){
        return clueType;
    }
    
    @XmlTransient
    public Point2D getCenterPosition(){
        return null;
    }
    
    @Override
    public int compareTo(Clue o) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this.getClueNumber().compareTo(o.getClueNumber());
    }
    
    
    public static Label labelGenerator(ObservableList<Clue> clues, Integer element) {
        Label label = new Label(element.toString());
        label.setTooltip(new Tooltip()); //else we get a NPE
        label.getTooltip().textProperty().bind(clueTooltip(clues,element));
        return label;
    }
    
    public static StringBinding clueTooltip(ObservableList<Clue> clues, Integer clueMajor) {
        Predicate<Clue> firstMajor = i -> (i.getClueNumber().getMajor() == clueMajor);
        FilteredList<Clue> filteredlist = new FilteredList(clues, firstMajor);

        List<StringProperty> dependencies = new ArrayList<>();
        filteredlist.forEach(clue -> dependencies.add(clue.clueTextProperty()));
        
        return Bindings.createStringBinding( () ->  filteredlist.stream().map(e -> e.displayClue()).collect(Collectors.joining("\n"))
    , filteredlist,FXCollections.observableArrayList(dependencies));
    }
    
}
