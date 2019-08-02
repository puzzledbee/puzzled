/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class NumberedClueList {
    
    //private ClueNumber latestClueNumber = new ClueNumber();
    private ObjectProperty<ClueNumber> nextClueNumberProperty = new SimpleObjectProperty<ClueNumber>(new ClueNumber());
    private List<Clue> clueList = new ArrayList<Clue>();
    //is it necessary to have both the ArrayList and the observable list
//    or can that be collapsed like members and properties are collapsed elsewhere
    private ObservableList<Clue> observableClueList;
    
    public NumberedClueList(){
        //wrap clue list into an Observable object for the TableView
        this.observableClueList = FXCollections.observableList(clueList);
    }
    //this will mostly be called by the Parser
    public void addClue(Clue newClue, ClueNumber nextClueNumber){
        //this.add(new Pair(nextClueNumberProperty.get(),newClue));
        //parse clue text here?
        this.observableClueList.add(newClue);
        //reconfigure
        //System.out.println("nextClueNumber 1) : " +getNextClueNumber().clueNumberStringProperty().get());
        this.nextClueNumberProperty.set(nextClueNumber);
//        System.out.println("nextClueNumber 2) : " +getNextClueNumber().clueNumberStringProperty().get());
        //return newClue;
    }

    public ObservableList<Clue> getObservableClueList() {
        return this.observableClueList;
    }
    
    public void clear(){
        this.observableClueList.clear();
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
        return this.nextClueNumberProperty;
    }
    
    public ClueNumber getNextClueNumber() {
        return this.nextClueNumberProperty.get();
    }
    
    public void setNextClueNumber(ClueNumber arg_nextClueNumber) {
//        System.out.println("action taken");
        this.nextClueNumberProperty.set(arg_nextClueNumber);
    }
    
    public ObjectProperty<ClueNumber> lastClueNumberProperty() {
        return (this.clueList.isEmpty()?null:this.clueList.get(clueList.size()-1).clueNumberProperty());
    }
    
    public ClueNumber getLastClueNumber() {
        return (this.clueList.isEmpty()?null:lastClueNumberProperty().get());
    }
    
    //for testing assertions
    public List<Clue> getClueList() {
        return this.clueList;
    }
    
}
