/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import java.util.HashMap;
import puzzled.data.Category;
import puzzled.data.Item;
import puzzled.data.ItemPair;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;

/**
 *
 * @author Fred
 */
public class Processor {
    
    
    public static void cross(LogicProblem logicProblem){
        System.out.println("cross invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
        for (Category cat1 : logicProblem.getCategories()){
            for (Category cat2 : logicProblem.getCategories()){
                if (cat1!=cat2) {
                    for (Item item1 : cat1.getItems()){
                        for (Item item2 : cat2.getItems()){
                            if (relationshipTable.get(new ItemPair(item1,item2)).getValue()==Relationship.ValueType.VALUE_YES) {
                                System.out.println("discovered VALUE_YES, setting up the cross");
                                    for (Item itemA : cat1.getItems()){
                                        Relationship rel = relationshipTable.get(new ItemPair(itemA,item2));
                                        if (rel.getValue()==Relationship.ValueType.VALUE_UNKNOWN)  rel.setValue(Relationship.ValueType.VALUE_NO);
                                    }
                                    for (Item itemB : cat2.getItems()){
                                        Relationship rel = relationshipTable.get(new ItemPair(item1,itemB));
                                        if (rel.getValue()==Relationship.ValueType.VALUE_UNKNOWN)  rel.setValue(Relationship.ValueType.VALUE_NO);
                                    }
                            }
                        }
                    }
                }
            }  
        }
    }
    
}
