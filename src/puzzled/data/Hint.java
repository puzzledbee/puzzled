/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Point2D;

/**
 *
 * @author Fred
 */
public class Hint implements Dependable {
    
    private List<Dependable> predecessors = new ArrayList<Dependable>();
    private List<Dependable> successors = new ArrayList<Dependable>();
    
    @Override
    public Point2D getCenterPosition(){
        return null;
    }
}
