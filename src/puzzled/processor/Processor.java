/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javafx.beans.property.IntegerProperty;
import puzzled.data.Category;
import puzzled.data.Dependable;
import puzzled.data.Item;
import puzzled.data.ItemPair;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;
import puzzled.exceptions.RelationshipConflictException;
import puzzled.data.CategoryPair;

/**
 *
 * @author phiv
 */
public class Processor {
    
    /***
    //whatever you do, don't pass a PuzzledController to these 
    //static methods; else JUnit testing will require a full UI!
    */
    
    //applyChanges: determines if the relationships found should be applied or invisible
    public static void process(LogicProblem logicProblem, boolean applyChanges) {
//        System.out.println("process invoked "+automaticProcessingMenuItem.isSelected());
//        IntegerProperty newlyDiscoveredRelationships =  controller.pendingRelationshipsCounterProperty();
//        boolean automaticProcessing = controller.getAutomaticProcessing();
        
//        System.out.println("entering processing loop");
        while (logicProblem.isLogicDirty()){
//                System.out.println("executing processingFlag loop");
            logicProblem.setDirtyLogic(false);
            
            //re-process SPECIAL clues (with streams and filters maybe?)
            try {
//                System.out.println("\t cross -> "+ logicProblem.isLogicDirty());
                Processor.cross(logicProblem, applyChanges);
//                System.out.println("\t unique -> "+ logicProblem.isLogicDirty());
                Processor.uniqueness(logicProblem, applyChanges);
//                System.out.println("\t transpose -> "+ logicProblem.isLogicDirty());
                Processor.transpose(logicProblem, applyChanges);
                
                if (logicProblem.getNumItems() >3) 
//                    System.out.println("\t commonality -> "+ logicProblem.isLogicDirty());
                    Processor.commonality(logicProblem, applyChanges);
            } catch (RelationshipConflictException e) {
//                    controller.notify(PuzzledController.WarningType.WARNING, e.toString());
            }
//            System.out.println("now what? -> "+ logicProblem.isLogicDirty());
        }
//        System.out.println("exiting processing loop");
    }
            
 
    //public for JUnit
    public static void cross(LogicProblem logicProblem, boolean applyChanges) 
            throws RelationshipConflictException {
//        System.out.println("cross invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();

        HashSet<CategoryPair> categoryPairs = logicProblem.getCategoryPairs();
        
        for (CategoryPair categoryPair : categoryPairs) {
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
                                    // could track superfluous and conflicts here
                                    if (rel.getValue() == Relationship.ValueType.VALUE_UNKNOWN) {
                                          Processor.setRelationship(logicProblem, applyChanges, rel, 
                                                  Relationship.ValueType.VALUE_NO,
                                                  Relationship.LogicType.CROSS, sourceRelationship);
//                                        rel.setValue(Relationship.ValueType.VALUE_NO,Relationship.LogicType.CROSS,sourceRelationship);
//                                        System.out.println("created VALUE_NO relationship between "+itemA.getName()+" and "+item2.getName()+ " (CROSS)");
                                    }
                                }
                            }
                            for (Item itemB : cat2.getItems()){
                                if (itemB != item2) {
                                    Relationship rel = relationshipTable.get(new ItemPair(item1,itemB));
                                    // could track superfluous and conflicts here
                                    if (rel.getValue() == Relationship.ValueType.VALUE_UNKNOWN) {
                                        Processor.setRelationship(logicProblem, applyChanges, rel, 
                                                  Relationship.ValueType.VALUE_NO,
                                                  Relationship.LogicType.CROSS, sourceRelationship);
//                                        rel.setValue(Relationship.ValueType.VALUE_NO,Relationship.LogicType.CROSS,sourceRelationship);
//                                        System.out.println("created VALUE_NO relationship between "+item1.getName()+" and "+itemB.getName()+ " (CROSS)");
                                    }
                                }
                            }
                    }
                }
            }
        }
    }
    
    public static void transpose(LogicProblem logicProblem, boolean applyChanges)
            throws RelationshipConflictException 
                                                                                    {
//        System.out.println("transpose invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
//      it is best to work with the category pair as a set without duplication, instead of processing each category pair twice
//      e.g. Books vs Pages and Pages vs Books
        HashSet<CategoryPair> categoryPairs = logicProblem.getCategoryPairs();
        
        for (CategoryPair categoryPair : categoryPairs) {
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
                        for (Category catA : logicProblem.getCategoriesList()){
                            if (catA != cat1 && catA != cat2) {
                                for (Item itemA : catA.getItems()) {
                                    Relationship relBase = relationshipTable.get(new ItemPair(item1,itemA));
                                    Relationship relCopy = relationshipTable.get(new ItemPair(itemA,item2));
                                    //System.out.println("testing->"+item1.getName()+" and "+itemA.getName()+" with value "+relBase.getValue());
                                   
                                    if (relBase.getValue()!= Relationship.ValueType.VALUE_UNKNOWN) {
                                        Processor.setRelationship(logicProblem, applyChanges, relCopy, 
                                                  relBase.getValue(),
                                                  Relationship.LogicType.TRANSPOSE, sourceRelationship, relBase);
//                                        relCopy.setValue(relBase.getValue(), Relationship.LogicType.TRANSPOSE,sourceRelationship, relBase);
//                                        System.out.println("created VALUE_YES relationship between "+item1.getName()+" and "+itemA.getName()+ " (TRANSPOSE)");
                                    }
                                    if (relCopy.getValue()!= Relationship.ValueType.VALUE_UNKNOWN) {
                                        Processor.setRelationship(logicProblem, applyChanges, relBase, 
                                                  relCopy.getValue(),
                                                  Relationship.LogicType.TRANSPOSE, sourceRelationship, relCopy);
//                                        relBase.setValue(relCopy.getValue(), Relationship.LogicType.TRANSPOSE,sourceRelationship, relCopy);
//                                        System.out.println("created VALUE_YES relationship between "+itemA.getName()+" and "+item2.getName()+ " (TRANSPOSE)");
                                    }
                                }                                        
                            }
                        }
                    }
                }
            }
        }
    }
    
    public static void uniqueness(LogicProblem logicProblem, boolean applyChanges)
            throws RelationshipConflictException {
//        System.out.println("uniqueness invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        HashSet<CategoryPair> categoryPairs = logicProblem.getCategoryPairs();
        
        for (CategoryPair categoryPair : categoryPairs) {

            Category cat1 = categoryPair.first();
            Category cat2 = categoryPair.last();
        
            for (Item item1 : cat1.getItems()){
                ArrayList<Relationship> verticalNoRelationships = new ArrayList<Relationship>();
//                        System.out.println("counting "+item1.getName()+" with "+cat2.getName());
                for (Item item2 : cat2.getItems()){
                    Relationship relationship = relationshipTable.get(new ItemPair(item1,item2));
                    if (relationship.getValue()==Relationship.ValueType.VALUE_YES) {
                        verticalNoRelationships.clear();
                        break;
                    } else if (relationship.getValue()==Relationship.ValueType.VALUE_NO) {
                        verticalNoRelationships.add(relationship);
                    }
                }

                if (verticalNoRelationships.size() == logicProblem.getNumItems()-1) {
                    //search item that does not have a VALUE_NO and set it
                    for (Item item2 : cat2.getItems()){
                        Relationship rel = relationshipTable.get(new ItemPair(item1,item2));
                        if (rel.getValue()!=Relationship.ValueType.VALUE_NO) {
                            //the upstream dependencies are already captured in the verticalNoRelationships ArrayList
                            Processor.setRelationship(logicProblem,applyChanges, rel, 
                                                  Relationship.ValueType.VALUE_YES,
                                                  Relationship.LogicType.UNIQUE, 
                                                  verticalNoRelationships.toArray(new Relationship[verticalNoRelationships.size()]));
//                            rel.setValue(Relationship.ValueType.VALUE_YES, Relationship.LogicType.UNIQUE,verticalNoRelationships.toArray(new Relationship[verticalNoRelationships.size()]));
//                            System.out.println("created VALUE_YES relationship between "+item1.getName()+" and "+item2.getName()+ " (UNIQUE)");
//                            Notifications.create()
//                                .title("New relationship discovered!")
//                                .text("Uniqueness!")
//                                .hideAfter(new Duration(2000))
//                                .show();
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
                        horizontalNoRelationships.clear();
                        break;
                    } else if (relationship.getValue()==Relationship.ValueType.VALUE_NO) {
                        horizontalNoRelationships.add(relationship);
                    }
                }
                if (horizontalNoRelationships.size() == logicProblem.getNumItems()-1) {
//                          //search item that does not have a VALUE_NO and set it
                    for (Item item2 : cat1.getItems()){
                        Relationship rel = relationshipTable.get(new ItemPair(item1,item2));
                        if (rel.getValue()!=Relationship.ValueType.VALUE_NO) {
                            Processor.setRelationship(logicProblem, applyChanges, rel, 
                                                  Relationship.ValueType.VALUE_YES,
                                                  Relationship.LogicType.UNIQUE, 
                                                  horizontalNoRelationships.toArray(new Relationship[horizontalNoRelationships.size()]));                            
//                            rel.setValue(Relationship.ValueType.VALUE_YES, Relationship.LogicType.UNIQUE,horizontalNoRelationships.toArray(new Relationship[horizontalNoRelationships.size()]));
//                            System.out.println("created VALUE_YES relationship between "+item1.getName()+" and "+item2.getName()+ " (UNIQUE)");
                        }
                    }
                }
            }
        }
    }
    
    public static void commonality(LogicProblem logicProblem, boolean applyChanges) 
            throws RelationshipConflictException {
//        System.out.println("commonality invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        
        HashSet<CategoryPair> categoryPairs = logicProblem.getCategoryPairs();
        
//        System.out.println("\n\n\n");
        for (CategoryPair categoryPair : categoryPairs) {
            Category cat1 = categoryPair.first();
            Category cat2 = categoryPair.last();
            
            
//            System.out.println("assessing "+cat1.getName()+" vs "+cat2.getName());
            
            //"vertical search"
            for (Item item1 : cat1.getItems()){
                
                ArrayList<Item> candidateList = new ArrayList<Item>();
                ArrayList<Dependable> predecessors1 = new ArrayList<Dependable>();
                
                //for a given item and item set in a category pair, stack all 
                //unknow values and mark them as candidates
                //then try to find common VALUE_NO for each of the candidates
                
                for (Item item2 : cat2.getItems()){
                    Relationship sourceRelationship = relationshipTable.get(new ItemPair(item1,item2));
                    if (sourceRelationship.getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
                        candidateList.add(item2);
                        predecessors1.add(sourceRelationship);
                    }
                }
//                System.out.println("\t\tfor item " + item1 + " -> candidate size: "+candidateList.size());
                
                //count the stack
                //stacks of size - 1 are tackled through uniqueness
                //stacks of 1 would be similarly tackled if another category had n-1 VALUE_NO from which a relationship 
                //could be inferred
                //the next consideration is we want to avoid double searching
                //so we promote the stacks that are less than half on the basis
                //(the algorithm would find both otherwise)
                if (candidateList.size()>1 && candidateList.size()<=cat2.getNumItems()/2) {
//                if (candidateList.size()>1 && candidateList.size()<cat2.getNumItems()-1) {
//                    System.out.println("found commonality candidate "+cat1.getName()+","+item1.getName()+" vs "+cat2.getName());
                    for (Category catSearch: logicProblem.getCategoriesList()) {
                        //looking to interpolate in another region
                        //away from the intersection under investigation (aka stack)
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
                                
                               //the search list will contain only those relationships that have VALUE_NO
                               //matching ALL the VALUE_UNKNOWN from the search list
                                if (searchList.size()==candidateList.size()) {
                                    //System.out.println("discovered new commonalisty for " + cat1.getName()+","+item1.getName()+" vs "+cat2.getName() + " at "+catSearch.getName()+","+itemSearch.getName());
                                    predecessors1.addAll(predecessors2); //merge predecessors
                                    Processor.setRelationship(logicProblem, applyChanges, 
                                            relationshipTable.get(new ItemPair(item1,itemSearch)), 
                                            Relationship.ValueType.VALUE_NO, Relationship.LogicType.COMMON,
                                            predecessors1.toArray(new Relationship[predecessors1.size()]));
//                                    relationshipTable.get(new ItemPair(item1,itemSearch)).setValue(Relationship.ValueType.VALUE_NO, Relationship.LogicType.COMMON,predecessors1.toArray(new Relationship[predecessors1.size()]));
                                }
                            }
                        }
                    }
                }
            }
            //"horizontal search"
            for (Item item1 : cat2.getItems()){
                
                ArrayList<Item> candidateList = new ArrayList<Item>();
                ArrayList<Dependable> predecessors1 = new ArrayList<Dependable>();
                
                //for a given item and item set in a category pair, stack all 
                //unknow values and mark them as candidates
                //then try to find common VALUE_NO for each of the candidates
                
                for (Item item2 : cat1.getItems()){
                    Relationship sourceRelationship = relationshipTable.get(new ItemPair(item1,item2));
                    if (sourceRelationship.getValue()==Relationship.ValueType.VALUE_UNKNOWN) {
                        candidateList.add(item2);
                        predecessors1.add(sourceRelationship);
                    }
                }
//                System.out.println("\t\tfor item " + item1 + " -> candidate size: "+candidateList.size());
                
                //count the stack
                //stacks of size - 1 are tackled through uniqueness
                //stacks of 1 would be similarly tackled if another category had n-1 VALUE_NO from which a relationship 
                //could be inferred
                //the next consideration is we want to avoid double searching
                //so we promote the stacks that are less than half on the basis
                //(the algorithm would find both otherwise)
                if (candidateList.size()>1 && candidateList.size()<=cat2.getNumItems()/2) {
//                if (candidateList.size()>1 && candidateList.size()<cat2.getNumItems()-1) {
//                    System.out.println("found commonality candidate "+cat1.getName()+","+item1.getName()+" vs "+cat2.getName());
                    for (Category catSearch: logicProblem.getCategoriesList()) {
                        //looking to interpolate in another region
                        //away from the intersection under investigation (aka stack)
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
                                
                               //the search list will contain only those relationships that have VALUE_NO
                               //matching ALL the VALUE_UNKNOWN from the search list
                                if (searchList.size()==candidateList.size()) {
                                    //System.out.println("discovered new commonalisty for " + cat1.getName()+","+item1.getName()+" vs "+cat2.getName() + " at "+catSearch.getName()+","+itemSearch.getName());
                                    predecessors1.addAll(predecessors2); //merge predecessors
                                    Processor.setRelationship(logicProblem, applyChanges, 
                                            relationshipTable.get(new ItemPair(item1,itemSearch)), 
                                            Relationship.ValueType.VALUE_NO, Relationship.LogicType.COMMON,
                                            predecessors1.toArray(new Relationship[predecessors1.size()]));
//                                    relationshipTable.get(new ItemPair(item1,itemSearch)).setValue(Relationship.ValueType.VALUE_NO, Relationship.LogicType.COMMON,predecessors1.toArray(new Relationship[predecessors1.size()]));
                                }
                            }
                        }
                    }
                }
            }

        }
    }
    
    private static void setRelationship(LogicProblem logicProblem,
                                        boolean applyChanges, 
                                        Relationship rel,
                                        Relationship.ValueType value,
                                        Relationship.LogicType logic,
                                        Dependable ... arg_predecessors) 
            throws RelationshipConflictException {
        
        rel.setValue(value,logic, applyChanges, arg_predecessors);
        System.out.println("created "+value+" relationship between "+rel.getItemPair().first()+" and "+rel.getItemPair().last()+ " ("+logic+")");
    }
}
