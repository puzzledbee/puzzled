/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.HashSet;
import java.util.logging.Logger;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
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
    private DoubleProperty centerX = new SimpleDoubleProperty();
    private DoubleProperty centerY = new SimpleDoubleProperty();

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
    
    public DoubleProperty centerXProperty(){
        return this.centerX;
    }
    
    public DoubleProperty centerYProperty(){
        return this.centerY;
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
    
    public void drawPredecessors(StackPane mainStack){
        System.out.println("about to draw many special lines pointing to: "+ getCenterPosition().getX()
                +", "+ getCenterPosition().getY());
                
        AnchorPane dependencyPane = (AnchorPane) mainStack.getChildren().get(1);
        
        dependencyPane.getChildren().clear();
        
        for (Dependable predecessor : predecessors) {
            System.out.println("\tpredecessor "+predecessor.getCenterPosition().getX()
                +", "+ predecessor.getCenterPosition().getY());
            
            Line depLine = new Line(getCenterPosition().getX(),getCenterPosition().getY(),predecessor.getCenterPosition().getX(),predecessor.getCenterPosition().getY());
            depLine.getStyleClass().add("x");
            depLine.setMouseTransparent(true);
            dependencyPane.getChildren().add(depLine);

        }
    }
    
    @Override
    public Point2D getCenterPosition(){
        return new Point2D(this.centerX.get(),this.centerY.get());
        
    }
}
