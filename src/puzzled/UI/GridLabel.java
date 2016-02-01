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
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;
import org.controlsfx.control.decoration.Decorator;
import org.controlsfx.control.decoration.GraphicDecoration;
import org.controlsfx.control.decoration.StyleClassDecoration;
import puzzled.data.Category;
import puzzled.data.DataElement;

/**
 *
 * @author phiv
 */
public class GridLabel extends AnchorPane {

    public enum LabelType {
        CATEGORY,
        ITEM
    }

    private DataElement dataElement;
    private Label mainLabel = new Label();
    private ContextMenu contextMenu = new ContextMenu();

    public GridLabel(DataElement dataElement, int width, int height) {
        this.dataElement = dataElement;
        
        mainLabel.textProperty().bindBidirectional(dataElement.nameProperty());
        this.setOnMouseClicked(this.labelDoubleClickHandler);
        this.getStyleClass().add("gridLabel");
        mainLabel.setAlignment(Pos.CENTER);
        this.setPrefWidth(width);
        this.setPrefHeight(height);
        
        this.getChildren().add(mainLabel);
        decorate();
        AnchorPane.setBottomAnchor(mainLabel, 0.0);
        AnchorPane.setTopAnchor(mainLabel, 0.0);
        AnchorPane.setLeftAnchor(mainLabel, 0.0);
        AnchorPane.setRightAnchor(mainLabel, 0.0);
    
//        Decorator.addDecoration(this, new StyleClassDecoration("warning"));
//        Decorator.addDecoration(this, new GraphicDecoration(createDecoratorNode(Color.RED),Pos.TOP_CENTER));
//        Decorator.addDecoration(this, new GraphicDecoration(createImageNode(),Pos.TOP_CENTER));
        
        MenuItem editMenuItem = new MenuItem("Edit "+((dataElement instanceof Category)?"category...":"item..."));
        //        <div>Icon made by <a href="http://www.amitjakhu.com" title="Amit Jakhu">Amit Jakhu</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        editMenuItem.setGraphic(new ImageView("/icons/context-menus/edit.png"));
        editMenuItem.setOnAction(new ContextMenuActionHandler(this));
        contextMenu.getItems().add(editMenuItem);
//        this.setOnMouseClicked(e -> contextMenu.show(this, Side.RIGHT, 0, 0)); 
        if (dataElement instanceof Category) {
            Tooltip newTooltip = new Tooltip();
            newTooltip.textProperty().bind(Bindings.format("category of type %s", ((Category)dataElement).getType()));
            mainLabel.setTooltip(newTooltip);
        }
    }
    
    private String getText() {
        return mainLabel.getText();
    }
    
    private void setText(String text) {
        mainLabel.setText(text);
    }
    
    private void decorate() {
       
        if (dataElement instanceof Category) {
            Label decorator = new Label();
            decorator.setPrefSize(20,20);
            decorator.setMouseTransparent(true);
            
            //binding the colour of the decorator to the CategoryType in order
            //to update the decorator when/if the CategoryType changes
            
            //the first binding solution was hard coded using the "styleProperty"
            //using color strings embedded in the CategoryType enum
            //this unfortunately keeps style information hard coded in java code
//            decorator.styleProperty().bind(Bindings.createStringBinding(() -> 
//                "-fx-shape: \"M0,0 L1,1 L1,0 Z\"; -fx-background-color: "+
//                this.categoryTypeProperty.get().color));

            //the second colour binding solution instead uses the idProperty
            //this has the advantage of capturing the style information in the .css file
            //by combining a style class (unbound), for generic shape, and an ID for the colour
            //Although it is an unorthodox way to use the idProperty
            //(there will be non-unique elements in the scene with the same ID)
            //it is preferred to the first solution as it keeps
            //CSS information from being stored in the java code
            //and simplifies the CategoryType enum
            decorator.getStyleClass().add("decorator"); //generic shape information
            decorator.idProperty().bind(Bindings.createStringBinding(() -> 
                "decorator-" + ((Category)dataElement).getType()));         

            this.getChildren().add(decorator);
            AnchorPane.setTopAnchor(decorator, 0.0);
            AnchorPane.setRightAnchor(decorator, 0.0); 
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
