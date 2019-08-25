/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import puzzled.PuzzledFileIO;
import puzzled.data.Category;
import puzzled.data.CategoryPair;
import puzzled.data.Constraint;
import puzzled.data.Item;
import puzzled.data.ItemPair;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class ProcessorTest {
    
    LogicProblem logicProblem;
//    PuzzledController controller;
//    BooleanProperty dirtyLogicProperty = new SimpleBooleanProperty();
    
//    @BeforeClass
//    public static void setUpClass() throws InterruptedException {
//        // Initialise Java FX
//
//        System.out.printf("About to launch FX App\n");
//        Thread t = new Thread("JavaFX Init Thread") {
//            public void run() {
//                Application.launch(Puzzled.class, new String[0]);
//            }
//        };
//        t.setDaemon(true);
//        t.start();
//        System.out.printf("FX App thread started\n");
//        Thread.sleep(500);
//    }
    
    public ProcessorTest() {
        try {
            this.logicProblem = PuzzledFileIO.loadProblem("resources/samples/problem47.lpd");
            this.logicProblem.initializeRelationshipTable();
        } catch (Exception e) {
            System.out.println("unable to load problem file");
        }
    }

    @Test
    public void testCross() throws Exception {
        System.out.println("cross");
        HashMap<ItemPair,Relationship> relationshipTable = this.logicProblem.getRelationshipTable();
        IntegerProperty newlyDiscoveredRelationshipsProperty = new SimpleIntegerProperty(0); 
            
        Item item1 = this.logicProblem.searchItem("Peyton");
        Item item2 = this.logicProblem.searchItem("Haley's Comet");

        Category cat1 = item1.getParentCategory();
        Category cat2 = item2.getParentCategory();
        ItemPair itemPair = new ItemPair(item1,item2);
        Relationship relationship = relationshipTable.get(itemPair);
        relationship.setValue(Relationship.ValueType.VALUE_YES, Relationship.LogicType.CONSTRAINT, true,
                new Constraint(itemPair,Relationship.ValueType.VALUE_YES));

        Processor.cross(logicProblem,true);
        assertTrue("failing at Peyton vs Starburst",
                relationshipTable.get(
                        new ItemPair(logicProblem.searchItem("Peyton"),logicProblem.searchItem("Starburst")))
                                .getValue() == Relationship.ValueType.VALUE_NO);

        for (Item testItem : cat2.getItems()) {
            if (testItem != item2) {
                ItemPair testPair = new ItemPair(item1,testItem);
                assertTrue("failing at "+testPair,relationshipTable.get(testPair).getValue() == Relationship.ValueType.VALUE_NO);
            }
        }

        for (Item testItem : cat1.getItems()) {
            if (testItem != item1) {
                ItemPair testPair = new ItemPair(testItem, item2);
                assertTrue("failing at "+testPair,relationshipTable.get(testPair).getValue() == Relationship.ValueType.VALUE_NO);
            }
        }
    }
    
    
    
    
    
    @Test
    public void testTranspose() throws Exception {
        System.out.println("transpose");
        HashMap<ItemPair,Relationship> relationshipTable = this.logicProblem.getRelationshipTable();
        IntegerProperty newlyDiscoveredRelationshipsProperty = new SimpleIntegerProperty(0); 
        List<ItemPair> pairList = new ArrayList<>();
        
        
        //single VALUE_YES
        Item item1 = this.logicProblem.searchItem("Bettany");
        Item item2 = this.logicProblem.searchItem("Haley's Comet");
        ItemPair itemPair = new ItemPair(item1,item2);
        Relationship relationship = relationshipTable.get(itemPair);
        relationship.setValue(Relationship.ValueType.VALUE_YES, Relationship.LogicType.CONSTRAINT, true,
                new Constraint(itemPair,Relationship.ValueType.VALUE_YES));
    
        
        //threee VALUE_NO
        item1 = this.logicProblem.searchItem("Koenig");
        item2 = this.logicProblem.searchItem("Haley's Comet");
        pairList.add(new ItemPair(item1,item2));
    
        item1 = this.logicProblem.searchItem("Wright");
        item2 = this.logicProblem.searchItem("Haley's Comet");
        pairList.add(new ItemPair(item1,item2));
    
        item1 = this.logicProblem.searchItem("Bettany");
        item2 = this.logicProblem.searchItem("15");
        pairList.add(new ItemPair(item1,item2));
    
        for (ItemPair pair : pairList) {
            Relationship rel = relationshipTable.get(pair);
            rel.setValue(Relationship.ValueType.VALUE_NO, Relationship.LogicType.CONSTRAINT, true,
                    new Constraint(pair,Relationship.ValueType.VALUE_NO));
        }
        
        
        Processor.transpose(logicProblem,true);
        
        assertTrue("failing at Bettany vs Koenig",
                relationshipTable.get(
                        new ItemPair(logicProblem.searchItem("Bettany"),logicProblem.searchItem("Koenig")))
                                .getValue() == Relationship.ValueType.VALUE_NO);
        
        assertTrue("failing at Bettany vs Wright",
                relationshipTable.get(
                        new ItemPair(logicProblem.searchItem("Bettany"),logicProblem.searchItem("Wright")))
                                .getValue() == Relationship.ValueType.VALUE_NO);
        
        assertTrue("failing at Haley's Comet vs 15",
                relationshipTable.get(
                        new ItemPair(logicProblem.searchItem("Haley's Comet"),logicProblem.searchItem("15")))
                                .getValue() == Relationship.ValueType.VALUE_NO);        
    }

    @Test
    public void testUniqueness() throws Exception {
        System.out.println("uniqueness");
    HashMap<ItemPair,Relationship> relationshipTable = this.logicProblem.getRelationshipTable();
        IntegerProperty newlyDiscoveredRelationshipsProperty = new SimpleIntegerProperty(0); 
        List<ItemPair> pairList = new ArrayList<>();
        Item item1;
        Item item2;
        
        item1 = this.logicProblem.searchItem("Bettany");
        item2 = this.logicProblem.searchItem("Haley's Comet");
        pairList.add(new ItemPair(item1,item2));
    
        item1 = this.logicProblem.searchItem("Bettany");
        item2 = this.logicProblem.searchItem("Never Again");
        pairList.add(new ItemPair(item1,item2));
    
        item1 = this.logicProblem.searchItem("Bettany");
        item2 = this.logicProblem.searchItem("Into The Fire");
        pairList.add(new ItemPair(item1,item2));
    
        item1 = this.logicProblem.searchItem("Bettany");
        item2 = this.logicProblem.searchItem("Passion Play");
        pairList.add(new ItemPair(item1,item2));
    
        for (ItemPair itemPair : pairList) {
            Relationship relationship = relationshipTable.get(itemPair);
            relationship.setValue(Relationship.ValueType.VALUE_NO, Relationship.LogicType.CONSTRAINT, true,
                    new Constraint(itemPair,Relationship.ValueType.VALUE_NO));
        }
        
        Processor.uniqueness(logicProblem,true);
        
        assertTrue("failing at Bettany vs Starburst",
                relationshipTable.get(
                        new ItemPair(logicProblem.searchItem("Bettany"),logicProblem.searchItem("Starburst")))
                                .getValue() == Relationship.ValueType.VALUE_YES);
    }

    
    @Test
    public void testCommonality() throws Exception {
        System.out.println("commonality");
        HashMap<ItemPair,Relationship> relationshipTable = this.logicProblem.getRelationshipTable();
        IntegerProperty newlyDiscoveredRelationshipsProperty = new SimpleIntegerProperty(0); 
        List<ItemPair> pairList = new ArrayList<>();
        Item item1;
        Item item2;
        
        item1 = this.logicProblem.searchItem("Bettany");
        item2 = this.logicProblem.searchItem("Haley's Comet");
        pairList.add(new ItemPair(item1,item2));
    
        item1 = this.logicProblem.searchItem("Bettany");
        item2 = this.logicProblem.searchItem("Never Again");
        pairList.add(new ItemPair(item1,item2));
    
        item1 = this.logicProblem.searchItem("Bettany");
        item2 = this.logicProblem.searchItem("Starburst");
        pairList.add(new ItemPair(item1,item2));
    
        item1 = this.logicProblem.searchItem("Chapman");
        item2 = this.logicProblem.searchItem("Into The Fire");
        pairList.add(new ItemPair(item1,item2));
        
        item1 = this.logicProblem.searchItem("Chapman");
        item2 = this.logicProblem.searchItem("Passion Play");
        pairList.add(new ItemPair(item1,item2));
    
        for (ItemPair itemPair : pairList) {
            Relationship relationship = relationshipTable.get(itemPair);
            relationship.setValue(Relationship.ValueType.VALUE_NO, Relationship.LogicType.CONSTRAINT, true,
                    new Constraint(itemPair,Relationship.ValueType.VALUE_NO));
        }
        
        Processor.commonality(logicProblem,true);
        
        assertTrue("failing at Bettany vs Chapman",
                relationshipTable.get(
                        new ItemPair(logicProblem.searchItem("Bettany"),logicProblem.searchItem("Chapman")))
                                .getValue() == Relationship.ValueType.VALUE_NO);
    }
    
    @Ignore
    @Test
    public void testPseudoTrueTuples() throws Exception {
        System.out.println("pseudo true tuples");
        HashMap<ItemPair,Relationship> relationshipTable = this.logicProblem.getRelationshipTable();
        IntegerProperty newlyDiscoveredRelationshipsProperty = new SimpleIntegerProperty(0); 
            
        Item item1 = this.logicProblem.searchItem("Peyton");
        Item item2 = this.logicProblem.searchItem("Haley's Comet");

        Category cat1 = item1.getParentCategory();
        Category cat2 = item2.getParentCategory();
        ItemPair itemPair = new ItemPair(item1,item2);
        Relationship relationship = relationshipTable.get(itemPair);
        relationship.setValue(Relationship.ValueType.VALUE_YES, Relationship.LogicType.CONSTRAINT, true,
                new Constraint(itemPair,Relationship.ValueType.VALUE_YES));

        Processor.cross(logicProblem,true);
        assertTrue("failing at Peyton vs Starburst",
                relationshipTable.get(
                        new ItemPair(logicProblem.searchItem("Peyton"),logicProblem.searchItem("Starburst")))
                                .getValue() == Relationship.ValueType.VALUE_NO);


        for (Item testItem : cat2.getItems()) {
            if (testItem != item2) {
                ItemPair testPair = new ItemPair(item1,testItem);
                assertTrue("failing at "+testPair,relationshipTable.get(testPair).getValue() == Relationship.ValueType.VALUE_NO);
            }
        }

        for (Item testItem : cat1.getItems()) {
            if (testItem != item1) {
                ItemPair testPair = new ItemPair(testItem, item2);
                assertTrue("failing at "+testPair,relationshipTable.get(testPair).getValue() == Relationship.ValueType.VALUE_NO);
            }
        }
    }
    
}
