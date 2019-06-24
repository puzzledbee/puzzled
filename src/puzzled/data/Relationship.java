/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import puzzled.Puzzled;
import puzzled.exceptions.RelationshipConflictException;
import puzzled.exceptions.SuperfluousRelationshipException;

/**
 *
 * @author phiv
 */
public class Relationship extends Dependable {
    
    
    public static enum ValueType {
        VALUE_YES, VALUE_NO, VALUE_UNKNOWN 
    }
    
    public static enum LogicType {
        CONSTRAINT, CROSS, TRANSPOSE, UNIQUE, COMMON
    }
    
    private ObjectProperty<ValueType> valueProperty = new SimpleObjectProperty<ValueType>(this, "value" , ValueType.VALUE_UNKNOWN);
    private ObjectProperty<LogicType> logicTypeProperty = new SimpleObjectProperty<LogicType>(this, "value" , LogicType.CONSTRAINT);
    
    private DoubleProperty centerX = new SimpleDoubleProperty();
    private DoubleProperty centerY = new SimpleDoubleProperty();
    
//    private StringProperty highlight = new SimpleStringProperty();

    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private LogicProblem logicProblem;
    private ItemPair pair;

//    private LogicType logicType = LogicType.CONSTRAINT;
    
    private StringProperty relationshipTextProperty = new SimpleStringProperty();
    
    public Relationship(LogicProblem arg_parent, ItemPair arg_pair) {
//        System.out.println("creating relationship: " + arg_pair);
        logicProblem = arg_parent;
        pair = arg_pair;
        
        valueProperty.addListener( (e,oldValue,newValue) -> {
//            fLogger.info("Relationship valueProperty chansged to: " + newValue);
            logicProblem.setDirtyLogic(true);
                });
        
        getExplainProperty().addListener((e,oldValue,newValue) -> {
            if (newValue == true) {
                System.out.println("explain property becoming true for "+this.toString());
                for (Dependable predecessor : this.getPredecessors()) {
                    System.out.println("setting predecessor "+predecessor.toString());
                    predecessor.getExplainProperty().set(true);
                }
            }
        });
        
        this.relationshipTextProperty.bind(Bindings.createStringBinding(
                () -> { 
                    StringBuilder text = new StringBuilder();
                    text.append("The relationship between " + this.pair.toString() +
                        " is " + (this.valueProperty.getValue()==ValueType.VALUE_UNKNOWN?"not defined":this.valueProperty.getValue()+" ("+this.logicTypeProperty.getValue()+")"));
                    text.append("\n");
                    this.getPredecessors().forEach(predecessor -> text.append(predecessor.toString()));
                    
                    return text.toString();
                        },
                this.valueProperty,
                this.logicTypeProperty));
        //this.relationshipTextProperty.bind(Bindings.createStringBinding(() -> "This is a relationship of type " + logicTypeProperty.toString() + " between " + pair.toString(), logicTypeProperty));
        
    }

    
    //public Relationship(ValueType myType) {
    //    valueProperty.set(myType);
    //    valueProperty.addListener( (e,oldValue,newValue) -> fLogger.info("Relationship valueProperty changed to: " + newValue));
    //}
    
    public ObjectProperty<ValueType> valueProperty(){
        return valueProperty;
    }
    
    
    public void clearInvestigate() {
        logicProblem.clearInvestigate();
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
    
    public LogicType getLogic() {
        return logicTypeProperty.get();
    }
    
    public ObjectProperty<LogicType> logicTypeProperty() {
        return this.logicTypeProperty;
    }
    
    public void setValue(ValueType value){
        valueProperty.set(value);
    }
    
    public void setValue(ValueType value, LogicType arg_logicType, Dependable ... arg_predecessors) throws SuperfluousRelationshipException, RelationshipConflictException {
        //is this relationship previously unassigned? then there are no issues, it can just take the new value
        if (valueProperty.getValue()==ValueType.VALUE_UNKNOWN) {
            valueProperty.set(value);
            logicTypeProperty.set(arg_logicType);
            for (Dependable predecessor : arg_predecessors) {
                System.out.print("there is a predecessor added to " + this.toString() + " : " + predecessor.toString());
                
                this.addPredecessor(predecessor);
                System.out.println("total number of predecessors: " + this.getPredecessors().size());
                predecessor.addSuccessor(this);
            }
        } else if (valueProperty.getValue()==value) {
            //already set by different logic type
           // if (logicTypeProperty.get() != arg_logicType) throw new SuperfluousRelationshipException(pair,logicTypeProperty.get(),arg_logicType); //investigate up tree
        } else {
            throw new RelationshipConflictException(this.pair,valueProperty.getValue(),value);
        }
    }
    
    public void drawPredecessors(StackPane mainStack){
        System.out.println("about to draw many special lines pointing to: "+ getCenterPosition().getX()
                +", "+ getCenterPosition().getY());
                
        AnchorPane dependencyPane = (AnchorPane) mainStack.getChildren().get(1);
        
        dependencyPane.getChildren().clear();
        
        for (Dependable predecessor : this.getPredecessors()) {
            System.out.println("\tpredecessor "+predecessor.getCenterPosition().getX()
                +", "+ predecessor.getCenterPosition().getY());
            
            Line depLine = new Line(getCenterPosition().getX(),getCenterPosition().getY(),predecessor.getCenterPosition().getX(),predecessor.getCenterPosition().getY());
            depLine.getStyleClass().add("x");
            depLine.setMouseTransparent(true);
            dependencyPane.getChildren().add(depLine);

        }
    }
    
    public LogicProblem getParent() {
        return logicProblem;
    }
    

    @Override
    public String toString(){
       return relationshipTextProperty.get(); 
    }
    
    @Override
    public Point2D getCenterPosition(){
        return new Point2D(this.centerX.get(),this.centerY.get());
    }
    
    public StringProperty relationshipTextProperty() {
        return this.relationshipTextProperty;
    }
}
