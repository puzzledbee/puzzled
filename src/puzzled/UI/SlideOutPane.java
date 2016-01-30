/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Side;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import org.controlsfx.control.HiddenSidesPane;

/**
 *
 * @author Sonia-Fred
 */
public class SlideOutPane extends AnchorPane {
    private Label textLabel = new Label();
    private Label tackLabel= new Label();
    private Label titleLabel = new Label("Problem text:");
    //private HiddenSidesPane hsPane;
    
    public SlideOutPane(HiddenSidesPane hsPane) {
        this.getStyleClass().add("slideout");
        this.setPrefSize(300, 200);
//        pinLabel.setPrefSize(200, 200);
        //this.hsPane = hsPane;
 
        
        textLabel.setTextAlignment(TextAlignment.JUSTIFY);
        textLabel.setWrapText(true);
        textLabel.setStyle("-fx-font-size: 10pt;");
        
        titleLabel.setStyle("-fx-underline:true;-fx-font-size: 14pt;");
        
        tackLabel.setGraphic(new ImageView("/icons/slideout-pane/thumbtack.png"));
        
        this.setOnMouseClicked(e -> {
            hsPane.setPinnedSide((hsPane.getPinnedSide() != null)?null:Side.RIGHT);
                tackLabel.setGraphic(new ImageView((hsPane.getPinnedSide() != null)?
                        "/icons/slideout-pane/thumbtack-pushed.png":"/icons/slideout-pane/thumbtack.png"));
                });
        
        
        this.getChildren().addAll(textLabel,tackLabel, titleLabel);
        
        AnchorPane.setBottomAnchor(textLabel, 0.0);
        AnchorPane.setRightAnchor(textLabel, 0.0);
        AnchorPane.setTopAnchor(textLabel, 35.0);
        AnchorPane.setLeftAnchor(textLabel, 0.0);
        AnchorPane.setTopAnchor(tackLabel, 5.0);
        AnchorPane.setLeftAnchor(tackLabel, 5.0);
        AnchorPane.setTopAnchor(titleLabel, 5.0);
        AnchorPane.setLeftAnchor(titleLabel, 40.0);
    }
    
    public StringProperty textProperty() {
        return textLabel.textProperty();
    }
    
}
