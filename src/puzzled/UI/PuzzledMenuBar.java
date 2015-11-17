/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.logging.Level;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCombination;
import puzzled.Puzzled;

/**
 *
 * @author Fred
 */
public class PuzzledMenuBar extends MenuBar {

    public PuzzledMenuBar(Puzzled myApplication) {
        
        myApplication.getLogger().log(Level.INFO, "generating menubar");
        
        Menu fileMenu = new Menu("_File");
        fileMenu.getItems().add(new MenuItem("_New"));

        MenuItem openMenuItem = new MenuItem("_Open");
        openMenuItem.setOnAction(ae -> myApplication.openFile());//using lambda expressions
        openMenuItem.setAccelerator(
            KeyCombination.keyCombination("SHORTCUT+O")
        );
        fileMenu.getItems().add(openMenuItem);
        
        MenuItem saveMenuItem = new MenuItem("_Save");
        saveMenuItem.setOnAction(ae -> myApplication.saveFile());//using lambda expressions
        saveMenuItem.setAccelerator(
            KeyCombination.keyCombination("SHORTCUT+S")
        );
        fileMenu.getItems().add(saveMenuItem);
        
        MenuItem exitMenuItem = new MenuItem("_Quit");
        exitMenuItem.setOnAction(ae -> Platform.exit());//using lambda expressions
        exitMenuItem.setAccelerator(
            KeyCombination.keyCombination("SHORTCUT+Q")
        );
        fileMenu.getItems().add(exitMenuItem);
        
        this.getMenus().add(fileMenu);

        Menu helpMenu = new Menu("_Help");
        //helpMenuItem.setOnAction(ae -> myApplication.openFile());//using lambda expressions
        helpMenu.getItems().add(new MenuItem("_Help"));

        MenuItem aboutMenuItem = new MenuItem("_About");
        //openMenuItem.setOnAction(ae -> myApplication.openFile());//using lambda expressions
        helpMenu.getItems().add(aboutMenuItem);
        
        this.getMenus().add(helpMenu);
        
    
    }
    
}
