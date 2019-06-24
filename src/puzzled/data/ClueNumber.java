/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class ClueNumber {
    private int intMajor;
    private int intMinor;
    private int intSub;
    
    //necessary for the TableView
    private StringProperty stringProperty = new SimpleStringProperty();
    
    public ClueNumber(int major, int minor, int sub) {
        intMajor = major;
        intMinor = minor;
        intSub = sub;
        
        //bind
        stringProperty.bind(Bindings.createStringBinding(() -> intMajor +"."+intMinor+"."+ intSub));

    }
    
    public ClueNumber() {
        this(1,1,1);
    }
    
    public int getMajor(){
        return intMajor;
    }
    
    public ClueNumber getNextMajor() {
        return new ClueNumber(intMajor + 1,1,1);
    }
    
        
    public int getMinor(){
        return intMinor;
    }
    
    public ClueNumber getNextMinor() {
        return new ClueNumber(intMajor,intMinor+1,1);
    }
    
    public int getSub(){
        return intSub;
    }
    
    public ClueNumber getNextSub() {
        return new ClueNumber(intMajor,intMinor,intSub+1);
    }
    

    public StringProperty getStringProperty() {
        return this.stringProperty;
    }
    
//    public String getClueNumberString () {
//        return Integer.toString(intMajor) + "." + Integer.toString(intMinor) + "." + Integer.toString(intSub);
//    }
}
