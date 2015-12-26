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
//                                System.out.println("discovered VALUE_YES, setting up the cross");
                                    for (Item itemA : cat1.getItems()){
                                        Relationship rel = relationshipTable.get(new ItemPair(itemA,item2));
                                        if (rel.getValue()==Relationship.ValueType.VALUE_UNKNOWN)  {
                                            //setDirty
                                            rel.setValue(Relationship.ValueType.VALUE_NO);
                                        }
                                    }
                                    for (Item itemB : cat2.getItems()){
                                        Relationship rel = relationshipTable.get(new ItemPair(item1,itemB));
                                        if (rel.getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
                                            //setDirty
                                            rel.setValue(Relationship.ValueType.VALUE_NO);
                                        }
                                    }
                            }
                        }
                    }
                }
            }  
        }
    }
    
    public static void transpose(LogicProblem logicProblem) {
        System.out.println("transpose invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
//        int i = 1;
        
        for (Category cat1 : logicProblem.getCategories()){
            for (Category cat2 : logicProblem.getCategories()){
                if (cat1!=cat2) {
                    for (Item item1 : cat1.getItems()){
                        for (Item item2 : cat2.getItems()){
                            if (relationshipTable.get(new ItemPair(item1,item2)).getValue()==Relationship.ValueType.VALUE_YES) {
//                                System.out.println("transposing for "+item1.getName()+" and "+item2.getName());
                                for (Category catA : logicProblem.getCategories()){
                                    if (catA != cat1 && catA !=cat2) {
                                        for (Item itemA : catA.getItems()) {
                                            Relationship relBase = relationshipTable.get(new ItemPair(itemA,item2));
                                            System.out.println("testing " + " ->"+itemA.getName()+" and "+item2.getName());
                                            if (relBase.getValue()!=Relationship.ValueType.VALUE_UNKNOWN) {
                                                //need to copy
                                                Relationship relCopy = relationshipTable.get(new ItemPair(itemA,item1));
                                                if (relCopy.getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
                                                    //setDirty
                                                    relCopy.setValue(relBase.getValue());
                                                }
                                            } else if (relBase.getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
                                                Relationship relCopy = relationshipTable.get(new ItemPair(itemA,item1));
                                                if (relCopy.getValue()!=Relationship.ValueType.VALUE_UNKNOWN) {
                                                    //setDirty
                                                    relBase.setValue(relCopy.getValue());
                                                }
                                            }
                                        }                                        
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    public static void findUnique(LogicProblem logicProblem) {
        System.out.println("findUnique invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
        
        for (Category cat1 : logicProblem.getCategories()){
            for (Category cat2 : logicProblem.getCategories()){
                if (cat1!=cat2) {
                    for (Item item1 : cat1.getItems()){
                        int counter = 0;
//                        System.out.println("counting "+item1.getName()+" with "+cat2.getName());
                        for (Item item2 : cat2.getItems()){
                            if (relationshipTable.get(new ItemPair(item1,item2)).getValue()==Relationship.ValueType.VALUE_YES) {
                                break;
                            } else if (relationshipTable.get(new ItemPair(item1,item2)).getValue()==Relationship.ValueType.VALUE_NO) {
                                counter++;
                            }
                        }
                        
//                        System.out.println("count is "+counter);
                        if (counter == logicProblem.getNumItems()-1) {
//                            System.out.println("discovered unique possibility at " + item1.getName()+" and "+cat2.getName());
                            for (Item itemB : cat2.getItems()){
                                if (relationshipTable.get(new ItemPair(item1,itemB)).getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
                                    //setDirty
                                    relationshipTable.get(new ItemPair(item1,itemB)).setValue(Relationship.ValueType.VALUE_YES);
                                }
                            }

                        }
                    }
                }
            }
        }
        
    }
    
    
}
