/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import puzzled.Puzzled;
/**
 *
 * @author Fred
 */
public class Grid extends HBox {

    Puzzled application;
    
    public Grid(Puzzled myApplication) {
        //super();
        application = myApplication;
        this.setAlignment(Pos.CENTER);
        GridCell cell1 = new GridCell(application);
        GridCell cell2 = new GridCell(application);
        GridCell cell3 = new GridCell(application);

        this.getChildren().addAll(cell1,cell2, cell3);
    }
 
}