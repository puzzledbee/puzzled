/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import puzzled.data.LogicProblem;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.Stage;

/**
 *
 * @author Fred
 */
public class Puzzled extends Application {
    
    private LogicProblem logicProblem;
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private boolean dirtyProblem = false;
    
    public Puzzled() {
        System.out.println("Puzzled ocnstructor invoked");
        //logicProblem = new LogicProblem();
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        Parent root = FXMLLoader.load(getClass().getResource("Puzzled.fxml"));
        
        //BorderPane root = new BorderPane();
        
        //root.setTop(new PuzzledMenuBar(this));
        
        //gridPane.setCenter(new Grid(this));
        //clueField.setText("test this");
        
        Scene scene = new Scene(root);
        
        setupDragNDrop(scene);
        
        primaryStage.setMaximized(true);
        
        primaryStage.setTitle("Puzzled!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void openFile() {
        System.out.println("openFile called");
        loadProblem("test.lpf");
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
    public Logger getLogger(){
        return fLogger;
    }
    
    public boolean getDirtyProblem(){
        return dirtyProblem;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("test this output");
        launch(args);
    }
    
}
