/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import puzzled.data.Category.CategoryType;

/**
 *
 * @author Fred
 */
public class DemoProblems {

    static LogicProblem demoProblem;
    
    public static LogicProblem generateDemoProblem0() {
       demoProblem = new LogicProblem("Problem 0");
       demoProblem.setSource("Penny Pres Original Logic Problems Mar 1999");
        
        List<Item> myItems = new ArrayList<Item>(3);
        
        myItems.addAll(Arrays.asList(new Item("Haley's Comet"), new Item("Into The Fire"), new Item("Never Again")));
        demoProblem.addCategory(new Category(myItems,"Book"));
        
        myItems = new ArrayList<Item>(3);
        myItems.addAll(Arrays.asList(new Item("Bettany"), new Item("Donovan"), new Item("Elizabeth")));
        demoProblem.addCategory(new Category(myItems,"School"));

        myItems = new ArrayList<Item>(3);
        myItems.addAll(Arrays.asList(new Item("15"), new Item("16"), new Item("17")));
        Category thirdCat = new Category(myItems,"Pages");
        thirdCat.setType(CategoryType.NUMERICAL);
        demoProblem.addCategory(thirdCat);
        
        return demoProblem;
    }
    
    public static LogicProblem generateDemoProblem47() {
       demoProblem = new LogicProblem("Problem 47");
       demoProblem.setSource("Penny Pres Original Logic Problems Mar 1999");
        
        List<Item> myItems = new ArrayList<Item>(5);
        
        myItems.addAll(Arrays.asList(new Item("Haley's Comet"), new Item("Into The Fire"), new Item("Never Again"), new Item("Passion Play"), new Item("Starburst")));
        demoProblem.addCategory(new Category(myItems,"Book"));

        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("Bettany"), new Item("Donovan"), new Item("Elizabeth"), new Item("Frederick"), new Item("Peyton")));
        demoProblem.addCategory(new Category(myItems,"Author"));

        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("15"), new Item("16"), new Item("17"), new Item("18"), new Item("19")));
        Category thirdCat = new Category(myItems,"Pages");
        thirdCat.setType(CategoryType.NUMERICAL);
        demoProblem.addCategory(thirdCat);

        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("Biography"), new Item("Fantasy"), new Item("Mystery"), new Item("Romance"), new Item("Science-Fiction")));
        demoProblem.addCategory(new Category(myItems,"Genre"));
        
        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("Chapman"), new Item("Holden"), new Item("Koenig"), new Item("Rawlins"), new Item("Wright")));
        demoProblem.addCategory(new Category(myItems,"Last Name"));

        return demoProblem;
    }

        
    public static LogicProblem generateDemoProblem31() {
        demoProblem = new LogicProblem("Problem 31");
        demoProblem.setSource("PennyPress Original Logic Problems Special Collector's Edition December 2015");
        List<Item> myItems = new ArrayList<Item>(5);
        
        myItems.addAll(Arrays.asList(new Item("400"), new Item("415"), new Item("430"), new Item("445"), new Item("460")));
        Category firstCat = new Category(myItems,"Office");
        firstCat.setType(CategoryType.NUMERICAL);
        demoProblem.addCategory(firstCat);
        
        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("Cuspidoria"), new Item("Denticus Maximus"), new Item("Rinse & Spit Academy"), new Item("The Tooth School"), new Item("University of Norway")));
        demoProblem.addCategory(new Category(myItems,"School"));

        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("Dr. Brusch"), new Item("Dr. Dentur"), new Item("Dr. Gengivis"), new Item("Dr. Moller"), new Item("Dr. Tartar")));
        demoProblem.addCategory(new Category(myItems,"Mentor"));

        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("1998"), new Item("2001"), new Item("2004"), new Item("2007"), new Item("2010")));
        Category fourthCat = new Category(myItems,"Year");
        fourthCat.setType(CategoryType.NUMERICAL);
        demoProblem.addCategory(fourthCat);
        
        return demoProblem;
    }
    
    
    public static LogicProblem generateDemoProblem33() {
        demoProblem = new LogicProblem("Problem 33");
        demoProblem.setSource("PennyPress Original Logic Problems Special Collector's Edition December 2015");
        List<Item> myItems = new ArrayList<Item>(5);
        
        myItems.addAll(Arrays.asList(new Item("2:30"), new Item("3:30"), new Item("4:30"), new Item("5:30"), new Item("6:30")));
        Category firstCat = new Category(myItems,"Time");
        firstCat.setType(CategoryType.TIME);
        demoProblem.addCategory(firstCat);
        
        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("Emilio's Eatery"), new Item("Feeding Frenzy"), new Item("Hungry Hyena"), new Item("Nineteenth Hole"), new Item("Tony's Through")));
        demoProblem.addCategory(new Category(myItems,"Restaurant"));

        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("Aluminium"), new Item("Copper"), new Item("Gold"), new Item("Silver"), new Item("Zinc")));
        demoProblem.addCategory(new Category(myItems,"Metal"));

        myItems = new ArrayList<Item>(5);
        myItems.addAll(Arrays.asList(new Item("3"), new Item("4"), new Item("5"), new Item("6"), new Item("7")));
        Category fourthCat = new Category(myItems,"People");
        fourthCat.setType(CategoryType.NUMERICAL);
        demoProblem.addCategory(fourthCat);
        
        return demoProblem;
    }
 
}
