/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Fred
 */
public class Puzzled extends Application {
    
    public static final String version = "1.0";
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private boolean dirtyProblem = false;
    
    public Puzzled() {
        
        //logicProblem = new LogicProblem();
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
        Parent root = FXMLLoader.load(getClass().getResource("Puzzled.fxml"));
        
        Scene scene = new Scene(root);
        
        //setting application icon. Icon files are in resources/icons
        primaryStage.getIcons().add(new Image("/icons/puzzle_16.png"));
        //new Image(Puzzled.class.getResourceAsStream( "/icons/puzzle_16.png" ))); 
        primaryStage.getIcons().add(new Image("/icons/puzzle_32.png"));
        //new Image(Puzzled.class.getResourceAsStream( "/icons/puzzle_32.png" ))); 
        
        primaryStage.setMaximized(true);
        primaryStage.setTitle("Puzzled! - Computer-aided logic problem solver - v."+Puzzled.version);
        primaryStage.setScene(scene);
        primaryStage.show();
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
        launch(args);
    }
}
