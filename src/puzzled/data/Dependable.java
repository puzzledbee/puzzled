/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.HashSet;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;

/**
 *
 * @author phiv
 */
public abstract class Dependable {
    private HashSet<Dependable> predecessors = new HashSet<Dependable>();
    private HashSet<Dependable> successors = new HashSet<Dependable>();
    private BooleanProperty explainProperty = new SimpleBooleanProperty(); 
    
    abstract public Point2D getCenterPosition();


    public HashSet<Dependable> getPredecessors() {
        return predecessors;
    }
    
    public HashSet<Dependable> getSuccessors() {
        return successors;
    }
    
    public void addPredecessor(Dependable predecessor){
        predecessors.add(predecessor);
    }
    
    public void addSuccessor(Dependable successor) {
        successors.add(successor);
    }
        
    public BooleanProperty getExplainProperty() {
        return explainProperty;
    }
    
    
    
}
