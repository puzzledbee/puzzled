/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;
import puzzled.data.LogicProblem;
import static org.assertj.core.api.Assertions.*;

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
        String clueText = "Neither Peyton’s book nor the science-fiction novel is Starburst.  Sabrina read exactly one page fewer of the biography than she did of Passion Play.  Frederick (who didn’t pen the fantasy novel) isn’t the one surnamed Wright (who isn’t the one who wrote Haley’s Comet).\"";
        List<String> sentenceList = new ArrayList<String>();
        try {
            sentenceList = Parser.sentenceDetector(clueText);
    //        fail("The test case is a prototype.");
            assertThat(sentenceList).contains("Neither Peyton’s book nor the science-fiction novel is Starburst.")
                    .as("the first element looks legit");
            assertThat(sentenceList.size())
                    .as("list size is %i", sentenceList.size())
                    .isEqualTo(3);
        } catch (Exception e) {
            e.printStackTrace();
            fail("exception! ");
        }
    }
    
}
