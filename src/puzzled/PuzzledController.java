/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.NotificationPane;



/**
 * FXML Controller class
 *
 * @author https://github.com/bepuzzled
 */
public class PuzzledController {
    
        private static double zoomFactor = 1.3;
        private static double maxZoom = 2.3;
        private static double minZoom = 0.4;
        private static int notificationTimer = 3000;

        public enum WarningType {
            SUCCESS ("success.png"), 
            INFO ("info.png"), 
            WARNING ("fail.png");

            private String imageName;

            WarningType(String image){
                imageName = image;
            }

            public String getImageName() {
                return imageName;
            }
        }
    /**
     * Initializes the controller class.
     */
    @FXML
    private Label label;
    
    @FXML
    private AnchorPane mainGrid;
    
    @FXML
    private NotificationPane nPane;
    
    @FXML
    private BorderPane bPane;
    
    @FXML
    private VBox clueEngineVBox;
    
    @FXML
    private ToolBar toolbar;
   
    private static final Logger fLogger =
    Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private String appTitle;
    private String appVersion;
    private StringProperty appTitleProperty = new SimpleStringProperty();
    
    @FXML
    private void quit(ActionEvent event) {
        //make sure logicProblem is not dirty!
        Platform.exit();
    }
    
    @FXML
    private void zoomInButtonAction(ActionEvent event) {
        
        //if (logicProblem.get().getScale() <= maxZoom) {
        //    logicProblem.get().setScale(logicProblem.get().getScale()*zoomFactor);
        //} else {
            notify(WarningType.WARNING, "Maximum zoom level reached!");
        //}
        //fLogger.log(Level.INFO, "scale:"+logicProblem.get().getScale());
        fLogger.log(Level.INFO, "zooming");
    }
    
    public void notify(WarningType type, String text) {
        nPane.setGraphic(new ImageView(new Image("/icons/notification-pane/"+type.getImageName())));  
        nPane.setText(text);
        nPane.show();
    }
    
    
    public void setupTitleBinding(StringProperty puzzledTitleProperty, String banner, String version) {
        puzzledTitleProperty.bind(this.appTitleProperty);
        appTitle = banner;
        appVersion = version;
        this.appTitleProperty.set(banner+" - "+version);
    }
    
    
    public void initialize() {
        fLogger.log(Level.INFO, "java version:" + System.getProperty("java.version"));
        fLogger.log(Level.INFO, "javafx version:" + System.getProperty("javafx.version"));
        //label.setText("Hello, JavaFX " + javafxVersion + "\nRunning on Java " + javaVersion + ".");
    }

    
}
