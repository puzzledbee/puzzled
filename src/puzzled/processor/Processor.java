/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;
import javafx.util.Duration;
import puzzled.data.Category;
import puzzled.data.Dependable;
import puzzled.data.Item;
import puzzled.data.ItemPair;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;
import puzzled.exceptions.RelationshipConflictException;
import puzzled.exceptions.SuperfluousRelationshipException;
import org.controlsfx.control.Notifications;

/**
 *
 * @author phiv
 */
public class Processor {
    
    
    public static void cross(LogicProblem logicProblem) throws RelationshipConflictException, SuperfluousRelationshipException {
        System.out.println("cross invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();

        HashSet<TreeSet<Category>> categoryPairs = logicProblem.getCategoryPairs();
        
        for (TreeSet<Category> categoryPair : categoryPairs) {
        //for (Category cat1 : logicProblem.getCategories()){
        //    for (Category cat2 : logicProblem.getCategories()){
        //        if (cat1!=cat2) {
            Category cat1 = categoryPair.first();
            Category cat2 = categoryPair.last();

        
        //for (Category cat1 : logicProblem.getCategories()){
        //    for (Category cat2 : logicProblem.getCategories()){
        //        if (cat1!=cat2) {
            for (Item item1 : cat1.getItems()){
                for (Item item2 : cat2.getItems()){
                    Relationship sourceRelationship = relationshipTable.get(new ItemPair(item1,item2));
                    if (sourceRelationship.getValue()==Relationship.ValueType.VALUE_YES) {
//                                System.out.println("discovered VALUE_YES, setting up the cross");
                            for (Item itemA : cat1.getItems()){
                                if (itemA != item1) {
                                    Relationship rel = relationshipTable.get(new ItemPair(itemA,item2));
                                    rel.setValue(Relationship.ValueType.VALUE_NO,Relationship.LogicType.CROSS,sourceRelationship);
                                }
                            }
                            for (Item itemB : cat2.getItems()){
                                if (itemB != item2) {
                                    Relationship rel = relationshipTable.get(new ItemPair(item1,itemB));
                                    rel.setValue(Relationship.ValueType.VALUE_NO,Relationship.LogicType.CROSS,sourceRelationship);
                                }
                            }
                    }
                }
            }
        }
            
        
    }
    
    public static void transpose(LogicProblem logicProblem) throws RelationshipConflictException, SuperfluousRelationshipException {
        System.out.println("transpose invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
//      it is best to work with the category pair as a set without duplication, instead of processing each category pair twice
//      e.g. Books vs Pages and Pages vs Books
        HashSet<TreeSet<Category>> categoryPairs = logicProblem.getCategoryPairs();
        
        for (TreeSet<Category> categoryPair : categoryPairs) {
        //for (Category cat1 : logicProblem.getCategories()){
        //    for (Category cat2 : logicProblem.getCategories()){
        //        if (cat1!=cat2) {
            Category cat1 = categoryPair.first();
            Category cat2 = categoryPair.last();
            
            for (Item item1 : cat1.getItems()){
                for (Item item2 : cat2.getItems()){
                    Relationship sourceRelationship = relationshipTable.get(new ItemPair(item1,item2));
                    //searching for a VALUE_YES relationship first
                    if (sourceRelationship.getValue()==Relationship.ValueType.VALUE_YES) {
                        System.out.println("transposing for "+item1.getName()+" and "+item2.getName());
                        for (Category catA : logicProblem.getCategories()){
                            if (catA != cat1 && catA != cat2) {
                                for (Item itemA : catA.getItems()) {
                                    Relationship relBase = relationshipTable.get(new ItemPair(item1,itemA));
                                    Relationship relCopy = relationshipTable.get(new ItemPair(itemA,item2));
                                    //System.out.println("testing->"+item1.getName()+" and "+itemA.getName()+" with value "+relBase.getValue());
                                   
                                    if (relBase.getValue()!= Relationship.ValueType.VALUE_UNKNOWN) relCopy.setValue(relBase.getValue(), Relationship.LogicType.TRANSPOSE,sourceRelationship, relBase);
                                    if (relCopy.getValue()!= Relationship.ValueType.VALUE_UNKNOWN) relBase.setValue(relCopy.getValue(), Relationship.LogicType.TRANSPOSE,sourceRelationship, relCopy);
                                }                                        
                            }
                        }
                    }
                }
            }
        }
    }
    
    
    public static void uniqueness(LogicProblem logicProblem) throws RelationshipConflictException, SuperfluousRelationshipException {
        System.out.println("uniqueness invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
        HashSet<TreeSet<Category>> categoryPairs = logicProblem.getCategoryPairs();
        
        for (TreeSet<Category> categoryPair : categoryPairs) {
        //for (Category cat1 : logicProblem.getCategories()){
        //    for (Category cat2 : logicProblem.getCategories()){
        //        if (cat1!=cat2) {
            Category cat1 = categoryPair.first();
            Category cat2 = categoryPair.last();
        
        //for (Category cat1 : logicProblem.getCategories()){
        //    for (Category cat2 : logicProblem.getCategories()){
        //        if (cat1!=cat2) {
            
            
            
            for (Item item1 : cat1.getItems()){
                ArrayList<Relationship> verticalNoRelationships = new ArrayList<Relationship>();
//                        System.out.println("counting "+item1.getName()+" with "+cat2.getName());
                for (Item item2 : cat2.getItems()){
                    Relationship relationship = relationshipTable.get(new ItemPair(item1,item2));
                    if (relationship.getValue()==Relationship.ValueType.VALUE_YES) {
                        break;
                    } else if (relationship.getValue()==Relationship.ValueType.VALUE_NO) {
                        verticalNoRelationships.add(relationship);
                    }
                }

                
                //debug
                //System.out.println("\tsize of noRelationships: " + noRelationships.size());
                //System.out.println("\tsize of getNumItems: " + (logicProblem.getNumItems()-1));
                //are there enough VALUE_NO to force a VALUE_YES?
                if (verticalNoRelationships.size() == logicProblem.getNumItems()-1) {
//                          //search item that does not have a VALUE_NO and set it
                    for (Item itemB : cat2.getItems()){
                        Relationship rel = relationshipTable.get(new ItemPair(item1,itemB));
                        if (rel.getValue()!=Relationship.ValueType.VALUE_NO) {
                            rel.setValue(Relationship.ValueType.VALUE_YES, Relationship.LogicType.UNIQUE,verticalNoRelationships.toArray(new Relationship[verticalNoRelationships.size()]));
                            Notifications.create()
                                .title("New relationship discovered!")
                                .text("Uniqueness!")
                                
                                .hideAfter(new Duration(2000))
                                .show();
                            
                        }
                    }

                }

            }
            for (Item item1 : cat2.getItems()){
                ArrayList<Relationship> horizontalNoRelationships = new ArrayList<Relationship>();
//                        System.out.println("counting "+item1.getName()+" with "+cat2.getName());
                for (Item item2 : cat1.getItems()){
                    Relationship relationship = relationshipTable.get(new ItemPair(item1,item2));
                    if (relationship.getValue()==Relationship.ValueType.VALUE_YES) {
                        break;
                    } else if (relationship.getValue()==Relationship.ValueType.VALUE_NO) {
                        horizontalNoRelationships.add(relationship);
                    }
                }

                
                //debug
                //System.out.println("\tsize of noRelationships: " + noRelationships.size());
                //System.out.println("\tsize of getNumItems: " + (logicProblem.getNumItems()-1));
                //are there enough VALUE_NO to force a VALUE_YES?
                if (horizontalNoRelationships.size() == logicProblem.getNumItems()-1) {
//                          //search item that does not have a VALUE_NO and set it
                    for (Item itemB : cat1.getItems()){
                        Relationship rel = relationshipTable.get(new ItemPair(item1,itemB));
                        if (rel.getValue()!=Relationship.ValueType.VALUE_NO)
                            rel.setValue(Relationship.ValueType.VALUE_YES, Relationship.LogicType.UNIQUE,horizontalNoRelationships.toArray(new Relationship[horizontalNoRelationships.size()]));
                    }

                }
                
            }
        }

    }
    
    public static void commonality(LogicProblem logicProblem) throws RelationshipConflictException, SuperfluousRelationshipException {
        System.out.println("commonality invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
//        int i = 1;
        HashSet<TreeSet<Category>> categoryPairs = logicProblem.getCategoryPairs();
        
        for (TreeSet<Category> categoryPair : categoryPairs) {
        //for (Category cat1 : logicProblem.getCategories()){
        //    for (Category cat2 : logicProblem.getCategories()){
        //        if (cat1!=cat2) {
            Category cat1 = categoryPair.first();
            Category cat2 = categoryPair.last();
        //for (Category cat1 : logicProblem.getCategories()){
        //    for (Category cat2 : logicProblem.getCategories()){
        //        if (cat1!=cat2) {
            for (Item item1 : cat1.getItems()){
                ArrayList<Item> candidateList = new ArrayList<Item>();
                ArrayList<Dependable> predecessors1 = new ArrayList<Dependable>();
                for (Item item2 : cat2.getItems()){
                    Relationship sourceRelationship = relationshipTable.get(new ItemPair(item1,item2));
                    if (sourceRelationship.getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
                        candidateList.add(item2);
                        predecessors1.add(sourceRelationship);
                    }
                }
//                        System.out.println("assessing "+cat1.getName()+","+item1.getName()+" vs "+cat2.getName()+"  -> size: "+searchList.size());
                if (candidateList.size()>1 && candidateList.size()<=cat2.getNumItems()/2) {
                    System.out.println("found commonality candidate "+cat1.getName()+","+item1.getName()+" vs "+cat2.getName());
                    for (Category catSearch: logicProblem.getCategories()) {
                        if (catSearch!=cat1 && catSearch!=cat2) {
                            for (Item itemSearch : catSearch.getItems()) {
                                ArrayList<Item> searchList = new ArrayList<Item>();
                                ArrayList<Dependable> predecessors2 = new ArrayList<Dependable>();
                                for (Item candidate : candidateList) {
                                    Relationship searchRelationship = relationshipTable.get(new ItemPair(itemSearch,candidate));
                                    if (searchRelationship.getValue()==Relationship.ValueType.VALUE_NO) {
                                        searchList.add(itemSearch);
                                        predecessors2.add(searchRelationship);
                                    }
                                }
                                if (searchList.size()==candidateList.size()) {
                                    //System.out.println("discovered new commonalisty for " + cat1.getName()+","+item1.getName()+" vs "+cat2.getName() + " at "+catSearch.getName()+","+itemSearch.getName());
                                    predecessors1.addAll(predecessors2); //merge predecessors
                                    relationshipTable.get(new ItemPair(item1,itemSearch)).setValue(Relationship.ValueType.VALUE_NO, Relationship.LogicType.COMMON,predecessors1.toArray(new Relationship[predecessors1.size()]));

                                }
                            }
                        }
                    }
                }
            }
        }
    }


}