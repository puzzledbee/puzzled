/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.controlsfx.control.NotificationPane;
import puzzled.UI.Grid;
import puzzled.data.Clue;
import puzzled.data.Item;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;

/**
 *
 * @author Fred
 */
public class PuzzledController implements Initializable {
    
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

    @FXML
    Parent root;
    
    @FXML
    Parent bPane;
    
    @FXML
    NotificationPane nPane;
    
    
    @FXML
    private TextField clueText;

    @FXML
    private AnchorPane mainGrid;

    @FXML
    private StackPane mainStack;
    
    @FXML
    private ScrollPane mainScroll;

    @FXML
    private Group mainGroup;
    
    @FXML
    private Label clueCounter;

    @FXML
    private CheckMenuItem hideLabelsMenuItem;
    @FXML
    private CheckMenuItem hideRelationshipsMenuItem;
    
    private LogicProblem logicProblem;
    private HashMap<Pair<Item,Item>,Relationship> relationships;
    
    
    private static final Logger fLogger =
    Logger.getLogger(Puzzled.class.getPackage().getName());
    


    
    @FXML
    private void loadMe(ActionEvent event) {
        loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem47.lpf");
    }
    
    @FXML
    private void zoomInButtonAction(ActionEvent event) {
        
        if (logicProblem.getScale() <= maxZoom) {
            logicProblem.setScale(logicProblem.getScale()*zoomFactor);
        } else {
            notify(WarningType.WARNING, "Maximum zoom level reached!");
        }
        fLogger.log(Level.INFO, "scale:"+logicProblem.getScale());
    }    
    
    @FXML
    private void zoomOutButtonAction(ActionEvent event) {
        if (logicProblem.getScale() >= minZoom) {
            logicProblem.setScale(logicProblem.getScale()/zoomFactor);
        } else {
            notify(WarningType.WARNING,"Minimum zoom level reached!");
        }
        fLogger.log(Level.INFO,"scale:"+logicProblem.getScale());
    }

        
    @FXML
    private void addClueButtonAction(ActionEvent event) {
        logicProblem.getClues().add(new Clue(clueText.getText()));
        clueText.clear();
        notify(WarningType.SUCCESS,"Clue "+logicProblem.getClues().size()+" was just added!");
    }
    
    @FXML
    private void quit(ActionEvent event) {
        //make sure logicProblem is not dirty!
        Platform.exit();
    }
    
    public void notify(WarningType type, String text) {
        nPane.setGraphic(new ImageView(new Image("/icons/"+type.getImageName())));  
        nPane.setText(text);
        nPane.show();
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //ScrollPane scroll = new ScrollPane();
        setupNotifier();
        mainScroll.setPannable(true);
//        Grid logicProblemGrid = new Grid(logicProblem);


//        mainScroll.setContent(logicProblemGrid);
//        mainGroup.getChildren().add(logicProblemGrid);
          
        //loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem47.lpf");
        
        mainGrid.sceneProperty().addListener((observable, oldvalue, newvalue) -> {
            if (newvalue!=null) setupDragNDrop(mainGrid.getScene());
        });
        
    //    relationships = new HashMap<Pair<Item,Item>,Relationship>(logicProblem.getNumItems()*logicProblem.getNumItems()*(logicProblem.getNumCategories()-1)*logicProblem.getNumCategories()/2);
    }    
    
    
    public Logger getLogger(){
        return fLogger;
    }
    
    
        /*
    * Sets up the drag and drop capability for files and URLs to be
    * dragged and dropped onto the scene. This will load the image into
    * the current image view area.
    * @param scene The primary application scene.
    */
    private void setupDragNDrop(Scene scene) {
        // Dragging over surface
        scene.setOnDragOver((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if ( db.hasFiles() ) {
                event.acceptTransferModes(TransferMode.LINK);
            } else {
                event.consume();
            }
        });
        // Dropping over surface
        scene.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                db.getFiles()
                .stream()
                .forEach( file -> {
                    try {
                    loadProblem(Paths.get(file.toURI()).toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            event.setDropCompleted(true);
            event.consume();
            
        });
        
    }

        /*
    * This routine loads a logic problem file by de-serializing
    * the object from file
    * @param myFile string representing the filename to be loaded.
    */
    public void loadProblem(String myFile){
        fLogger.log(Level.INFO, "loading logic problem file: " + myFile);
        loadProblem(new File(myFile));
    }
        
    private void loadProblem(File file){
        //check that there are no problem already loaded
        
        LogicProblem newProblem = null;
        try {

		JAXBContext jaxbContext = JAXBContext.newInstance(LogicProblem.class);

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		newProblem = (LogicProblem) jaxbUnmarshaller.unmarshal(file);
                fLogger.log(Level.INFO, newProblem.toString());
                logicProblem = newProblem;
                Grid logicProblemGrid = new Grid(this,logicProblem);
                mainGroup.getChildren().clear();
                mainGroup.getChildren().add(logicProblemGrid);
                //bind labels layer visibility to checkMenuItem
                logicProblemGrid.getChildren().get(1).visibleProperty().bind(hideLabelsMenuItem.selectedProperty().not());
                //bind relationships layer visibility to checkMenuItem        
//                logicProblemGrid.getChildren().get(2).visibleProperty().bind(hideRelationshipsMenuItem.selectedProperty().not());
                fLogger.log(Level.WARNING, Integer.toString(logicProblem.getClues().size()));
                clueCounter.textProperty().bind(Bindings.size(logicProblem.getClues()).add(1).asString().concat("->"));
                notify(WarningType.SUCCESS, "Problem file "+file.getName()+" loaded successfully!");
                
	  } catch (JAXBException e) {
		notify(WarningType.WARNING, "Unable to load problem file "+file.getName()+ "!");
                e.printStackTrace();
	  }
    }
    
    
    public void openFile() {
        fLogger.log(Level.INFO, "opening file invoked");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Logic Problem File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Logic Problem Files", "*.lpf"));
        Stage mainStage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            fLogger.log(Level.INFO, "file "+selectedFile.getName());
            loadProblem(selectedFile);
        } else {
            fLogger.log(Level.INFO, "no file selected");
        
        }
    }
    
    public void saveFile(){
        
        fLogger.log(Level.INFO, "saveFile invoked");
        File file = new File("test.lpf");
        try {
                   
                    JAXBContext jaxbContext = JAXBContext.newInstance(LogicProblem.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                    // output pretty printed
                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                    jaxbMarshaller.marshal(logicProblem, file);
                    jaxbMarshaller.marshal(logicProblem, System.out);
                    notify(WarningType.SUCCESS, "File "+file.getName()+" saved successfully!");
        } catch (JAXBException e) {
                    notify(WarningType.WARNING, "Unable to save file "+file.getName()+"!");
                    e.printStackTrace();
        }
    }

    public void setupNotifier() {
        nPane.setContent(bPane);
        //nPane.getStylesheets().add(getClass().getResource("Puzzled.css").toExternalForm());
        nPane.setOnShown(e -> {
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(notificationTimer), f -> nPane.hide()));
            timeline.play();
        });
        nPane.setOnKeyPressed(e -> {
             KeyCode key = e.getCode();
             if (key == KeyCode.ESCAPE) {
                 nPane.hide();
             }
        });

    }
    
    
}
