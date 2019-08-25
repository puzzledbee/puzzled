/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.exceptions;

import java.util.List;
import puzzled.data.Item;
import puzzled.data.ItemPair;
import puzzled.data.Relationship;

/**
 *
 * @author phiv
 */
public class MultipleMatchesException extends Exception {
    List<Item> itemList;
    
    public MultipleMatchesException(List<Item> arg_itemList) {
           this.itemList = arg_itemList;
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        itemList.forEach(item -> b.append(item.getName()+"\n"));
        return "Multiple matches:\n"+ b;
    }
    
}
