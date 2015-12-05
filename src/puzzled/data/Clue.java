/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Fred
 */
public class Clue {
    private SimpleStringProperty clueText = new SimpleStringProperty();
    private ClueType clueType = ClueType.NORMAL;
    
    public Clue(String clueText_arg) {
        clueText.set(clueText_arg);
    }
}
