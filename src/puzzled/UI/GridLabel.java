/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.Optional;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.StageStyle;
import puzzled.data.Category;

/**
 *
 * @author Fred
 */
public class GridLabel extends Label {

    public enum LabelType {
        CATEGORY,
        ITEM
    }
    
    private LabelType labelType;
    
    private ContextMenu contextMenu = new ContextMenu();  
    
    public GridLabel(LabelType labelType_arg, StringProperty bound, int width, int height) {
        this(labelType_arg, bound, width, height, null);
    }

    //overloaded
    public GridLabel(LabelType labelType_arg, StringProperty bound, int width, int height, ObjectProperty<Category.CategoryType> arg_typeProperty) {
        this.labelType = labelType_arg;
        this.textProperty().bindBidirectional(bound);
        this.setOnMouseClicked(this.labelDoubleClickHandler);
        this.getStyleClass().add("gridLabel");
        this.setAlignment(Pos.CENTER);
        this.setPrefWidth(width);
        this.setPrefHeight(height);

        
        MenuItem editMenuItem = new MenuItem("Edit "+((labelType==LabelType.CATEGORY)?"category...":"item..."));
        //        <div>Icon made by <a href="http://www.amitjakhu.com" title="Amit Jakhu">Amit Jakhu</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        editMenuItem.setGraphic(new ImageView("/icons/context-menus/edit.png"));
        editMenuItem.setOnAction(new ContextMenuActionHandler(this));
        contextMenu.getItems().add(editMenuItem);
//        this.setOnMouseClicked(e -> contextMenu.show(this, Side.RIGHT, 0, 0)); 
        if (labelType==LabelType.CATEGORY) {
            Tooltip newTooltip = new Tooltip();
            newTooltip.textProperty().bind(Bindings.format("category of type %s", arg_typeProperty.asString()));
            this.setTooltip(newTooltip);
        }
    }
    

    private EventHandler<MouseEvent> labelDoubleClickHandler = new EventHandler<MouseEvent>() {
            
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && mouseEvent.getClickCount() == 2){

                    Label sourceLabel = (Label)mouseEvent.getSource();
//                        controller.notify(WarningType.INFO, "Hello World "+ sourceLabel.getText());
    
                    EditDialog editDialog = new EditDialog(sourceLabel.getText());
                    Platform.runLater(() -> {
                        editDialog.getEditor().selectAll();
                        editDialog.getEditor().requestFocus();
                    });

                    Optional<String> result = editDialog.showAndWait();
                    result.ifPresent(name -> sourceLabel.setText(name));
                } else {
                    contextMenu.show((Node) mouseEvent.getTarget(), Side.RIGHT, 0, 0);
                }
            }
    };
    
    class ContextMenuActionHandler implements EventHandler<ActionEvent> {
        
        private GridLabel refLabel;
        
        public ContextMenuActionHandler(GridLabel refLabel) {
            this.refLabel = refLabel;
        }
        
        @Override
        public void handle(ActionEvent actionEvent) {

//                controller.notify(WarningType.INFO, "Hello World "+ sourceLabel.getText());

                EditDialog editDialog = new EditDialog(refLabel.getText());
                Platform.runLater(() -> {
                    editDialog.getEditor().selectAll();
                    editDialog.getEditor().requestFocus();
                });

                Optional<String> result = editDialog.showAndWait();
                result.ifPresent(name -> refLabel.setText(name));
        }
    }
    
    class EditDialog extends TextInputDialog {
        
        public EditDialog(String inputText) {
              this.initStyle(StageStyle.UTILITY);
              this.setTitle("Change label name");
              this.setHeaderText(null);
              this.setContentText("Please enter the new name:");
              this.getEditor().setText(inputText);
        }
    }
}
