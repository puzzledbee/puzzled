/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

/**
 *
 * @author Fred
 */
public class Relationship {
    
    
    public static enum ValueTypes {
        VALUE_YES, VALUE_NO, VALUE_UNKNOWN 
    }
    
    private ValueTypes value;

    public Relationship(ValueTypes value) {
        this.value = value;
    }
    
}
