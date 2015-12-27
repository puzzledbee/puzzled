/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    private List<Dependable> successors = new ArrayList<Dependable>();
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());

    public Relationship() {
//        System.out.println("creating relationship #" + index);
        valueProperty.addListener( (e,oldValue,newValue) -> fLogger.info("Relationship valueProperty chansged to: " + newValue));
    }
    public Relationship(ValueType myType) {
        valueProperty.set(myType);
        valueProperty.addListener( (e,oldValue,newValue) -> fLogger.info("Relationship valueProperty changed to: " + newValue));
    }
    
    public ObjectProperty<ValueType> valueProperty(){
        return valueProperty;
    }
    
    public ValueType getValue(){
        return valueProperty.get();
    }
    
    public void setValue(ValueType value){
        valueProperty.set(value);
    }
    
    @Override
    public Point2D getCenterPosition(){
        return null;
    }
}
