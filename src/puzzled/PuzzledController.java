/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import puzzled.UI.Grid;
import puzzled.data.DemoProblem;
import puzzled.data.Item;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;

/**
 *
 * @author Fred
 */
public class PuzzledController implements Initializable {
    
    @FXML
    private TextField clueField;

    @FXML
    private BorderPane gridPane;
    
    private LogicProblem logicProblem;
    private HashMap<Pair<Item,Item>,Relationship> relationships;
    
    
    private static final Logger fLogger =
    Logger.getLogger(Puzzled.class.getPackage().getName());
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        //System.out.println("You clicked me!");
        clueField.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        logicProblem = DemoProblem.generateDemoProblem47();
        gridPane.setCenter(new Grid(logicProblem));
        gridPane.sceneProperty().addListener((observable, oldvalue, newvalue) -> {
            if (newvalue!=null) setupDragNDrop(gridPane.getScene());
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
                    loadProblem(file.toURI().toURL().toString());
                    } catch (MalformedURLException ex) {
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
        //check that there are no problem already loaded
        System.out.println("loading logic problem file: " + myFile);
        
        try(
            InputStream file = new FileInputStream("test.lpf");
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream (buffer);
          ){
            //deserialize the object
            logicProblem = (LogicProblem) input.readObject();
            //display its data
            fLogger.setLevel(Level.INFO);
            fLogger.log(Level.INFO, "trying out the logger."+ logicProblem);
          }
          catch(ClassNotFoundException ex){
            fLogger.log(Level.SEVERE, "Cannot perform input. Class not found.", ex);
          }
          catch(IOException ex){
            fLogger.log(Level.SEVERE, "Cannot perform input.", ex);
          }
    }
    
    
    public void openFile() {
        System.out.println("openFile called");
        //loadProblem("test.lpf");
        logicProblem = DemoProblem.generateDemoProblem47();
    }
    
    public void saveFile(){
        
        System.out.println("saveFile called");
        System.out.println(logicProblem);
        
        try (
          OutputStream file = new FileOutputStream("test.lpf");
          OutputStream buffer = new BufferedOutputStream(file);
          ObjectOutput output = new ObjectOutputStream(buffer);
        ){
          output.writeObject(logicProblem);
        }  
        catch(IOException ex){
          fLogger.log(Level.SEVERE, "Cannot perform output.", ex);
        }
    }
    
}
