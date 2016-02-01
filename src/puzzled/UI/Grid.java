/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import org.controlsfx.control.PopOver;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.GraphicDecoration;
import org.controlsfx.control.decoration.StyleClassDecoration;
import puzzled.Puzzled;
import puzzled.PuzzledController;
import puzzled.data.Category;
import puzzled.data.Item;
import puzzled.data.ItemPair;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;
/**
 *
 * @author phiv
 */
public class Grid extends StackPane {

    //PuzzledController controller;
    public final static int cellwidth = 40;
    public static int labelheight = 175; //for vertical item labels
    public static int labelwidth = 175;  //for horizontal item labels
    private LogicProblem logicProblem;
    
    private int numCategories;
    private int numItems;
    
    private PuzzledController controller;
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    private ContextMenu contextMenu = new ContextMenu();
    
    private PopOver myPopOver = new PopOver(new Label("hello!"));

    public Grid(PuzzledController arg_controller, LogicProblem arg_logicProblem) {
        //super();
        controller = arg_controller;    
        logicProblem = arg_logicProblem;
        this.numCategories = logicProblem.getNumCategories();
        this.numItems = logicProblem.getNumItems();
        //fLogger.log(Level.INFO, hideLabels.getText());
        
        AnchorPane gridPane = drawGrid();
        this.scaleXProperty().bind(logicProblem.scaleProperty());
        this.scaleYProperty().bind(logicProblem.scaleProperty());
        
        MenuItem item1 = new MenuItem("Edit...");
//        item1.setOnAction(labelDoubleClickHandler);
        contextMenu.getItems().add(item1);
        
        
        
        AnchorPane labelPane = labelPane();
        AnchorPane cellsPane = cellsPane();
        
//        HBox hbox = new HBox();
//        hbox.setAlignment(Pos.CENTER);
//        //this.setSpacing(10);
//        //HBox.setMargin(this,new Insets(50,50,50,50));
//          
//        VBox labels = new VBox(25);
//        labels.setAlignment(Pos.CENTER);
//        
//        
//        for (int j = 0; j < 3; j++) {
//            Label myLabel = new Label("test 1 2 3");
//            
//            myLabel.setOnMouseClicked(e->myPopOver.show(myLabel));
//            labels.getChildren().add(myLabel);
//        }      
//        
//
//        TilePane tPane = new TilePane();
//        tPane.setMaxWidth(155);
//        tPane.setAlignment(Pos.CENTER);
//        for (int i = 0; i < 9; i++) {
//            //Relationship rel = new Relationship(new Pair(logicProblem.getCategories().));
//            tPane.getChildren().add(new GridCell(cellwidth));
//        }
//        
//        hbox.getChildren().addAll(labels,tPane);
        StackPane.setMargin(labelPane, new Insets(cellwidth));
        StackPane.setMargin(cellsPane, new Insets(cellwidth));
        
        this.getChildren().addAll(gridPane,labelPane,cellsPane);
//        this.setMaxWidth(21*cellwidth+labelwidth);
//        this.setMaxHeight(21*cellwidth+labelheight);
    }
    
    
    private AnchorPane drawGrid() {
        AnchorPane drawingPane = new AnchorPane();
                //draw the grid
        int gridmaxwidth=(numCategories-1)*numItems*cellwidth;
        int gridmaxheight=(numCategories-1)*numItems*cellwidth;
        //
        //horizontal, top to bottom
        //drawingPane.setBackground(Background.EMPTY);
        //from labels -> labels+gridmaxwidth, blue
        Line nextLine = new Line(cellwidth+labelwidth,0,cellwidth+labelwidth+gridmaxwidth,0);
        nextLine.getStyleClass().add("gridmajor");
        drawingPane.getChildren().add(nextLine);

        //from labels -> labels+gridmaxwidth, blue
        nextLine = new Line(cellwidth+labelwidth,cellwidth,cellwidth+labelwidth+gridmaxwidth,cellwidth);
        nextLine.getStyleClass().add("gridmajor");
        drawingPane.getChildren().add(nextLine);

        //from start -> labels+gridmaxwidth, blue
        nextLine = new Line(0,cellwidth+labelheight,cellwidth+labelwidth+gridmaxwidth,cellwidth+labelheight);
        nextLine.getStyleClass().add("gridmajor");
        drawingPane.getChildren().add(nextLine);
        
        for (int j=1;j<numCategories;j++) {
            for (int i=1;i<numItems;i++) {
             
                nextLine = new Line(cellwidth,cellwidth+labelheight+(j-1)*numItems*cellwidth+i*cellwidth,cellwidth+labelwidth+gridmaxwidth-(j-1)*numItems*cellwidth,cellwidth+labelheight+(j-1)*numItems*cellwidth+i*cellwidth);
                nextLine.getStyleClass().add("gridminor");
                drawingPane.getChildren().add(nextLine);          
            }
            nextLine = new Line(0,cellwidth+labelheight+j*numItems*cellwidth,cellwidth+labelwidth+gridmaxwidth-(j-1)*numItems*cellwidth,cellwidth+labelheight+j*numItems*cellwidth);
            nextLine.getStyleClass().add("gridmajor");
            drawingPane.getChildren().add(nextLine); 
        }
        //vertical, top to bottom
        
        //from labels -> labels+gridmaxwidth, blue
        nextLine = new Line(0,cellwidth+labelheight,0,cellwidth+labelheight+gridmaxheight);
        nextLine.getStyleClass().add("gridmajor");
        drawingPane.getChildren().add(nextLine);

        //from labels -> labels+gridmaxwidth, blue
        nextLine = new Line(cellwidth,cellwidth+labelheight,cellwidth,cellwidth+labelheight+gridmaxheight);
        nextLine.getStyleClass().add("gridmajor");
        drawingPane.getChildren().add(nextLine);

        //from start -> labels+gridmaxwidth, blue
        nextLine = new Line(cellwidth+labelwidth,0,cellwidth+labelwidth,cellwidth+labelheight+gridmaxheight);
        nextLine.getStyleClass().add("gridmajor");
        drawingPane.getChildren().add(nextLine);
        
        for (int j=1;j<numCategories;j++) {
            for (int i=1;i<numItems;i++) {
             
                nextLine = new Line(cellwidth+labelwidth+(j-1)*numItems*cellwidth+(i)*cellwidth,cellwidth,cellwidth+labelwidth+(j-1)*numItems*cellwidth+(i)*cellwidth,cellwidth+labelheight+gridmaxheight-(j-1)*numItems*cellwidth);
                nextLine.getStyleClass().add("gridminor");
                drawingPane.getChildren().add(nextLine);          
            }
            nextLine = new Line(cellwidth+labelwidth+j*numItems*cellwidth,0,cellwidth+labelwidth+j*numItems*cellwidth,cellwidth+labelheight+gridmaxheight-(j-1)*numItems*cellwidth);
            nextLine.getStyleClass().add("gridmajor");
            drawingPane.getChildren().add(nextLine);
            StackPane.setMargin(drawingPane, new Insets(cellwidth));
            
        }

        return drawingPane;
        
    }

    private AnchorPane labelPane() {
        
        AnchorPane anchorPane = new AnchorPane();
    
        List<Category> categories = logicProblem.getCategories();
        
        //left labels
        for (int cat=1;cat<=numCategories;cat++){
            if (cat==2) continue; //second category appears first on the horizontal axis
            //category labels
            AnchorPane myLabel = new GridLabel(categories.get(cat-1),cellwidth*numItems,cellwidth);
            
            AnchorPane.setLeftAnchor(myLabel, cellwidth+0.0);
            AnchorPane.setTopAnchor(myLabel, labelheight+cellwidth*(cat<3?cat:cat-1)*numItems+0.0);   
            //the Rotate object allows you to define a pivot point, and is easier to position than the setRotate method.
            myLabel.getTransforms().add(new Rotate(270, 0, cellwidth));
            anchorPane.getChildren().add(myLabel);
            //item labels
            for (int item=1;item<=numItems;item++){
                myLabel = new GridLabel(categories.get(cat-1).getItems().get(item-1),labelwidth,cellwidth);
                
                AnchorPane.setLeftAnchor(myLabel, cellwidth+0.0);
                AnchorPane.setTopAnchor(myLabel, labelheight+cellwidth*(cat<3?cat-1:cat-2)*numItems+cellwidth*item+0.0);
                anchorPane.getChildren().add(myLabel);
            }
        }
        


        //top labels
        for (int cat=1;cat<numCategories;cat++){
            
            //category labels
            AnchorPane myLabel = new GridLabel(categories.get((cat==1?1:numCategories-cat+1)),cellwidth*numItems,cellwidth);
            AnchorPane.setLeftAnchor(myLabel, cellwidth+labelwidth+cellwidth*numItems*(cat-1)+0.0);
            AnchorPane.setTopAnchor(myLabel, 0.0);   

            anchorPane.getChildren().add(myLabel);
            //item labels
            for (int item=1;item<=numItems;item++){
                myLabel = new GridLabel(categories.get((cat==1?1:numCategories-cat+1)).getItems().get(item-1),labelheight,cellwidth);
                myLabel.getTransforms().add(new Rotate(270, 0, 0));

                AnchorPane.setLeftAnchor(myLabel, cellwidth+labelwidth+(cellwidth*(cat-1)*numItems)+(cellwidth*(item-1))+0.0);
                AnchorPane.setTopAnchor(myLabel, labelheight+cellwidth+0.0);
                anchorPane.getChildren().add(myLabel);
            }
        }

        return anchorPane;
    }
    
    private AnchorPane cellsPane() {
            AnchorPane cells = new AnchorPane();
            HashMap<ItemPair,Relationship> relationshipTable = logicProblem.getRelationshipTable();
            
            for (ItemPair key : relationshipTable.keySet()){
//                System.out.println("position cell for key "+key.first().getName()+ " <-> "+ key.last().getName());
                GridCell cell = new GridCell(cellwidth, relationshipTable.get(key), logicProblem.dirtyFileProperty());
                cells.getChildren().add(cell);
                Point2D position = positionCell(key);
                AnchorPane.setLeftAnchor(cell, position.getX());
                AnchorPane.setTopAnchor(cell, position.getY());
             }
//            cells.setMouseTransparent(true);
            cells.setPickOnBounds(false);
            return cells;
    }
    
    
    private Point2D positionCell(ItemPair pair) {
         
        Item a = pair.first();
        Item b = pair.last();
        
//        System.out.println("a->"+a.getName()+", catIndex="+a.getCatIndex());
//        System.out.println("b->"+b.getName()+", catIndex="+b.getCatIndex());
        
        
        //if lowest catIndex is 0 (first Category), the relationships will be in the top row of the grid
        //if lowest catIndex is 1 (second Category), the relationships will be in the left most column of the grid
        int x = (a.getCatIndex()==1)?0:(b.getCatIndex()==1)?0:logicProblem.getNumCategories()-b.getCatIndex();
        int y = (a.getCatIndex()==0)?0:(a.getCatIndex()==1)?b.getCatIndex()-1:a.getCatIndex()-1;
//        System.out.println("x="+x);
//        System.out.println("y="+y);
        
        Double xPos = cellwidth+labelwidth+x*cellwidth*logicProblem.getNumItems()+((a.getCatIndex()==1)?a.getItemIndex():b.getItemIndex())*cellwidth+0.0;
        Double yPos = cellwidth+labelheight+y*cellwidth*logicProblem.getNumItems()+((a.getCatIndex()==1)?b.getItemIndex():a.getItemIndex())*cellwidth+0.0;
        return new Point2D(xPos,yPos);
    }
}