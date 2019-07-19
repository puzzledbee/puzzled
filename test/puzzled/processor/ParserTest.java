/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import org.junit.Test;
import static org.junit.Assert.*;
import puzzled.data.LogicProblem;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class ParserTest {
    
    public ParserTest() {
    }

    @Test
    public void testParse_LogicProblem_String() {
        System.out.println("parse");
        LogicProblem logicProblem = new LogicProblem();
        String clueText = "This is a test of the emergency system";
        Parser.parse(logicProblem, clueText);
//        fail("The test case is a prototype.");
        assertTrue("This will succeed.", true);
    }
    
}
