/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author phiv
 */
public class Puzzled extends Application {
    private static final String banner = "Puzzled! - Logic problem solver";
    public static final String version = "2.0.3";
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    private static Object FileUtils;
    
    
    //private NotificationPane nPane;
    
    public Puzzled() {
        
        //logicProblem = new LogicProblem();
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Puzzled.fxml"));
        Scene scene = new Scene(root);
        
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("Puzzled.fxml"));
        //Scene scene = new Scene(loader.load());
        //PuzzledController controller = (PuzzledController)loader.getController();
        //controller.setupTitleBinding(primaryStage.titleProperty(), banner, version);
        
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
        
        
        //controller.loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem47.lpf");
        
        //String css = PuzzledController.class.getResource("Puzzled.css").toExternalForm(); //seems necessary to re-specify CSS for the notifications popup
        //primaryStage.getScene().getStylesheets().add(css);
        //Notifications.create().owner(primaryStage).text("my message").hideAfter(Duration.seconds(2)).showInformation();        
        
        //set application icon
        //the alternative method primaryStage.getIcons().add(new Image("file:icon.png")); does not work
        primaryStage.getIcons().add(new Image(Puzzled.class.getResourceAsStream("/icons/puzzle_32.png")));
        
    }
    
    
    
    public Logger getLogger(){
        return fLogger;
    }
    
    /**
     * Export a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName ie.: "/SmartLibrary.dll"
     * @return The path to the exported resource
     * @throws Exception
     */
    static public void exportResource(String resourceName) {
        try {
            URL inputUrl = Puzzled.class.getResource(resourceName);
            String jarFolder = new File(Puzzled.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            File dest = new File(jarFolder + resourceName);
            org.apache.commons.io.FileUtils.copyURLToFile(inputUrl, dest);
        } catch (Exception e) {
                fLogger.log(Level.WARNING, "unable to extract resource file "+resourceName);
        }
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
