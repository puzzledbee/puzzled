/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import puzzled.Puzzled;
/**
 *
 * @author Fred
 */
public class Grid extends VBox {

    Puzzled application;
    
    public Grid(Puzzled myApplication) {
        //super();
        application = myApplication;
        this.setAlignment(Pos.CENTER);
        
        HBox row1 = new HBox();
        row1.setAlignment(Pos.CENTER);
        
        GridCell cell1 = new GridCell(application);
        GridCell cell2 = new GridCell(application);
        GridCell cell3 = new GridCell(application);
        
        row1.getChildren().addAll(cell1,cell2, cell3);
        
        HBox row2 = new HBox();
        row2.setAlignment(Pos.CENTER);
        
        GridCell cell4 = new GridCell(application);
        GridCell cell5 = new GridCell(application);
        GridCell cell6 = new GridCell(application);

        row2.getChildren().addAll(cell4,cell5, cell6);
        
        HBox row3 = new HBox();
        row3.setAlignment(Pos.CENTER);
        
        GridCell cell7 = new GridCell(application);
        GridCell cell8 = new GridCell(application);
        GridCell cell9 = new GridCell(application);

        row3.getChildren().addAll(cell7,cell8, cell9);
        
        this.getChildren().addAll(row1,row2,row3);
    }
 
}