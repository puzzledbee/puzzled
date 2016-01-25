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
 * @author Fred
 */
public class SuperfluousRelationshipException extends Exception {
    private ItemPair pair;
    private Relationship.LogicType oldLogic;
    private Relationship.LogicType newLogic;
    
    public SuperfluousRelationshipException(ItemPair arg_pair, Relationship.LogicType arg_oldLogic,Relationship.LogicType arg_newLogic) {
        this.pair = arg_pair;
        this.oldLogic = arg_oldLogic;
        this.newLogic = arg_newLogic;
    }
    @Override
    public String toString() {
        return "Superfluous Relationship Exception between the two items:\n"+pair.first().getName()+"\n"+pair.last().getName()+"\n\n"+oldLogic+"\n"+newLogic;
    }
    
}
