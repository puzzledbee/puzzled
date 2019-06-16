/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class Constraint {
    private Relationship relationship;
    private String notes;
    
    public Constraint(Relationship newRelationship) {
        relationship = newRelationship;
    }
    
    public void annotate(String annotation) {
        notes = annotation;
    }
    
    public String getNotes() {
        return notes;
    }
    
}
