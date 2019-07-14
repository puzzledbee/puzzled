/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.HashSet;
import java.util.Optional;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Point2D;

/**
 *
 * @author phiv
 */
public abstract class Dependable {
   
    //hashsets are used to collapse duplicates; would that ever happen?
    private HashSet<Dependable> predecessors = new HashSet<Dependable>();
    private HashSet<Dependable> successors = new HashSet<Dependable>();
    private BooleanProperty explainProperty = new SimpleBooleanProperty(); 
    
    private ObservableSet<Dependable> observablePredecessors; 
    private ObservableSet<Dependable> observableSuccessors; 
    
    abstract public Point2D getCenterPosition();

    
    public Dependable(){
//        System.out.println("Dependable constructor called;");
        this.observablePredecessors = FXCollections.observableSet(predecessors);
        this.observableSuccessors = FXCollections.observableSet(successors);
    }

    
    public HashSet<Dependable> getPredecessors() {
        return predecessors;
    }
    
    public ObservableSet observablePredecessors(){
        return this.observablePredecessors;
    }
    
    //used in cases where a Constraint is the sole Relationship predecessor
    //in order to retreive the annotation
    public Optional<Constraint> getPredecessorConstraint() {
        if (predecessors.size()==1) {
            Dependable singleElement = predecessors.iterator().next();
            if (singleElement instanceof Constraint) {
                return Optional.of((Constraint) singleElement);
            }  else return Optional.empty();
        } else return Optional.empty();
    }
    
    public HashSet<Dependable> getSuccessors() {
        return this.successors;
    }
    
    public ObservableSet observableSuccessors(){
        return this.observableSuccessors;
    }
    
    public void addPredecessor(Dependable predecessor){
        this.predecessors.add(predecessor);
        System.out.println(">>>>>>predecessor " + predecessor.toString() + " added...");
    }
    
    public void addSuccessor(Dependable successor) {
        this.successors.add(successor);
        System.out.println(">>>>>>successor " + successor.toString() + " added...");
    }
        
    public BooleanProperty explainProperty() {
        return this.explainProperty;
    }
    
    
}
