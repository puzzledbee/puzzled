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
    

    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private boolean dirtyProblem = false;
    
    public Puzzled() {
        
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
        
        //primaryStage.getIcons().add(new Image("file:puzzle_16.png"));
        //primaryStage.getIcons().add(new Image("file:puzzle_32.png"));
        primaryStage.setMaximized(true);
        
        primaryStage.setTitle("Puzzled!");
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
