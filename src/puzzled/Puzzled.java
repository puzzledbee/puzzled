/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.controlsfx.control.NotificationPane;


/**
 *
 * @author Fred
 */
public class Puzzled extends Application {
    
    private static final String banner = "Puzzled! - Computer-aided logic problem solver";
    public static final String version = "2.0";
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private boolean dirtyProblem = false;
    
    private NotificationPane nPane;
    
    public Puzzled() {
        
        //logicProblem = new LogicProblem();
    }
    
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        
//        Parent root = FXMLLoader.load(getClass().getResource("Puzzled.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Puzzled.fxml"));
        Scene scene = new Scene(loader.load());
        PuzzledController controller = (PuzzledController)loader.getController();
//        nPane = new NotificationPane(root);
        
        
        //setting application icon. Icon files are in resources/icons
        primaryStage.getIcons().add(new Image("/icons/puzzle_16.png"));
        //new Image(Puzzled.class.getResourceAsStream( "/icons/puzzle_16.png" ))); 
        primaryStage.getIcons().add(new Image("/icons/puzzle_32.png"));
        //new Image(Puzzled.class.getResourceAsStream( "/icons/puzzle_32.png" ))); 
        
        primaryStage.setMaximized(true);
        
        primaryStage.titleProperty().bind(Bindings.createStringBinding(() -> controller.getLogicProblemProperty().isNull().get() ? banner+" v."+version : banner+" v."+version+" - "+controller.getLogicProblemProperty().get().getTitle(), controller.getLogicProblemProperty()));
//        primaryStage.setTitle("Puzzled! - Computer-aided logic problem solver - v."+Puzzled.version);
        primaryStage.setScene(scene);
        primaryStage.show();
//        controller.loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem47.lpf");
        

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
