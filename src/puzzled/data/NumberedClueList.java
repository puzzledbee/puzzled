/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.Iterator;
import javafx.util.Pair;
import puzzled.processor.Parser;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class NumberedClueList extends ArrayList<Pair<ClueNumber,Clue>> {
    
    public void addMajorClue(Clue newClue){
        this.add(new Pair(getLastClueNumber().getNextMajor(),newClue));
    }

    public void addMinorClue(Clue newClue){
        this.add(new Pair(getLastClueNumber().getNextMinor(),newClue));
    }
    
    public void addSubClue(Clue newClue){
        this.add(new Pair(getLastClueNumber().getNextSub(),newClue));
    }

    public void removeClue(Clue oldClue){
        Iterator<Pair<ClueNumber,Clue>> itr = this.iterator();
        //find pair that has the oldClue
        while (itr.hasNext()) {
            Pair<ClueNumber,Clue> row = itr.next();
            if (row.getValue()==oldClue) this.remove(row);
        }
    }
    
    public ClueNumber getLastClueNumber() {
        return (this.isEmpty()?(new ClueNumber(0,0,0)):this.get(this.size() - 1).getKey());
    }
          
}
