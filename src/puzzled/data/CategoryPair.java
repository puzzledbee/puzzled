/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author phiv
 */
public class CategoryPair extends TreeSet<Category>{
    public CategoryPair(Category cat1, Category cat2) {
        super(Comparator.comparing(p1 -> p1.getCatIndex())); 
        this.add(cat1);
        this.add(cat2);
    }
    
    @Override
    public String toString() {
        return this.first().toString() + " and " + this.last().toString();
    }
    
    
    
    
}
