/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import puzzled.Puzzled;

/**
 *
 * @author Fred
 */
public class PuzzledMenuBar extends MenuBar {

    public PuzzledMenuBar(Puzzled myApplication) {
        
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().add(new MenuItem("New"));

        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setOnAction(ae -> myApplication.openFile());//using lambda expressions
        fileMenu.getItems().add(openMenuItem);
        
        
        fileMenu.getItems().add(new MenuItem("Save"));
        
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(ae -> Platform.exit());//using lambda expressions
        fileMenu.getItems().add(exitMenuItem);
        
        this.getMenus().add(fileMenu);
    
    
    }
    
}
