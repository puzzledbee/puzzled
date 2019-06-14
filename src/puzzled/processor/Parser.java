/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import puzzled.data.Clue;
import puzzled.data.LogicProblem;

/**
 *
 * @author phiv
 */
public class Parser {
    
    public static void parse(LogicProblem logicProblem, String clueText){
        System.out.println("parsing clue" + clueText);
        //NLP here
        logicProblem.getNumberedClueList().addMajorClue(new Clue(clueText));
        logicProblem.setLogicDirty(true);
    }
    
    
}
