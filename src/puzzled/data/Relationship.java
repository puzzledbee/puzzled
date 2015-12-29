/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.HashSet;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import puzzled.Puzzled;

/**
 *
 * @author Fred
 */
public class Relationship implements Dependable {
    
    
    public static enum ValueType {
        VALUE_YES, VALUE_NO, VALUE_UNKNOWN 
    }
    
    private ObjectProperty<ValueType> valueProperty = new SimpleObjectProperty<>(this, "value" , ValueType.VALUE_UNKNOWN);
   
    private ObjectProperty<Bounds> boundProperty = new SimpleObjectProperty<Bounds>();

    private HashSet<Dependable> predecessors = new HashSet<Dependable>();
    private HashSet<Dependable> successors = new HashSet<Dependable>();
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private LogicProblem logicProblem;

    public Relationship(LogicProblem arg_parent) {
//        System.out.println("creating relationship #" + index);
        logicProblem = arg_parent;
        valueProperty.addListener( (e,oldValue,newValue) -> {
//            fLogger.info("Relationship valueProperty chansged to: " + newValue);
            logicProblem.setLogicDirty(true);
                });
    }
    public Relationship(ValueType myType) {
        valueProperty.set(myType);
        valueProperty.addListener( (e,oldValue,newValue) -> fLogger.info("Relationship valueProperty changed to: " + newValue));
    }
    
    public ObjectProperty<ValueType> valueProperty(){
        return valueProperty;
    }
    
    public ObjectProperty<Bounds> boundProperty(){
        return this.boundProperty;
    }
    
    public ValueType getValue(){
        return valueProperty.get();
    }
    
    public void setValue(ValueType value){
        valueProperty.set(value);
    }
    
    public void setValue(ValueType value, Dependable ... arg_predecessors){
        valueProperty.set(value);
        for (Dependable predecessor : arg_predecessors) {
            predecessors.add(predecessor);
            predecessor.addSuccessor(this);
        }
    }
    
    public void addSuccessor(Dependable successor) {
        successors.add(successor);
    }
    public HashSet<Dependable> getSuccessors(){
        return successors;
    }
    
    @Override
    public Point2D getCenterPosition(){
        return null;
    }
}
