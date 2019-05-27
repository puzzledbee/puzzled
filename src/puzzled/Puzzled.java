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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class Puzzled extends Application {
    private static final String banner = "Puzzled! - Logic problem solver";
    public static final String version = "3.0.0";
    
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Puzzled.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        PuzzledController controller = (PuzzledController)loader.getController();
        controller.setupTitleBinding(primaryStage.titleProperty(), banner, version);
        
        primaryStage.setMaximized(true);
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
//    static public void exportResource(String resourceName) {
//        try {
//            URL inputUrl = Puzzled.class.getResource(resourceName);
//            String jarFolder = new File(Puzzled.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
//            File dest = new File(jarFolder + resourceName);
//            org.apache.commons.io.FileUtils.copyURLToFile(inputUrl, dest);
//        } catch (Exception e) {
//                fLogger.log(Level.WARNING, "unable to extract resource file "+resourceName);
//        }
//    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        launch(args);
    }
    
}
