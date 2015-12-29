/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import java.util.ArrayList;
import java.util.HashMap;
import puzzled.data.Category;
import puzzled.data.Dependable;
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
//        System.out.println("cross invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
        for (Category cat1 : logicProblem.getCategories()){
            for (Category cat2 : logicProblem.getCategories()){
                if (cat1!=cat2) {
                    for (Item item1 : cat1.getItems()){
                        for (Item item2 : cat2.getItems()){
                            Relationship sourceRelationship = relationshipTable.get(new ItemPair(item1,item2));
                            if (sourceRelationship.getValue()==Relationship.ValueType.VALUE_YES) {
//                                System.out.println("discovered VALUE_YES, setting up the cross");
                                    for (Item itemA : cat1.getItems()){
                                        Relationship rel = relationshipTable.get(new ItemPair(itemA,item2));
                                        if (rel.getValue()==Relationship.ValueType.VALUE_UNKNOWN)  {
                                            rel.setValue(Relationship.ValueType.VALUE_NO,sourceRelationship);
                                        }
                                    }
                                    for (Item itemB : cat2.getItems()){
                                        Relationship rel = relationshipTable.get(new ItemPair(item1,itemB));
                                        if (rel.getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
                                            rel.setValue(Relationship.ValueType.VALUE_NO, sourceRelationship);
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
//        System.out.println("transpose invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
//        int i = 1;
        
        for (Category cat1 : logicProblem.getCategories()){
            for (Category cat2 : logicProblem.getCategories()){
                if (cat1!=cat2) {
                    for (Item item1 : cat1.getItems()){
                        for (Item item2 : cat2.getItems()){
                            Relationship sourceRelationship = relationshipTable.get(new ItemPair(item1,item2));
                            if (sourceRelationship.getValue()==Relationship.ValueType.VALUE_YES) {
//                                System.out.println("transposing for "+item1.getName()+" and "+item2.getName());
                                for (Category catA : logicProblem.getCategories()){
                                    if (catA != cat1 && catA != cat2) {
                                        for (Item itemA : catA.getItems()) {
                                            Relationship relBase = relationshipTable.get(new ItemPair(item1,itemA));
                                            Relationship relCopy = relationshipTable.get(new ItemPair(itemA,item2));
//                                            System.out.println("testing->"+item1.getName()+" and "+itemA.getName()+" with value "+relBase.getValue());
                                            if (relBase.getValue()!=Relationship.ValueType.VALUE_UNKNOWN && relCopy.getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
//                                                System.out.println("this value needs transposing ->"+item1.getName()+" and "+itemA.getName());
                                                //need to copy
                                               
                                                relCopy.setValue(relBase.getValue(), sourceRelationship, relBase);
                                                
                                            } else if (relBase.getValue()==Relationship.ValueType.VALUE_UNKNOWN && relCopy.getValue()!=Relationship.ValueType.VALUE_UNKNOWN) {
                                            
                                                relBase.setValue(relCopy.getValue(), sourceRelationship, relCopy);
                                                
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
//        System.out.println("findUnique invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
        
        for (Category cat1 : logicProblem.getCategories()){
            for (Category cat2 : logicProblem.getCategories()){
                if (cat1!=cat2) {
                    for (Item item1 : cat1.getItems()){
                        
                        ArrayList<Dependable> noRelationships = new ArrayList<Dependable>();
//                        System.out.println("counting "+item1.getName()+" with "+cat2.getName());
                        for (Item item2 : cat2.getItems()){
                            Relationship relationship = relationshipTable.get(new ItemPair(item1,item2));
                            if (relationship.getValue()==Relationship.ValueType.VALUE_YES) {
                                break;
                            } else if (relationship.getValue()==Relationship.ValueType.VALUE_NO) {
                                noRelationships.add(relationship);
                            }
                        }
                        
//                        System.out.println("count is "+counter);
                        if (noRelationships.size() == logicProblem.getNumItems()-1) {
//                            System.out.println("discovered unique possibility at " + item1.getName()+" and "+cat2.getName());
                            for (Item itemB : cat2.getItems()){
                                if (relationshipTable.get(new ItemPair(item1,itemB)).getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
                                    
                                    relationshipTable.get(new ItemPair(item1,itemB)).setValue(Relationship.ValueType.VALUE_YES, noRelationships.toArray(new Relationship[noRelationships.size()]));
                                }
                            }

                        }
                    }
                }
            }
        }
        
    }
    
    
}
