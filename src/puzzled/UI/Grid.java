/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.stage.StageStyle;
import org.controlsfx.control.PopOver;
import puzzled.Puzzled;
import puzzled.PuzzledController;
import puzzled.data.Category;
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
    private LogicProblem logicProblem;
    
    private int numCategories;
    private int numItems;
    
    private PuzzledController controller;
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    
    private PopOver myPopOver = new PopOver(new Label("hello!"));
    
    private EventHandler<MouseEvent> labelDoubleClickHandler = new EventHandler<MouseEvent>() {
            
            private TextInputDialog editDialog = new TextInputDialog();
        
            {
              editDialog.initStyle(StageStyle.UTILITY);
              editDialog.setTitle("Change label name");
              editDialog.setHeaderText(null);
              editDialog.setContentText("Please enter the new name:");      
              
            }
            
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){

                        Label sourceLabel = (Label)mouseEvent.getSource();
//                        controller.notify(WarningType.INFO, "Hello World "+ sourceLabel.getText());
                        editDialog.getEditor().setText(sourceLabel.getText());
                        Platform.runLater(() -> {
                            editDialog.getEditor().selectAll();
                            editDialog.getEditor().requestFocus();
                        });
                        
                        Optional<String> result = editDialog.showAndWait();
                        result.ifPresent(name -> sourceLabel.setText(name));
                    }
                }
            }
    };
    
    public Grid(PuzzledController arg_controller, LogicProblem arg_logicProblem) {
        //super();
        controller = arg_controller;    
        logicProblem = arg_logicProblem;
        this.numCategories = logicProblem.getNumCategories();
        this.numItems = logicProblem.getNumItems();
        //fLogger.log(Level.INFO, hideLabels.getText());
        
        AnchorPane gridPane = drawGrid();
        this.scaleXProperty().bind(logicProblem.getScaleProperty());
        this.scaleYProperty().bind(logicProblem.getScaleProperty());
        
        AnchorPane labelPane = labelPane();
        
        
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
        this.getChildren().addAll(gridPane,labelPane);
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
        
        //vertical labels
        for (int cat=1;cat<=numCategories;cat++){
            if (cat==2) continue; //second category appears first on the horizontal axis
            //category labels
            Label myLabel = new Label();
            myLabel.setOnMouseClicked(labelDoubleClickHandler);
//            myLabel.textProperty().bind(categories.get(cat-1).nameProperty());
            myLabel.textProperty().bindBidirectional(categories.get(cat-1).nameProperty());
            myLabel.setPrefWidth(cellwidth*numItems);
            myLabel.setPrefHeight(cellwidth);
            AnchorPane.setLeftAnchor(myLabel, cellwidth+0.0);
            AnchorPane.setTopAnchor(myLabel, labelheight+cellwidth*(cat<3?cat:cat-1)*numItems+0.0);   
            
            myLabel.setAlignment(Pos.CENTER);
            //the Rotate object allows you to define a pivot point, and is easier to position than the setRotate method.
            myLabel.getTransforms().add(new Rotate(270, 0, cellwidth));
            //myLabel.setRotate(270);
            myLabel.getStyleClass().add("gridLabel");
            anchorPane.getChildren().add(myLabel);
            //item labels
            for (int item=1;item<=numItems;item++){
                myLabel = new Label();
                myLabel.setOnMouseClicked(labelDoubleClickHandler);
                myLabel.textProperty().bindBidirectional(categories.get(cat-1).getItems().get(item-1).nameProperty());
                myLabel.setPrefWidth(labelwidth);
                myLabel.setPrefHeight(cellwidth);              
                myLabel.getStyleClass().add("gridLabel");
                AnchorPane.setLeftAnchor(myLabel, cellwidth+0.0);
                AnchorPane.setTopAnchor(myLabel, labelheight+cellwidth*(cat<3?cat-1:cat-2)*numItems+cellwidth*item+0.0);
                anchorPane.getChildren().add(myLabel);
            }
        }

        //horizontal labels
        for (int cat=1;cat<numCategories;cat++){
            
            //category labels
            Label myLabel = new Label();
            myLabel.setOnMouseClicked(labelDoubleClickHandler);
            myLabel.textProperty().bindBidirectional(categories.get((cat==1?1:numCategories-cat+1)).nameProperty());
            myLabel.setPrefWidth(cellwidth*numItems);
            myLabel.setPrefHeight(cellwidth);
            AnchorPane.setLeftAnchor(myLabel, cellwidth+labelwidth+cellwidth*numItems*(cat-1)+0.0);
            AnchorPane.setTopAnchor(myLabel, 0.0);   

            myLabel.setAlignment(Pos.CENTER);
            myLabel.getStyleClass().add("gridLabel");
            anchorPane.getChildren().add(myLabel);
            //item labels
            for (int item=1;item<=numItems;item++){
                myLabel = new Label();
                myLabel.setOnMouseClicked(labelDoubleClickHandler);
                myLabel.textProperty().bindBidirectional(categories.get((cat==1?1:numCategories-cat)).getItems().get(item-1).nameProperty());
                myLabel.setPrefWidth(labelheight);
                myLabel.setPrefHeight(cellwidth);
                myLabel.getStyleClass().add("gridLabel");
                //the Rotate object allows you to define a pivot point, and is easier to position than the setRotate method.
                myLabel.getTransforms().add(new Rotate(270, 0, 0));

                AnchorPane.setLeftAnchor(myLabel, cellwidth+labelwidth+(cellwidth*(cat-1)*numItems)+(cellwidth*(item-1))+0.0);
                AnchorPane.setTopAnchor(myLabel, labelheight+cellwidth+0.0);
                anchorPane.getChildren().add(myLabel);

            }
        }

        return anchorPane;
    }
}