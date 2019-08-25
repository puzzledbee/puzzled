/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.processor;

import java.util.HashMap;
import java.util.HashSet;
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
//        Processor.process(this.logicProblem, this.controller);
        HashMap<ItemPair,Relationship> relationshipTable = this.logicProblem.getRelationshipTable();
        HashSet<CategoryPair> categoryPairs = this.logicProblem.getCategoryPairs();
        IntegerProperty newlyDiscoveredRelationshipsProperty = new SimpleIntegerProperty(0); 
            
//        System.out.println(categoryPairs);
        
        if (!categoryPairs.isEmpty()) {
            CategoryPair categoryPair = categoryPairs.iterator().next();
            Category cat1 = categoryPair.first();
            Category cat2 = categoryPair.last();
            
            Item item1 = cat1.getItems().get(0);
            Item item2 = cat2.getItems().get(0);
            ItemPair itemPair = new ItemPair(item1,item2);
            Relationship relationship = relationshipTable.get(itemPair);
            relationship.setValue(Relationship.ValueType.VALUE_YES, Relationship.LogicType.CONSTRAINT, true,
                    new Constraint(itemPair,Relationship.ValueType.VALUE_YES));
            
            //LogicProblem logicProblem, IntegerProperty newlyDiscoveredRelationshipsProperty, 
            //boolean automaticProcessing, boolean applyChanges
            Processor.cross(logicProblem,true);
            
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
        } else fail("The HashSet of CategoryPairs is empty");
    }
    
    
    
    
    @Test
    public void testPseudoTrueTuples() throws Exception {
        System.out.println("pseudo true tuples");
//        Processor.process(this.logicProblem, this.controller);
        HashMap<ItemPair,Relationship> relationshipTable = this.logicProblem.getRelationshipTable();
        HashSet<CategoryPair> categoryPairs = this.logicProblem.getCategoryPairs();
        IntegerProperty newlyDiscoveredRelationshipsProperty = new SimpleIntegerProperty(0); 
            
//        System.out.println(categoryPairs);
        
        if (!categoryPairs.isEmpty()) {
            CategoryPair categoryPair = categoryPairs.iterator().next();
            Category cat1 = categoryPair.first();
            Category cat2 = categoryPair.last();
            
            Item item1 = cat1.getItems().get(0);
            Item item2 = cat2.getItems().get(0);
            ItemPair itemPair = new ItemPair(item1,item2);
            Relationship relationship = relationshipTable.get(itemPair);
            relationship.setValue(Relationship.ValueType.VALUE_YES, Relationship.LogicType.CONSTRAINT, true,
                    new Constraint(itemPair,Relationship.ValueType.VALUE_YES));
            
            //LogicProblem logicProblem, IntegerProperty newlyDiscoveredRelationshipsProperty, 
            //boolean automaticProcessing, boolean applyChanges
            Processor.pseudoTrueTuples(logicProblem,true);
            
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
        } else fail("The HashSet of CategoryPairs is empty");
    }
    

    @Ignore
    @Test
    public void testTranspose() throws Exception {
        System.out.println("transpose");
        LogicProblem logicProblem = null;
//        Processor.transpose(logicProblem,);
        fail("The test case is a prototype.");
    }

    @Ignore
    @Test
    public void testUniqueness() throws Exception {
        System.out.println("uniqueness");
        LogicProblem logicProblem = null;
//        Processor.uniqueness(logicProblem);
        fail("The test case is a prototype.");
    }

    @Ignore
    @Test
    public void testCommonality() throws Exception {
        System.out.println("commonality");
        LogicProblem logicProblem = null;
//        Processor.commonality(logicProblem);
        fail("The test case is a prototype.");
    }
    
}
