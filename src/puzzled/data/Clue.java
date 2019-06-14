/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.HashSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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

public class Clue implements Dependable {
    private HashSet<Dependable> successors = new HashSet<Dependable>();
    private HashSet<Dependable> predecessors = new HashSet<Dependable>();
    private BooleanProperty investigateProperty = new SimpleBooleanProperty(false);

    public enum ClueType {
        NORMAL,
        SPECIAL, //needs re-assessment, see notebook for SpecialClue    
        CONSTRAINT
    }

    private SimpleStringProperty clueText = new SimpleStringProperty();
    private ClueType clueType = ClueType.NORMAL;
     //whether the clue is enabled or disabled
    private BooleanProperty activeProperty = new SimpleBooleanProperty(true); 
    
    
    //necessary for iterator?
    public Clue(){
        
    }
    
    public void addSuccessor(Dependable successor) {
        successors.add(successor);
    }
    
    public HashSet<Dependable> getSuccessors(){
        return successors;
    }
    
    public Clue(String arg_clueText, ClueType arg_clueType) {
        clueText.set(arg_clueText);
        clueType = arg_clueType;
    }
    
    public Clue(String ... clueInfo) { //clue text and clue type
        clueText.set(clueInfo[0].trim());
        if (clueInfo.length>1) clueType = ClueType.valueOf(clueInfo[1].trim());
    }
    
    @XmlElement
    public String getText(){
        return clueText.getValue();
    }
    
    public void setText(String arg_text) {
        clueText.set(arg_text);
    }
    
    public void setType(ClueType arg_type) {
        clueType = arg_type;
    }
    
    
    public BooleanProperty investigateProperty() {
        return this.investigateProperty;
    }
    
    @XmlElement
    public ClueType getType(){
        return clueType;
    }
    
    @XmlTransient
    public Point2D getCenterPosition(){
        return null;
    }
}
