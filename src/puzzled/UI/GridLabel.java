/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;

/**
 *
 * @author Fred
 */
public class GridLabel extends Label {

    
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
    
    
    
    public GridLabel(StringProperty bound, int width, int height) {
//        super(text);
        this.textProperty().bindBidirectional(bound);
        this.setOnMouseClicked(labelDoubleClickHandler);
        this.getStyleClass().add("gridLabel");
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(width);
        this.setPrefHeight(height);
    }
    
}
