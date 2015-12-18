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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import puzzled.UI.Grid;
import puzzled.data.Item;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;

/**
 *
 * @author Fred
 */
public class PuzzledController implements Initializable {
    
    private static double zoomFactor = 1.3;

    @FXML
    Parent root;
    
    @FXML
    private TextField clueField;

    @FXML
    private AnchorPane mainGrid;

    @FXML
    private StackPane mainStack;
    
    @FXML
    private ScrollPane mainScroll;

    @FXML
    private Group mainGroup;
    
    @FXML
    private CheckMenuItem hideLabelsMenuItem;

    @FXML
    private CheckMenuItem hideRelationshipsMenuItem;
    
    private LogicProblem logicProblem;
    private HashMap<Pair<Item,Item>,Relationship> relationships;
    
    
    private static final Logger fLogger =
    Logger.getLogger(Puzzled.class.getPackage().getName());
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //System.out.println("You clicked me!");
        clueField.setText("Hello World!");
    }

    @FXML
    private void zoomInButtonAction(ActionEvent event) {
        //System.out.println("You clicked me!");
        logicProblem.setScale(logicProblem.getScale()*zoomFactor);
    }
    
    @FXML
    private void zoomOutButtonAction(ActionEvent event) {
        //System.out.println("You clicked me!");
        logicProblem.setScale(logicProblem.getScale()/zoomFactor);
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
//        logicProblem = DemoProblems.generateDemoProblem0();
        
        
        //ScrollPane scroll = new ScrollPane();
        //scroll.setBackground(Background.EMPTY);

        mainScroll.setPannable(true);
//        Grid logicProblemGrid = new Grid(logicProblem);
        //bind labels layer visibility to checkMenuItem
//        logicProblemGrid.getChildren().get(1).visibleProperty().bind(hideLabelsMenuItem.selectedProperty().not());
        //bind relationships layer visibility to checkMenuItem        
//        logicProblemGrid.getChildren().get(2).visibleProperty().bind(hideRelationshipsMenuItem.selectedProperty().not());

//        mainScroll.setContent(logicProblemGrid);
//        mainGroup.getChildren().add(logicProblemGrid);
          
        loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem47.lpf");
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
    private void loadProblem(String myFile){
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
                Grid logicProblemGrid = new Grid(logicProblem);
                mainGroup.getChildren().clear();
                mainGroup.getChildren().add(logicProblemGrid);
                logicProblemGrid.getChildren().get(1).visibleProperty().bind(hideLabelsMenuItem.selectedProperty().not());
                
	  } catch (JAXBException e) {
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
        
        try {
                    File file = new File("test.lpf");
                    JAXBContext jaxbContext = JAXBContext.newInstance(LogicProblem.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                    // output pretty printed
                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                    jaxbMarshaller.marshal(logicProblem, file);
                    jaxbMarshaller.marshal(logicProblem, System.out);

        } catch (JAXBException e) {
                    e.printStackTrace();
        }
    }
    
}
