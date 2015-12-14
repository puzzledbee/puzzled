/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import org.controlsfx.control.PopOver;
import puzzled.Puzzled;
import puzzled.data.LogicProblem;
/**
 *
 * @author Fred
 */
public class Grid extends StackPane {

    //PuzzledController controller;
    private static int cellwidth = 40;
    private static int labelheight = 175; //for vertical item labels
    private static int labelwidth = 175;  //for horizontal item labels
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    
    private PopOver myPopOver = new PopOver(new Label("hello!"));
    
    public Grid(LogicProblem logicProblem) {
        //super();
        //controller = myController;
        
        AnchorPane drawingPane = new AnchorPane();
        //drawingPane.setAlignment(Pos.CENTER);
        
        int numCategories = logicProblem.getNumCategories();
        int numItems = logicProblem.getNumItems();
        
        
        //draw the grid
        int gridmaxwidth=(numCategories-1)*numItems*cellwidth;
        int gridmaxheight=(numCategories-1)*numItems*cellwidth;
        //
        //horizontal, top to bottom
        drawingPane.setBackground(Background.EMPTY);
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
        }
        
        
        
        
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        //this.setSpacing(10);
        //HBox.setMargin(this,new Insets(50,50,50,50));
          
        VBox labels = new VBox(25);
        labels.setAlignment(Pos.CENTER);
        
        
        for (int j = 0; j < 3; j++) {
            Label myLabel = new Label("test 1 2 3");
            
            myLabel.setOnMouseClicked(e->myPopOver.show(myLabel));
            labels.getChildren().add(myLabel);
        }      
        

        TilePane tPane = new TilePane();
        tPane.setMaxWidth(155);
        tPane.setAlignment(Pos.CENTER);
        for (int i = 0; i < 9; i++) {
            //Relationship rel = new Relationship(new Pair(logicProblem.getCategories().));
            tPane.getChildren().add(new GridCell(cellwidth));
        }
        
        hbox.getChildren().addAll(labels,tPane);
        StackPane.setMargin(drawingPane, new Insets(cellwidth));
        this.getChildren().addAll(drawingPane,hbox);
    }
 
}