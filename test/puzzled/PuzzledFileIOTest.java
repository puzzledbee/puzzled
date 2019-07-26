/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.io.File;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.Test;
import static org.junit.Assert.*;
import puzzled.data.LogicProblem;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class PuzzledFileIOTest {
        LogicProblem logicProblem;
//    PuzzledController controller;
    
    public PuzzledFileIOTest() {
    }

    @Test
    public void testSaveFile() throws Exception {
        try {
            this.logicProblem = PuzzledFileIO.loadProblem("resources/samples/problem47.lps");
            this.logicProblem.initializeRelationshipTable();
            PuzzledFileIO.saveFile("resources/samples/problem47.lpf", logicProblem);
        } catch (Exception e) {
            System.out.println("unable to load and save problem file");
            fail("exception thrown");
        }
    }
    
}
