/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Fred
 */
public class DemoProblem {

    static LogicProblem demoProblem = new LogicProblem("my_demo_problem",5,5);

    //Penny Pres Original Logic Problems Mar 1999
    public static LogicProblem generateDemoProblem47() {
        List<Item> myItems = new ArrayList<Item>(5);
        
        myItems.addAll(Arrays.asList(new Item("Haley's Comet"), new Item("Into The Fire"), new Item("Never Again"), new Item("Passion Play"), new Item("Starburst")));
        demoProblem.addCategory(new Category("Book",myItems));

        myItems.addAll(Arrays.asList(new Item("Bettany"), new Item("Donovan"), new Item("Elizabeth"), new Item("Frederick"), new Item("Peyton")));
        demoProblem.addCategory(new Category("School",myItems));

        myItems.addAll(Arrays.asList(new Item("15"), new Item("16"), new Item("17"), new Item("18"), new Item("19")));
        Category thirdCat = new Category("Pages",myItems);
        thirdCat.setType(CategoryType.NUMERICAL);
        demoProblem.addCategory(thirdCat);
        
        myItems.addAll(Arrays.asList(new Item("Dr. Brusch"), new Item("Dr. Dentur"), new Item("Dr. Gengivis"), new Item("Dr. Moller"), new Item("Dr. Tartar")));
        demoProblem.addCategory(new Category("Mentor",myItems));

        return demoProblem;
    }
    
    
    //PennyPress Original Logic Problems Special Collector's Edition December 2015
    public static LogicProblem generateDemoProblem33() {
        List<Item> myItems = new ArrayList<Item>(5);
        
        myItems.addAll(Arrays.asList(new Item("2:30"), new Item("3:30"), new Item("4:30"), new Item("5:30"), new Item("6:30")));
        Category firstCat = new Category("Time",myItems);
        firstCat.setType(CategoryType.TIME);
        demoProblem.addCategory(firstCat);

        myItems.addAll(Arrays.asList(new Item("Emilio's Eatery"), new Item("Feeding Frenzy"), new Item("Hungry Hyena"), new Item("Nineteenth Hole"), new Item("Tony's Through")));
        demoProblem.addCategory(new Category("Restaurant",myItems));

        myItems.addAll(Arrays.asList(new Item("Aluminium"), new Item("Copper"), new Item("Gold"), new Item("Silver"), new Item("Zinc")));
        demoProblem.addCategory(new Category("Metal",myItems));

        myItems.addAll(Arrays.asList(new Item("3"), new Item("4"), new Item("5"), new Item("6"), new Item("7")));
        Category fourthCat = new Category("People",myItems);
        fourthCat.setType(CategoryType.NUMERICAL);
        demoProblem.addCategory(fourthCat);
        
        return demoProblem;
    }
    
    
    
    //PennyPress Original Logic Problems Special Collector's Edition December 2015
    public static LogicProblem generateDemoProblem31() {
        List<Item> myItems = new ArrayList<Item>(5);
        
        myItems.addAll(Arrays.asList(new Item("400"), new Item("415"), new Item("430"), new Item("445"), new Item("460")));
        Category firstCat = new Category("Office",myItems);
        firstCat.setType(CategoryType.NUMERICAL);
        demoProblem.addCategory(firstCat);

        myItems.addAll(Arrays.asList(new Item("Cuspidoria"), new Item("Denticus Maximus"), new Item("Rinse & Spit Academy"), new Item("The Tooth School"), new Item("University of Norway")));
        demoProblem.addCategory(new Category("School",myItems));

        myItems.addAll(Arrays.asList(new Item("Dr. Brusch"), new Item("Dr. Dentur"), new Item("Dr. Gengivis"), new Item("Dr. Moller"), new Item("Dr. Tartar")));
        demoProblem.addCategory(new Category("Mentor",myItems));

        myItems.addAll(Arrays.asList(new Item("1998"), new Item("2001"), new Item("2004"), new Item("2007"), new Item("2010")));
        Category fourthCat = new Category("Year",myItems);
        fourthCat.setType(CategoryType.NORMAL);
        demoProblem.addCategory(fourthCat);
        
        return demoProblem;
    }
    
}
