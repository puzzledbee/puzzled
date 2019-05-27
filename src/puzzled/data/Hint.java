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
public class Hint implements Dependable {
    
    private HashSet<Dependable> predecessors = new HashSet<Dependable>();
    private HashSet<Dependable> successors = new HashSet<Dependable>();
    private BooleanProperty investigateProperty = new SimpleBooleanProperty(false);
    
    @Override
    public Point2D getCenterPosition(){
        return null;
    }
    
    public void addSuccessor(Dependable successor){
        successors.add(successor);
    }
    
    public HashSet<Dependable> getSuccessors(){
        return successors;
    }
    
    public BooleanProperty investigateProperty() {
        return this.investigateProperty;
    }
}
