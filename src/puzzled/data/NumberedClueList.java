/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.Iterator;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Pair;
import puzzled.processor.Parser;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class NumberedClueList extends ArrayList<Pair<ClueNumber,Clue>> {
    
    private ClueNumber clueNumber = new ClueNumber();
    private ObjectProperty<ClueNumber> nextClueNumberProperty = new SimpleObjectProperty(clueNumber);
    
   
    public ClueNumber addMajorClue(Clue newClue){
        this.add(new Pair(nextClueNumberProperty.get(),newClue));
        nextClueNumberProperty.set(getLastClueNumber().getNextMajor());
        return nextClueNumberProperty.get();
    }

    public ClueNumber addMinorClue(Clue newClue){
        this.add(new Pair(nextClueNumberProperty.get(),newClue));
        nextClueNumberProperty.set(getLastClueNumber().getNextMinor());
        return nextClueNumberProperty.get();
    }
    
    public ClueNumber addSubClue(Clue newClue){
        this.add(new Pair(nextClueNumberProperty.get(),newClue));
        nextClueNumberProperty.set(getLastClueNumber().getNextSub());
        return nextClueNumberProperty.get();
    }

    public ClueNumber getClueNumber(Clue clue){
        Iterator<Pair<ClueNumber,Clue>> itr = this.iterator();
        ClueNumber returnValue = null;
        
        //find pair that has the oldClue
        while (itr.hasNext()) {
            Pair<ClueNumber,Clue> row = itr.next();
            if (row.getValue()==clue) returnValue=row.getKey();
        }
        return returnValue;
    }
    
    public String getClueNumberAsString(Clue clue) {
        ClueNumber clueNumber = getClueNumber(clue);
        
        return (clueNumber == null)?"":clueNumber.getClueNumberString();
        
    }

    public void removeClue(Clue oldClue){
        Iterator<Pair<ClueNumber,Clue>> itr = this.iterator();
        //find pair that has the oldClue
        while (itr.hasNext()) {
            Pair<ClueNumber,Clue> row = itr.next();
            if (row.getValue()==oldClue) this.remove(row);
        }
    }
    
    public ObjectProperty<ClueNumber> getNextClueNumberProperty(){
        return nextClueNumberProperty;
    }
    
    public ClueNumber getLastClueNumber() {
        return (this.isEmpty()?(null):this.get(this.size() - 1).getKey());
    }
}
