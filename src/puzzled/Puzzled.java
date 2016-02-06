/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.controlsfx.control.NotificationPane;


/**
 *
 * @author phiv
 */
public class Puzzled extends Application {
    
    private static final String banner = "Puzzled! - Computer-aided logic problem solver";
    public static final String version = "2.0.1";
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    private static Object FileUtils;
    
    
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
        
        controller.setupTitleBinding(primaryStage.titleProperty(), banner, version);

        primaryStage.setScene(scene);
        primaryStage.show();
//        controller.loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem47.lpf");
        
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
        exportResource("/samples/problem47.lpf");
        exportResource("/samples/problem33.lpf");
        launch(args);
    }
}
