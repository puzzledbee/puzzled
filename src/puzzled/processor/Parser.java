/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import java.awt.event.ActionEvent;
import puzzled.data.Clue;
import puzzled.data.ClueNumber;
import puzzled.data.LogicProblem;

/**
 *
 * @author phiv
 */
public class Parser {
    
    public static void parse(LogicProblem logicProblem, String clueText, boolean isCtrlDown, boolean isAltDown){
        System.out.println("parsing clue " + clueText);

       
        ClueNumber nextClueNumber = logicProblem.getNumberedClueList().getNextClueNumberProperty().get();
        //however, the last fragment needs to be added with the modifiers in mind so that the 
        //next clue number matches the intent of the user
        //in the case of a file input, this step will result in the next clue
        //number to be an major increment

        //NLP here
        //the parser needs to be able to add clue fragments (minor, subs)
        //by breaking down the sentence

        //modifier logic here
        if (isCtrlDown) {
            logicProblem.getNumberedClueList().addClue(new Clue(nextClueNumber,clueText,Clue.ClueType.NORMAL),nextClueNumber.getNextMinor());
        } else if (isAltDown) {
            logicProblem.getNumberedClueList().addClue(new Clue(nextClueNumber,clueText,Clue.ClueType.NORMAL),nextClueNumber.getNextSub());
        } else {
            logicProblem.getNumberedClueList().addClue(new Clue(nextClueNumber,clueText,Clue.ClueType.NORMAL),nextClueNumber.getNextMajor());
        }
        logicProblem.setDirtyLogic(true);
    }
    
    public static void parse(LogicProblem logicProblem, String clueText){
        Parser.parse(logicProblem, clueText, false, false);
    }
    
}
