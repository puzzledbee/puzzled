/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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

/**
 *
 * @author phiv
 */
public class Relationship extends Dependable {
    
    public static enum ValueType {
        VALUE_YES, VALUE_NO, VALUE_UNKNOWN 
    }
    
    public static enum LogicType {
        CONSTRAINT, CROSS, TRANSPOSE, UNIQUE, COMMON, PSEUDOTRUE
    }
    
    private ObjectProperty<ValueType> valueProperty = new SimpleObjectProperty<ValueType>(this, "value" , ValueType.VALUE_UNKNOWN);
    private ObjectProperty<LogicType> logicTypeProperty = new SimpleObjectProperty<LogicType>(this, "value" , LogicType.CONSTRAINT);
    private BooleanProperty appliedProperty = new SimpleBooleanProperty(true);
    
    private DoubleProperty centerXProperty = new SimpleDoubleProperty(); //--> should we move this into GridCell or Dependable?
    private DoubleProperty centerYPropery = new SimpleDoubleProperty();
    
    private StringProperty annotationProperty = new SimpleStringProperty("");
//    private ObservableSet observablePredecessorSet;
    
//    private StringProperty highlight = new SimpleStringProperty();

    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private LogicProblem logicProblem;
    private ItemPair itemPair;

//    private LogicType logicType = LogicType.CONSTRAINT;
    
    private StringProperty relationshipTextProperty = new SimpleStringProperty();
    
    //constructor
    public Relationship(LogicProblem arg_parent, ItemPair arg_pair) {
//        System.out.println("creating relationship: " + arg_pair);
        logicProblem = arg_parent;
        itemPair = arg_pair;
//        observablePredecessorSet = FXCollections.observableSet(this.observablePredecessors());
        valueProperty.addListener( (e,oldValue,newValue) -> {
//            fLogger.info("Relationship valueProperty for "+arg_pair+" changed to: " + newValue);
            if (newValue != Relationship.ValueType.VALUE_UNKNOWN) logicProblem.setDirtyLogic(true);
                });
        
        explainProperty().addListener((e,oldValue,newValue) -> {
            if (newValue == true) {
                System.out.println("explain property becoming true for "+this.toString());
                for (Dependable predecessor : this.getPredecessors()) {
                    System.out.println("setting predecessor "+predecessor.toString());
                    predecessor.explainProperty().set(true);
                }
            }
        });
        setTooltipTextBinding();
    }
    
    public LogicProblem getParentLogicProblem() {
        return logicProblem;
    }
    
    
    public void clearInvestigate() {
        logicProblem.clearInvestigate();
    }
    
    
    public DoubleProperty centerXProperty(){
        return this.centerXProperty;
    }
    
    public DoubleProperty centerYProperty(){
        return this.centerYPropery;
    }
   
    
    
    public LogicType getLogicType() {
        return logicTypeProperty.get();
    }
    
    public void setLogicType(Relationship.LogicType logic) {
        this.logicTypeProperty.set(logic);
    }
    
    public ObjectProperty<LogicType> logicTypeProperty() {
        return this.logicTypeProperty;
    }
    
    public ItemPair getItemPair() {
        return this.itemPair;
    }
    
    public ValueType getValue(){
        return valueProperty.get();
    }
    
    public ObjectProperty<ValueType> valueProperty(){
        return valueProperty;
    }
    
    public void setValue(ValueType value, boolean apply){
        valueProperty.set(value);
        this.appliedProperty.set(apply);
    }
        
    public void setValue(ValueType value, LogicType arg_logicType, boolean apply, Dependable ... arg_predecessors) 
            throws RelationshipConflictException {
        //is this relationship previously unassigned? then there are no issues, it can just take the new value
        if (this.getValue()==ValueType.VALUE_UNKNOWN || (this.getValue()==value && !this.isApplied())) {
            this.setValue(value, apply);
            this.setLogicType(arg_logicType);
            
            //adjusting the upstream dependable objects (predecessors)
//            System.out.println("adding a total of  " + arg_predecessors.length + " predecessors to " + this.toString());
            for (Dependable predecessor : arg_predecessors) {

                this.addPredecessor(predecessor);
                predecessor.addSuccessor(this);
            }
            //changing this flag is the sole responsibility of this method
            //it signifies the Processor.process static method needs to run again
//            logicProblem.dirtyLogicProperty().set(true); //not required a ChangeListener is registered
        } else if (this.getValue()==value) {
            //already set by different logic type
           // if (logicTypeProperty.get() != arg_logicType) throw new SuperfluousRelationshipException(pair,logicTypeProperty.get(),arg_logicType); //investigate up tree
        } else {
            throw new RelationshipConflictException(this.itemPair,valueProperty.getValue(),value);
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
    
    private void setTooltipTextBinding(){
        
        this.relationshipTextProperty.bind(Bindings.createStringBinding(() -> { 
                StringBuilder text = new StringBuilder();
                text.append("The relationship between " + this.itemPair.toString() +
                    " is " + (this.getValue()==ValueType.VALUE_UNKNOWN || !this.isApplied()?"not defined":this.getValue()+" ("+this.getLogicType()+")"));

                if (this.getValue() != ValueType.VALUE_UNKNOWN && this.getLogicType() == LogicType.CONSTRAINT) {
                    text.append("\n\nThis relationship was set graphically by the user\n");
                }

                if (this.getValue() != ValueType.VALUE_UNKNOWN && this.getLogicType() != LogicType.CONSTRAINT && this.isApplied()) {
                    text.append("\n\nderived from:\n");
                    this.getPredecessors().forEach(dependable -> text.append(dependable.toString()+"\n"));
                }
                if (!annotationProperty.get().isBlank()) {
                    text.append("\n\n"+annotationProperty.get());
                }
            
                return text.toString();
                },
            this.observablePredecessors(),
            this.valueProperty,
            this.appliedProperty,
            this.logicTypeProperty, //otherwise the timing of the binding being refreshed
            this.annotationProperty));
    }
    
    @Override
    public String toString(){
        return ("The relationship between " + this.itemPair.toString() +
            " is " + (this.getValue()==ValueType.VALUE_UNKNOWN?"not defined":this.getValue()+" ("+this.getLogicType()+")"));
//       return relationshipTextProperty.get(); 
    }
    
    @Override
    public Point2D getCenterPosition(){
        return new Point2D(this.centerXProperty.get(),this.centerYPropery.get());
    }
    
    public StringProperty relationshipTextProperty() {
        return this.relationshipTextProperty;
    }

    public void setAnnotation(String arg_annotation) {
        annotationProperty.set(arg_annotation);
    }
    
    public StringProperty annotationProperty() {
        return this.annotationProperty;
    }

    public String getAnnotation() {
        return this.annotationProperty.get();
    }

    
    
    public BooleanProperty appliedProperty() {
        return this.appliedProperty;
    }
    
    public boolean isApplied() {
        return this.appliedProperty.get();
    }
    
    public void apply(boolean shown) {
        this.appliedProperty.set(shown);
    }
    
}



