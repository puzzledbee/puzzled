/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import puzzled.data.LogicProblem;

/**
 *
 * @author phiv
 */
public class Parser {
    
    public static void parse(LogicProblem logicProblem){
        System.out.println("parsing clue" + logicProblem.getClues().get(logicProblem.getClues().size()-1));
        logicProblem.setLogicDirty(true);
    }
    
    
}
