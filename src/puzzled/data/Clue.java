/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.HashSet;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Point2D;

/**
 *
 * @author Fred
 */
public class Clue implements Dependable {
    private HashSet<Dependable> successors = new HashSet<Dependable>();
    private HashSet<Dependable> predecessors = new HashSet<Dependable>();
    
    
    public enum ClueType {
        NORMAL,
        SPECIAL //needs re-assessment, see notebook for SpecialClue
    }
    

    private SimpleStringProperty clueText = new SimpleStringProperty();
    private ClueType clueType = ClueType.NORMAL;
    
    //necessary for iterator?
    public Clue(){
        
    }
    
    public void addSuccessor(Dependable successor) {
        successors.add(successor);
    }
    
    public HashSet<Dependable> getSuccessors(){
        return successors;
    }
    
    public Clue(String clueText_arg) {
        clueText.set(clueText_arg);
    }
    
    public String getText(){
        return clueText.getValue();
    }
    
    
    
    public Point2D getCenterPosition(){
        return null;
    }
}
