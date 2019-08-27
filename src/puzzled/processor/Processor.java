/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.paukov.combinatorics3.Generator;
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
                Processor.pseudoTrueTuples(logicProblem, applyChanges);
                
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
    
    //this static method resolves the rest of a subrow and rest of subcolumn
    // (within a subgrid)
    //when a relationship between two items is set to VALUE_YES
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
        
        //within a subgrid
        for (CategoryPair categoryPair : categoryPairs) {
            System.out.println("Category pair:" + categoryPair);
            //vertical and horizontal search
            for (final boolean verticalOrientation : new boolean[] { false, true } ) {

                Category subrowCategory = verticalOrientation?categoryPair.first():categoryPair.last();
                Category subcolumnCategory = verticalOrientation?categoryPair.last():categoryPair.first();

                for (Item item1 : subrowCategory.getItems()){
                    ArrayList<Relationship> verticalNoRelationships = new ArrayList<Relationship>();
    //                        System.out.println("counting "+item1.getName()+" with "+cat2.getName());
                    for (Item item2 : subcolumnCategory.getItems()){
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
                        for (Item item2 : subcolumnCategory.getItems()){
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
            }

        }
    }
    
    public static void commonality(LogicProblem logicProblem, boolean applyChanges) 
            throws RelationshipConflictException {
//        System.out.println("commonality invoked");
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        HashSet<CategoryPair> categoryPairs = logicProblem.getCategoryPairs();
        
//        System.out.println("\n\n\n");
        //within a subgrid
        for (CategoryPair categoryPair : categoryPairs) {
            System.out.println("Category pair:" + categoryPair);
            //vertical and horizontal search
            for (final boolean verticalOrientation : new boolean[] { false, true } ) {

                Category subrowCategory = verticalOrientation?categoryPair.first():categoryPair.last();
                Category subcolumnCategory = verticalOrientation?categoryPair.last():categoryPair.first();
                 
            
//            System.out.println("assessing "+cat1.getName()+" vs "+cat2.getName());
            
                //subrow/column
                for (Item item1 : subrowCategory.getItems()){

                    ArrayList<Item> candidateList = new ArrayList<Item>();
                    ArrayList<Dependable> predecessors1 = new ArrayList<Dependable>();

                    //for a given item and item set in a category pair, stack all 
                    //unknow values and mark them as candidates
                    //then try to find common VALUE_NO for each of the candidates

                    for (Item item2 : subcolumnCategory.getItems()){
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
                    if (candidateList.size()>1 && candidateList.size()<=subcolumnCategory.getNumItems()/2) {
    //                if (candidateList.size()>1 && candidateList.size()<cat2.getNumItems()-1) {
    //                    System.out.println("found commonality candidate "+cat1.getName()+","+item1.getName()+" vs "+cat2.getName());
                        for (Category catSearch: logicProblem.getCategoriesList()) {
                            //looking to interpolate in another region
                            //away from the intersection under investigation (aka stack)
                            if (catSearch!=subrowCategory && catSearch!=subcolumnCategory) {
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
    }
    
    
    public static void pseudoTrueTuples(LogicProblem logicProblem, boolean applyChanges)
        throws RelationshipConflictException {
        //inspired from Puzzle Baron's pseudo-true pairs, extended ton-tuples
        
        HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
        HashSet<CategoryPair> categoryPairs = logicProblem.getCategoryPairs();
        
//        System.out.println("searching...");
        //within a subgrid
        for (CategoryPair categoryPair : categoryPairs) {
//            System.out.println("Category pair:" + categoryPair);
            //vertical and horizontal search
            for (final boolean verticalOrientation : new boolean[] { false, true } ) {

                Category subrowCategory = verticalOrientation?categoryPair.first():categoryPair.last();
                Category subcolumnCategory = verticalOrientation?categoryPair.last():categoryPair.first();

                //commenting "vertical search" 
                //(subrows match first category's items
                //  subcolumns match second category's items)

                //i (2 to floor(n/2)) is the number of elements in the combination tuple
                //      i=2 in pseudoTrue pairs
                //(for subgrids of 5x5 items, looking for both pairs and triplets becomes redundant
                for (int i=2; i<=floor(logicProblem.getNumCategories()/2);i++) {

                    //https://github.com/dpaukov/combinatoricslib3/wiki/v3.3.0#1-simple-combinations
                    List<List<Item>> columnCombinations = Generator.combination(subcolumnCategory.getItems())
                        .simple(i) //for pseudo true pairs, i=2
                        .stream()
                        .collect(Collectors.<List<Item>>toList());
                    //for i=2, this would contain 
                    //Bethany, Donovan
                    //Bethany, Elizabeth
                    //...
                    //Frederick, Peyton
//                    columnCombinations.stream().forEach(l -> System.out.println(
//                           (verticalOrientation?"vertical":"horizontal")+ " combo: "+l.toString()));

                    //create the sets
                    //for each combination, start over
                    
                    //columnTuple contains i items with which we will compute set intersections
                    for (List<Item> columnItemTuple : columnCombinations) {
                        //Bethany, Donovan
                        
                        //similarly, valueNoSets contains i sets of VALUE_NO relationships
                        List<Set<Item>> valueNoSets = new ArrayList<>(); //size=i
                        //build a set for each i item of columnTuple
                        for (Item columnItem : columnItemTuple) {
                            //Bethany
                            Set<Item> valueNoSet = new HashSet<>(); //size = n
                            for (Item rowItem : subrowCategory.getItems()) {
                                //Haley's Comet ....
                                if (relationshipTable.get(new ItemPair(rowItem, columnItem)).getValue() == Relationship.ValueType.VALUE_NO)
                                    //stacking all VALUE_NO relationship into vertical set
                                    valueNoSet.add(rowItem); //store into single subcolumn set 
                            }
                            //adding each vertical set into list of sets
                            if (valueNoSet.size() >= logicProblem.getNumItems() - i //for pairs, we need three VALUE_NO in the set
                                && valueNoSet.size() < logicProblem.getNumItems()-1) //uniqueness will take or n-1 cases
                                    valueNoSets.add(valueNoSet); //store single subcolumn into multi-set
                        }
                        //magic!
                        Set<Item> intersectionSet = new HashSet<>(); //avoid NPE when the next call is made
                        if (valueNoSets.size()>=2) //don't compute intersections otherwise
                            intersectionSet = Processor.multiSetIntersection(valueNoSets);

                        if (intersectionSet.size()==logicProblem.getNumItems()-i) {
                            
//                            System.out.println("----> found candidates for pseudo true pairs!");
//                            intersectionSet.stream().forEach(l -> System.out.println("\t" + l.toString()));
//                            System.out.println("\t\tvs.");
//                            columnItemTuple.stream().forEach(l -> System.out.println("\t" + l.toString()));
                            
                            //create predecessor list
                            //we can only add the intersection output as predecessors
                            ArrayList<Dependable> predecessors = new ArrayList<Dependable>();
                            for (Item intersectionColumn : intersectionSet) {
                                for (Item rowItem : columnItemTuple) {
                                    predecessors.add(relationshipTable.get(new ItemPair(rowItem, intersectionColumn)));
                                }
                            }


                            //we have a bunch of work to do
                            for (Item everyRowItem : subrowCategory.getItems()) {
                                //for every row item not in the intersectionSet
                                if (!intersectionSet.contains(everyRowItem)) {
                                    for (Item everyColumnItem : subcolumnCategory.getItems()) {
                                        //for every column item not in any of the columnTuple list
                                        if (!columnItemTuple.contains(everyColumnItem)) {
                                            Relationship rel = relationshipTable.get(new ItemPair(everyRowItem,everyColumnItem));
                                            if (rel.getValue()!=Relationship.ValueType.VALUE_NO)
                                                Processor.setRelationship(logicProblem, applyChanges, rel, 
                                                    Relationship.ValueType.VALUE_NO, Relationship.LogicType.PSEUDOTRUE,
                                                    predecessors.toArray(new Relationship[predecessors.size()]));                                        
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
    
    private static void setRelationship(LogicProblem logicProblem,
                                        boolean applyChanges, 
                                        Relationship rel,
                                        Relationship.ValueType value,
                                        Relationship.LogicType logic,
                                        Dependable ... arg_predecessors) 
            throws RelationshipConflictException {
        System.out.println("creating "+value+" relationship between "+rel.getItemPair().first()+" and "+rel.getItemPair().last()+ " ("+logic+") ...");
        rel.setValue(value,logic, applyChanges, arg_predecessors);
        
    }
    
    private static Set<Item> multiSetIntersection(List<Set<Item>> setList) {
        // create a deep copy of one (in case you don't wish to modify it)
        Set<Item> interQrels = new HashSet<>(setList.get(0));
        
        for (int i = 1; i< setList.size();i++) {
            interQrels.retainAll(setList.get(i));     // intersection with two (and one)
        }

        return interQrels;
    }
       
}
