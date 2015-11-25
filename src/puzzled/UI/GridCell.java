/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.logging.Level;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import puzzled.Puzzled;

/**
 *
 * @author Fred
 */
public class GridCell extends StackPane {

    Puzzled application;
    Rectangle myRectangle = new Rectangle(50, 50, Color.TRANSPARENT);
    private Circle circle = new Circle(20.0f,Color.TRANSPARENT);
    private Line line1 = new Line(5,5,45,45);
    private Line line2 = new Line(45,5,5,45);
    private ContextMenu contextMenu = new ContextMenu();

    public GridCell(Puzzled myApplication) {
        //super();
        application = myApplication;
        
        myRectangle.setStroke(Color.BLACK);
        circle.setMouseTransparent(true);
        circle.setStroke(Color.BLUE);
        
        line1.setMouseTransparent(true);
        line1.setStroke(Color.RED);
        line2.setMouseTransparent(true);
        line2.setStroke(Color.RED);
        
        MenuItem item1 = new MenuItem("Set as FALSE");
        item1.setOnAction(e -> this.setFalse());

        MenuItem item2 = new MenuItem("Set as TRUE");
        item2.setOnAction(e -> this.setTrue());
        
        MenuItem item3 = new MenuItem("Clear");
        item3.setOnAction(e -> this.clear());
        
        contextMenu.getItems().addAll(item1,item2, item3);
        
        myRectangle.setOnMouseClicked(e -> contextMenu.show(myRectangle, Side.RIGHT, 0, 0));
        clear();
        this.getChildren().addAll(myRectangle,circle,line1,line2);
    }

    
    public void setFalse() {
        circle.setVisible(false);
        line1.setVisible(true);
        line2.setVisible(true);
        application.getLogger().log(Level.INFO, "setting FALSE");
    }
     
    
    public void setTrue() {
        circle.setVisible(true);
        line1.setVisible(false);
        line2.setVisible(false);
        application.getLogger().log(Level.INFO, "setting TRUE");
    }
 


    public void clear() {
        circle.setVisible(false);
        line1.setVisible(false);
        line2.setVisible(false);
        application.getLogger().log(Level.INFO, "setting UNKNOWN");
    }
  
}
