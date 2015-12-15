/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import puzzled.Puzzled;
import puzzled.data.Relationship.ValueType;

/**
 *
 * @author Fred
 */
public class GridCell extends StackPane {

    private ObjectProperty<ValueType> valueProperty = new SimpleObjectProperty<>(this, "value" , ValueType.VALUE_UNKNOWN);
    
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    //keep this only until

    private ContextMenu contextMenu = new ContextMenu();
    //private Pane xPane = new Pane();


    public GridCell(int cellwidth) {
        Rectangle myRectangle = new Rectangle(cellwidth, cellwidth, Color.TRANSPARENT);
    
        Circle circle = new Circle((float)cellwidth*2/5,Color.TRANSPARENT);
        Line line1 = new Line(5,5,cellwidth -5 ,cellwidth -5 );
        Line line2 = new Line(cellwidth -5 ,5,5,cellwidth -5 );    
        
        
        valueProperty.addListener( (e,oldValue,newValue) -> fLogger.info("GridCell valueProperty chaged to" + newValue));
        
        myRectangle.setStroke(Color.BLACK);
        
        circle.setMouseTransparent(true);
        circle.getStyleClass().add("o");
        //circle.relocate(5, 5);
        
        circle.visibleProperty().bind(valueProperty.isEqualTo(ValueType.VALUE_YES));
        
        
        //line1.setMouseTransparent(true);
        line1.getStyleClass().add("x");
        line1.setMouseTransparent(true);
        line2.getStyleClass().add("x");
        line2.setMouseTransparent(true);
        //xPane.getChildren().addAll(line1,line2);
        line1.visibleProperty().bind(valueProperty.isEqualTo(ValueType.VALUE_NO));
        line2.visibleProperty().bind(valueProperty.isEqualTo(ValueType.VALUE_NO));
        
        MenuItem item1 = new MenuItem("Set as FALSE");
        item1.disableProperty().bind(valueProperty.isNotEqualTo(ValueType.VALUE_UNKNOWN));
        item1.setOnAction(e -> this.setFalse());
        //needs to add this to constraint table

        MenuItem item2 = new MenuItem("Set as TRUE");
        item2.disableProperty().bind(valueProperty.isNotEqualTo(ValueType.VALUE_UNKNOWN));
        item2.setOnAction(e -> this.setTrue());
        //needs to add this to constraint table
        
        MenuItem item3 = new MenuItem("Clear");
        item3.setOnAction(e -> this.reset());
        
        contextMenu.getItems().addAll(item1,item2, item3);
        
        myRectangle.setOnMouseClicked(e -> contextMenu.show(myRectangle, Side.RIGHT, 0, 0));
    
        this.getChildren().addAll(myRectangle,circle,line1,line2);
    }

    
    public ObjectProperty<ValueType> valueProperty() {
        return this.valueProperty;
    }
    
    public ValueType getValue(){
        return this.valueProperty.get();
    }
    
    public void setFalse() {
        this.valueProperty.set(ValueType.VALUE_NO);
        fLogger.info("setting FALSE");
    }
     
    
    public void setTrue() {
        this.valueProperty.set(ValueType.VALUE_YES);
        fLogger.info("setting TRUE");
    }
 
    public void reset() {
        this.valueProperty.set(ValueType.VALUE_UNKNOWN);
        fLogger.info("setting UNKNOWN");
    }
  
}
