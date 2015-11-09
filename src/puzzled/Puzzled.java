/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.net.MalformedURLException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Fred
 */
public class Puzzled extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(event -> System.out.println("Hello World")); //using lambda expressions
        
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().add(new MenuItem("New"));

        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setOnAction(ae -> openFile());//using lambda expressions
        fileMenu.getItems().add(openMenuItem);
        
        
        fileMenu.getItems().add(new MenuItem("Save"));
        
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(ae -> Platform.exit());//using lambda expressions
        fileMenu.getItems().add(exitMenuItem);
        
        menuBar.getMenus().add(fileMenu);
        
  
        BorderPane root = new BorderPane();
        root.setCenter(btn);
        root.setTop(menuBar);
        
        Scene scene = new Scene(root, 300, 250);
        
        setupDragNDrop(scene);
        
        primaryStage.setTitle("Puzzled!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private static void openFile() {
        loadProblem("test");
    }
    
    private static void loadProblem(String myFile){
        System.out.println("loading logic problem file: " + myFile);
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
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
