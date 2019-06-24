/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.HashSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
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

public class Clue extends Dependable {
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
    
    
    @XmlElement
    public String getText(){
        return clueTextProperty.getValue();
    }
    
    public void setText(String arg_text) {
        clueTextProperty.set(arg_text);
    }
    
    public void setType(ClueType arg_type) {
        clueType = arg_type;
    }
    
    //used for ClueNumber computation, alternatively, calls to this method
    //could be replaced by calls to getClueNumberProperty().get()
    public ClueNumber getClueNumber() {
        return clueNumberProperty.get();
    }
    
    //necessary for the TableView celldata column assignment
    public ObjectProperty<ClueNumber> getClueNumberProperty() {
        return clueNumberProperty;
    }
    
    //necessary for the TableView
    public StringProperty getClueTextProperty() {
        return clueTextProperty;
    }
    
    
    //
//    public String getClueNumberAsString() {
//        return (this.getClueNumber() == null)?"":this.getClueNumber().getStringProperty().get();
//    }
    
    
    @XmlElement
    public ClueType getType(){
        return clueType;
    }
    
    @XmlTransient
    public Point2D getCenterPosition(){
        return null;
    }
}
