/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.HashSet;
import javafx.beans.property.BooleanProperty;
import javafx.geometry.Point2D;

/**
 *
 * @author phiv
 */
public interface Dependable {
//    private HashSet<Dependable> predecessors = new HashSet<Dependable>();
//    private HashSet<Dependable> sucessors = new HashSet<Dependable>();
    
    
    public Point2D getCenterPosition();
    
    public void addSuccessor(Dependable successor);
    
    public HashSet<Dependable> getSuccessors();
    public BooleanProperty investigateProperty();
    
    //public void addPredecessor(Dependable dependency);
    
    //public void getPredecessor);
}
