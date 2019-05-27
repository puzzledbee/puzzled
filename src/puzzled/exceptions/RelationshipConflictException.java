/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.exceptions;

import puzzled.data.ItemPair;
import puzzled.data.Relationship;

/**
 *
 * @author phiv
 */
public class RelationshipConflictException extends Exception {
    private ItemPair pair;
    private Relationship.ValueType oldValue;
    private Relationship.ValueType newValue;
    
    public RelationshipConflictException(ItemPair arg_pair, Relationship.ValueType arg_oldValue, Relationship.ValueType arg_newValue) {
        this.pair = arg_pair;
        this.oldValue = arg_oldValue;
        this.newValue = arg_newValue;
    }
    @Override
    public String toString() {
        return "Relationship Conflict Exception between the two items:\n"+pair.first().getName()+"\n"+pair.last().getName()+"\n\nfrom:"+oldValue+"\nto:"+newValue;
    }
    
}
