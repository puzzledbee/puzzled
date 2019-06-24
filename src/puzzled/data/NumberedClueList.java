/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class NumberedClueList extends ArrayList<Clue> {
    
    //private ClueNumber latestClueNumber = new ClueNumber();
    private ObjectProperty<ClueNumber> nextClueNumberProperty = new SimpleObjectProperty<ClueNumber>(new ClueNumber());
    
    //this will mostly be called by the Parser
    public void addClue(Clue newClue, ClueNumber nextClueNumber){
        //this.add(new Pair(nextClueNumberProperty.get(),newClue));
        //parse clue text here?
        this.add(newClue);
        //reconfigure
        System.out.println("nextClueNumber 1) : " +getNextClueNumber().clueNumberStringProperty().get());
        nextClueNumberProperty.set(nextClueNumber);
        System.out.println("nextClueNumber 2) : " +getNextClueNumber().clueNumberStringProperty().get());
        //return newClue;
    }

//    public void removeClue(Clue oldClue){
//        Iterator<Clue> itr = this.iterator();
//        //find pair that has the oldClue
//        while (itr.hasNext()) {
//            Pair<ClueNumber,Clue> row = itr.next();
//            if (row.getValue()==oldClue) this.remove(row);
//        }
//    }
    
    public ObjectProperty<ClueNumber> nextClueNumberProperty(){
        return nextClueNumberProperty;
    }
    
    public ClueNumber getNextClueNumber() {
        return nextClueNumberProperty.get();
    }
    
    public void setNextClueNumber(ClueNumber arg_nextClueNumber) {
        nextClueNumberProperty.set(arg_nextClueNumber);
    }
    
}
