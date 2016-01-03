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
 * @author Fred
 */
public class ItemPair extends TreeSet<Item>{
    
    
    public ItemPair(Item item1, Item item2) {
                super(Comparator.comparing(p1 -> p1.getCatIndex()));
                this.add(item1);
                this.add(item2);
    }
        
}
