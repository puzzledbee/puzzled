/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import puzzled.Puzzled;
import puzzled.data.Clue;
import puzzled.data.ClueNumber;
import puzzled.data.LogicProblem;

/**
 *
 * @author phiv
 */
public class Parser {
    
    public static void parse(LogicProblem logicProblem, String clueText, boolean isCtrlDown, boolean isAltDown){
        System.out.println("Parser parsing clue " + clueText);

        ClueNumber nextClueNumber = logicProblem.getNumberedClueList().getNextClueNumber();
        //however, the last fragment needs to be added with the modifiers in mind so that the 
        //next clue number matches the intent of the user
        //in the case of a file input, this step will result in the next clue
        //number to be an major increment

        //NLP here
        //the parser needs to be able to add clue fragments (minor, subs)
        //by breaking down the sentence
        List<String> sentenceList = new ArrayList<String>();
        
        try {
            sentenceList = sentenceDetector(clueText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //all element but last
        for (int i=0; i< sentenceList.size() -1; i++) {
            logicProblem.getNumberedClueList().addClue(
                    new Clue(nextClueNumber,sentenceList.get(i),Clue.ClueType.NORMAL),nextClueNumber.getNextMinorClueNumber());
                    nextClueNumber = logicProblem.getNumberedClueList().getNextClueNumber();
        }
        
        //last element
        if (isCtrlDown) {
            logicProblem.getNumberedClueList().addClue(
                    new Clue(nextClueNumber,sentenceList.get(sentenceList.size() -1),Clue.ClueType.NORMAL),nextClueNumber.getNextMinorClueNumber());
        } else if (isAltDown) {
            logicProblem.getNumberedClueList().addClue(
                    new Clue(nextClueNumber,sentenceList.get(sentenceList.size() -1),Clue.ClueType.NORMAL),nextClueNumber.getNextSubClueNumber());
        } else {
            logicProblem.getNumberedClueList().addClue(
                    new Clue(nextClueNumber,sentenceList.get(sentenceList.size() -1),Clue.ClueType.NORMAL),nextClueNumber.getNextMajorClueNumber());
        }
        
        logicProblem.setDirtyLogic(true);
        
    }
    
    public static void parse(LogicProblem logicProblem, String clueText){
        Parser.parse(logicProblem, clueText, false, false);
    }
    
    public static List<String> sentenceDetector(String inputText) throws Exception {
 
        InputStream is = Puzzled.class.getClassLoader().getResourceAsStream("NLPmodels/en-sent.bin");
        SentenceModel model = new SentenceModel(is);

        SentenceDetectorME sdetector = new SentenceDetectorME(model);

        String sentences[] = sdetector.sentDetect(inputText);
//        System.out.println(Arrays.toString(sentences));
        return Arrays.asList(sentences);  
    }
    
}
