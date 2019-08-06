/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class ClueNumber implements Comparable<ClueNumber>{
    //the following need to be properties for the String binding to work
    private IntegerProperty majorProperty = new SimpleIntegerProperty();
    private IntegerProperty minorProperty = new SimpleIntegerProperty();
    private IntegerProperty subProperty = new SimpleIntegerProperty();
    
    //necessary for the TableView
    private StringProperty clueNumberStringProperty = new SimpleStringProperty();
    
    public ClueNumber(int major, int minor, int sub) {
        majorProperty.set(major);
        minorProperty.set(minor);
        subProperty.set(sub);
        
        //bind
        this.clueNumberStringProperty.bind(Bindings.createStringBinding(
                () -> majorProperty.get() +"."+minorProperty.get()+"."+ subProperty.get(),
                majorProperty,minorProperty,subProperty)); //observable dependencies

    }
    
    public ClueNumber() {
        this(1,1,1);
    }
    
    public int getMajor(){
        return this.majorProperty.get();
    }
    
    public ClueNumber getNextMajorClueNumber() {
        return new ClueNumber(majorProperty.get() + 1,1,1);
    }
    
        
    public int getMinor(){
        return this.minorProperty.get();
    }
    
    public ClueNumber getNextMinorClueNumber() {
        return new ClueNumber(majorProperty.get(),minorProperty.get()+1,1);
    }
    
    public int getSub(){
        return this.subProperty.get();
    }
    
    public ClueNumber getNextSubClueNumber() {
        return new ClueNumber(majorProperty.get(),minorProperty.get(),subProperty.get()+1);
    }
    
    public StringProperty clueNumberStringProperty() {
        return this.clueNumberStringProperty;
    }
    
    @Override
    public String toString() {
        return this.clueNumberStringProperty.get();
    }
//    public String getClueNumberString () {
//        return Integer.toString(intMajor) + "." + Integer.toString(intMinor) + "." + Integer.toString(intSub);
//    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (this.getClass() != other.getClass()) return false;
        ClueNumber otherClue = (ClueNumber)other;
        return (this.getMajor() == otherClue.getMajor() && this.getMinor() == otherClue.getMinor() && this.getSub() == otherClue.getSub());
    }
    
    @Override
    public int compareTo(ClueNumber anotherNumber) {
        if (this.getMajor() != anotherNumber.getMajor()) {
            return this.getMajor() - anotherNumber.getMajor();
        } else if (this.getMinor() != anotherNumber.getMinor()) {
            return this.getMinor() - anotherNumber.getMinor();
        } else {
            return this.getSub() - anotherNumber.getSub();
        }
    }
}