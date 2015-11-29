/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import puzzled.Puzzled;
/**
 *
 * @author Fred
 */
public class Grid extends HBox {

    //PuzzledController controller;
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    public Grid() {
        //super();
        //controller = myController;

        this.setAlignment(Pos.CENTER);
        //this.setSpacing(10);
        //HBox.setMargin(this,new Insets(50,50,50,50));
          
        VBox labels = new VBox(25);
        labels.setAlignment(Pos.CENTER);
        for (int j = 0; j < 3; j++) {
            labels.getChildren().add(new Label("test 1 2 3"));
        }      
        

        
        TilePane tPane = new TilePane();
        tPane.setMaxWidth(155);
        tPane.setAlignment(Pos.CENTER);
        for (int i = 0; i < 9; i++) {
            tPane.getChildren().add(new GridCell());
        }
        
        getChildren().addAll(labels,tPane);
    }
 
}