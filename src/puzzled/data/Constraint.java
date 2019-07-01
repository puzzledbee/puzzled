/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Point2D;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class Constraint extends Dependable {
    private ItemPair itemPair;
    
    //although only two of the three values are possible, using the same
    //enum types makes assignment of the value between Constraint 
    //and the Relationships easier
    private Relationship.ValueType valueType;
    
    
    public Constraint(ItemPair pair, Relationship.ValueType value) {
        this.itemPair = pair;
        this.valueType = value;
    }
    
   
    public Point2D getCenterPosition(){
        return null;
    }
    
    @Override
    public String toString(){
        return "Constraint " + itemPair;
    }
    
}
