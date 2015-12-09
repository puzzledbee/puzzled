/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Point2D;

/**
 *
 * @author Fred
 */
public class Clue implements Dependable {

    
    public enum ClueType {
        NORMAL,
        SPECIAL //needs re-assessment, see notebook for SpecialClue
    }
    
    
    private List<Dependable> successors = new ArrayList<Dependable>();
    
    List<Dependable> dependencies = new ArrayList<Dependable>(10);
    private SimpleStringProperty clueText = new SimpleStringProperty();
    private ClueType clueType = ClueType.NORMAL;
    
    public Clue(String clueText_arg) {
        clueText.set(clueText_arg);
    }
    
    
    
    public Point2D getCenterPosition(){
        return null;
    }
}
