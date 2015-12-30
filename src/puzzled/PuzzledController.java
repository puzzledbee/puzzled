/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.Pair;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.controlsfx.control.NotificationPane;
import puzzled.UI.Grid;
import puzzled.data.Category;
import puzzled.data.Clue;
import puzzled.data.Item;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;
import puzzled.processor.Processor;

/**
 *
 * @author Fred
 */
public class PuzzledController implements Initializable {
    
    private static double zoomFactor = 1.3;
    private static double maxZoom = 2.3;
    private static double minZoom = 0.4;
    private static int notificationTimer = 3000;
    
    public enum WarningType {
        SUCCESS ("success.png"), 
        INFO ("info.png"), 
        WARNING ("fail.png");
        
        private String imageName;
        
        WarningType(String image){
            imageName = image;
        }
        
        public String getImageName() {
            return imageName;
        }
        
    }

    @FXML
    private Parent root;
    
    @FXML
    private Parent bPane;
    
    @FXML
    private HBox clueGlyphBox;
    
    @FXML
    private NotificationPane nPane;
    
    @FXML
    private Button addClueButton;
    
    @FXML
    private TextField clueText;

    @FXML
    private AnchorPane mainGrid;

    @FXML
    private StackPane mainStack;
    
    @FXML
    private ScrollPane mainScroll;

    @FXML
    private Group mainGroup;
    
    @FXML
    private Label clueCounter;
    
    @FXML
    private MenuItem saveMenuItem;
    
    @FXML
    private MenuItem saveAsMenuItem;
    
    @FXML
    private MenuItem printMenuItem;
    
    @FXML
    private MenuItem zoomInMenuItem;
    
    @FXML
    private MenuItem zoomOutMenuItem;
    
    @FXML
    private ToolBar toolbar;
    
    @FXML
    private CheckMenuItem hideToolbarMenuItem;
    
    @FXML
    private Button saveButton;
    
    @FXML
    private Button zoomOutButton;
    
    @FXML
    private Button zoomInButton;
    
    @FXML
    private MenuItem propertiesMenuItem;
    
    @FXML
    private CheckMenuItem hideLabelsMenuItem;
    @FXML
    private CheckMenuItem hideRelationshipsMenuItem;
    
    ObjectProperty<LogicProblem> logicProblem = new SimpleObjectProperty<LogicProblem>();
    
    private HashMap<Pair<Item,Item>,Relationship> relationships;
    private BooleanProperty dirtyLogicProperty = new SimpleBooleanProperty();
    
    private static final Logger fLogger =
    Logger.getLogger(Puzzled.class.getPackage().getName());
    
    Grid logicProblemGrid;
    private boolean processing = false;


    
    @FXML
    private void loadMe(ActionEvent event) {
//        loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem0.lpf");
        loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem47.lpf");
        
    }
    
    
    @FXML
    private void zoomInButtonAction(ActionEvent event) {
        
        if (logicProblem.get().getScale() <= maxZoom) {
            logicProblem.get().setScale(logicProblem.get().getScale()*zoomFactor);
        } else {
            notify(WarningType.WARNING, "Maximum zoom level reached!");
        }
        fLogger.log(Level.INFO, "scale:"+logicProblem.get().getScale());
    }    
    
    @FXML
    private void zoomOutButtonAction(ActionEvent event) {
        if (logicProblem.get().getScale() >= minZoom) {
            logicProblem.get().setScale(logicProblem.get().getScale()/zoomFactor);
        } else {
            notify(WarningType.WARNING,"Minimum zoom level reached!");
        }
        fLogger.log(Level.INFO,"scale:"+logicProblem.get().getScale());
    }

    @FXML
    private void print(ActionEvent event) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.LANDSCAPE, Printer.MarginType.EQUAL_OPPOSITES);
        double scaleX = pageLayout.getPrintableWidth() / this.logicProblemGrid.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / this.logicProblemGrid.getBoundsInParent().getHeight();
        Scale scaler = new Scale(scaleX,scaleY);
        
        System.out.println("pagewidth="+pageLayout.getPrintableWidth());
        System.out.println("pageheight="+pageLayout.getPrintableHeight());
        System.out.println("imagewidth="+this.logicProblemGrid.getBoundsInParent().getWidth());
        System.out.println("imagewidth="+this.logicProblemGrid.getBoundsInParent().getHeight());
        System.out.println("scaleX="+scaleX);
        System.out.println("scaleY="+scaleY);
        
        int numcats = this.logicProblem.get().getNumCategories()-1;
        System.out.println("numCats="+numcats);
        int numcells = numcats * this.logicProblem.get().getNumItems() + 1;
        System.out.println("\nnumCells="+numcells);
        int totalwidth = numcells*this.logicProblemGrid.cellwidth+this.logicProblemGrid.labelwidth;
        System.out.println("totalwidth="+totalwidth);
        double scaled = totalwidth*scaleX;
        System.out.println("scaled="+scaled);
        this.logicProblemGrid.getTransforms().add(scaler);
        //center grid in page
        Translate translator = new Translate((pageLayout.getPrintableWidth()-scaled)/2,0);
        this.logicProblemGrid.getTransforms().add(translator);
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        if (printerJob != null) {
            printerJob.getJobSettings().setPageLayout(pageLayout);
            
            boolean success = printerJob.printPage(this.logicProblemGrid);
            if (success) {
                printerJob.endJob();
                notify(WarningType.SUCCESS,"Printing successful!");
            } else {
                notify(WarningType.WARNING,"Printing not successful!");
            }
            this.logicProblemGrid.getTransforms().remove(scaler);
        }
    }
        
    @FXML
    private void addClueButtonAction(ActionEvent event) {
        Clue newClue = new Clue(clueText.getText());
        logicProblem.get().getClues().add(newClue);
        Label label = new Label(Integer.toString(logicProblem.get().getClues().size()));
        label.setTooltip(new Tooltip(clueText.getText()+" ("+newClue.getType()+")"));
        clueGlyphBox.getChildren().add(label);
        clueText.clear();
        notify(WarningType.SUCCESS,"Clue "+logicProblem.get().getClues().size()+" was just added!");
    }
    
    @FXML
    private void quit(ActionEvent event) {
        //make sure logicProblem is not dirty!
        Platform.exit();
    }
    
    public void notify(WarningType type, String text) {
        nPane.setGraphic(new ImageView(new Image("/icons/"+type.getImageName())));  
        nPane.setText(text);
        nPane.show();
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //ScrollPane scroll = new ScrollPane();
        setupNotifier();
        mainScroll.setPannable(true);
//        Grid logicProblemGrid = new Grid(logicProblem);


//        mainScroll.setContent(logicProblemGrid);
//        mainGroup.getChildren().add(logicProblemGrid);
          
        //loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem47.lpf");
        clueText.disableProperty().bind(logicProblem.isNull());
        addClueButton.disableProperty().bind(logicProblem.isNull());
        saveMenuItem.disableProperty().bind(logicProblem.isNull());
        saveAsMenuItem.disableProperty().bind(logicProblem.isNull());
        saveButton.disableProperty().bind(logicProblem.isNull());
        propertiesMenuItem.disableProperty().bind(logicProblem.isNull());
        printMenuItem.disableProperty().bind(logicProblem.isNull());
        toolbar.managedProperty().bind(hideToolbarMenuItem.selectedProperty().not());
//        zoomInMenuItem.disableProperty().bind(logicProblem.isNull().or(logicProblem.get().scaleProperty().greaterThanOrEqualTo(maxZoom)));
//        zoomOutMenuItem.disableProperty().bind(logicProblem.isNull().or(logicProblem.get().scaleProperty().greaterThanOrEqualTo(minZoom)));
        zoomInMenuItem.disableProperty().bind(logicProblem.isNull());
        zoomOutMenuItem.disableProperty().bind(logicProblem.isNull());
        zoomInButton.disableProperty().bind(logicProblem.isNull());
        zoomOutButton.disableProperty().bind(logicProblem.isNull());
        
        mainGrid.sceneProperty().addListener((observable, oldvalue, newvalue) -> {
            if (newvalue!=null) setupDragNDrop(mainGrid.getScene());
        });
        
    //    relationships = new HashMap<Pair<Item,Item>,Relationship>(logicProblem.getNumItems()*logicProblem.getNumItems()*(logicProblem.getNumCategories()-1)*logicProblem.getNumCategories()/2);
    }    
    
    
    public Logger getLogger(){
        return fLogger;
    }
    
    public ObjectProperty<LogicProblem> getLogicProblemProperty(){
        return logicProblem;
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
                    loadProblem(Paths.get(file.toURI()).toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            event.setDropCompleted(true);
            event.consume();
            
        });
        
    }

        /*
    * This routine loads a logic problem file by de-serializing
    * the object from file
    * @param myFile string representing the filename to be loaded.
    */
    public void loadProblem(String myFile){
        fLogger.log(Level.INFO, "loading logic problem file: " + myFile);
        loadProblem(new File(myFile));
    }
        
    private void loadProblem(File file){
        //check that there are no problem already loaded
        
        LogicProblem newProblem = null;
        String extension = file.getName().replaceAll("^.*\\.([^.]+)$", "$1");
        if (extension.equalsIgnoreCase("lpf")) {
            try {

                    JAXBContext jaxbContext = JAXBContext.newInstance(LogicProblem.class);

                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    newProblem = (LogicProblem) jaxbUnmarshaller.unmarshal(file);
                    fLogger.log(Level.INFO, newProblem.toString());
                    logicProblem.set(newProblem);
                    initializeProblem();
                    notify(WarningType.SUCCESS, "Problem file "+file.getName()+" loaded successfully!");
              } catch (JAXBException e) {
                    notify(WarningType.WARNING, "Unable to load problem file "+file.getName()+ "!");
                    e.printStackTrace();
              }
        } else if (extension.equalsIgnoreCase("lps")) {
//            System.out.println("trying to load Logic Problem Shorthand");
            try {
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//                System.out.println("problem "+lines.get(0)+" with "+ lines.size());
//                System.out.println("last line "+lines.get(lines.size()-1));
                newProblem = new LogicProblem(lines.get(0).split(";"));
                int i=1;
                int catIndex=1;

                while (i < lines.size()) {
                    if (i < lines.size()-1 && lines.get(i+1).startsWith("\t")) {
                        i++;
                        ArrayList<Item> items = new ArrayList<Item>();
                        while (i< lines.size() && lines.get(i).startsWith("\t")) {
                            System.out.println("adding item "+lines.get(i).trim()+" ("+i+")");
                            items.add(new Item(lines.get(i++).trim()));
                        }
//                        System.out.println("adding category "+lines.get(catIndex)+" ("+catIndex+")");
                        String[] catInfo = lines.get(catIndex).split(";");
                        Category newCat = new Category(items,catInfo);
                        catIndex = i;
                        newProblem.addCategory(newCat);
                    } else {
                        if (i<lines.size()) {
                            System.out.println("adding clue "+lines.get(i)+" ("+i+")");
                            String[] clueInfo = lines.get(i++).split(";");
                            Clue newClue = new Clue(clueInfo);
                            newProblem.addClue(newClue);
                        } //else there are no clues present
                    }
                }
                
                logicProblem.set(newProblem);
                initializeProblem();
                notify(WarningType.SUCCESS, "Problem file "+file.getName()+" loaded successfully!");
            
            } catch (IOException e) {
                notify(WarningType.WARNING, "Unable to load shorthand problem file "+file.getName()+ "!");
                e.printStackTrace();
            }
        } else if (extension.equalsIgnoreCase("lpc")) {
            try {
                List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                for (String line : lines) {
                    String[] clueInfo = line.split(";");
                    Clue newClue = new Clue(clueInfo);
                    logicProblem.get().addClue(newClue);
                    Label label = new Label(Integer.toString(logicProblem.get().getClues().indexOf(newClue)+1));
                    label.setTooltip(new Tooltip(newClue.getText()+" ("+newClue.getType()+")"));
                    clueGlyphBox.getChildren().add(label);
                }
            } catch (IOException e) {
                notify(WarningType.WARNING, "Unable to load problem clues file "+file.getName()+ "!");
                e.printStackTrace();
            }
        } else {
            notify(WarningType.WARNING, "Filetype not supported. Please choose .lpf, .lps or .lpc files!");
        }
    }
    
    public void resetButtonActtion(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Reset Logic Problem");
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.setContentText("Resetting the Logic Problem will clear all relationships and clues\nDo you wish to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            logicProblem.get().getClues().clear();
            this.clueGlyphBox.getChildren().clear();
            
            this.initializeProblem();
            notify(WarningType.SUCCESS, "Logic problem reset successfully!");
        }
    }
    
    private void initializeProblem(){
        
            logicProblem.get().generateRelationships();
//                    System.out.println(logicProblem.get().getRelationshipTable().size());
            logicProblemGrid = new Grid(this,logicProblem.get());
            mainGroup.getChildren().clear();
            mainGroup.getChildren().add(logicProblemGrid);
            //bind labels layer visibility to checkMenuItem
            logicProblemGrid.getChildren().get(1).visibleProperty().bind(hideLabelsMenuItem.selectedProperty().not());

            //bind relationships layer visibility to checkMenuItem        
            logicProblemGrid.getChildren().get(2).visibleProperty().bind(hideRelationshipsMenuItem.selectedProperty().not());
            this.dirtyLogicProperty.bind(logicProblem.get().dirtyLogicProperty());
            
            this.dirtyLogicProperty.addListener((e,oldValue,newValue) -> {
                System.out.println("change detected to dirtyLogicProperty");
                this.process();
                    });
            clueCounter.textProperty().bind(Bindings.size(logicProblem.get().getClues()).add(1).asString().concat("->"));
            
            for (Clue clue : logicProblem.get().getClues()){
                Label label = new Label(Integer.toString(logicProblem.get().getClues().indexOf(clue)+1));
                label.setTooltip(new Tooltip(clue.getText()+" ("+clue.getType()+")"));
                clueGlyphBox.getChildren().add(label);
            }
            
            
    }
    
    public void openFile() {
        fLogger.log(Level.INFO, "opening file invoked");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Logic Problem File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Logic Problem Files", "*.lpf"),
                new ExtensionFilter("Logic Problem Shorthand", "*.lps"),
                new ExtensionFilter("Logic Problem Clues", "*.lpc"));
        Stage mainStage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            fLogger.log(Level.INFO, "file "+selectedFile.getName());
            loadProblem(selectedFile);
        } else {
            fLogger.log(Level.INFO, "no file selected");
        
        }
    }
    
    
    public void process() {
        System.out.println("process invoked");
        if (!processing) {//prevents the changeHandler from triggering multiple
            //concurrent processing loops
            processing = true;
            System.out.println("entering processing loop");
            while (logicProblem.get().isLogicDirty()){
                System.out.println("executing processing loop");
                logicProblem.get().setLogicDirty(false);
                Processor.cross(logicProblem.get());
                Processor.findUnique(logicProblem.get());
                Processor.transpose(logicProblem.get());
            }
            processing = false;
        }
            
//        logicProblem.get().getRelationshipTable().get(
//                new ItemPair(logicProblem.get().getCategories().get(0).getItems().get(4),
//                            logicProblem.get().getCategories().get(1).getItems().get(2))
//                ).setValue(Relationship.ValueType.VALUE_NO);
//
//        logicProblem.get().getRelationshipTable().get(
//                new ItemPair(logicProblem.get().getCategories().get(0).getItems().get(1),
//                            logicProblem.get().getCategories().get(1).getItems().get(2))
//                ).setValue(Relationship.ValueType.VALUE_NO);
//        
//        logicProblem.get().getRelationshipTable().get(
//                new ItemPair(logicProblem.get().getCategories().get(0).getItems().get(0),
//                            logicProblem.get().getCategories().get(1).getItems().get(2))
//                ).setValue(Relationship.ValueType.VALUE_NO);
//
//        logicProblem.get().getRelationshipTable().get(
//                new ItemPair(logicProblem.get().getCategories().get(0).getItems().get(3),
//                            logicProblem.get().getCategories().get(1).getItems().get(2))
//                ).setValue(Relationship.ValueType.VALUE_NO);

        
        
//        logicProblem.get().getRelationshipTable().get(
//                new ItemPair(logicProblem.get().getCategories().get(3).getItems().get(2),
//                            logicProblem.get().getCategories().get(1).getItems().get(2))
//                ).setValue(Relationship.ValueType.VALUE_YES);
//                System.out.println("item1: "+logicProblem.get().getCategories().get(0).getItems().get(0).getName()+", item2: "+
//                        logicProblem.get().getCategories().get(1).getItems().get(2).getName());
//
//                Bounds bound = logicProblem.get().getRelationshipTable().get(
//                        new ItemPair(logicProblem.get().getCategories().get(2).getItems().get(0),
//                                    logicProblem.get().getCategories().get(3).getItems().get(2))
//                        ).boundProperty().get();
//                System.out.println("minX: "+bound.getMinX());
//                System.out.println("minY: "+bound.getMinY());
//                System.out.println("maxX: "+bound.getMaxX());
//                System.out.println("maxY: "+bound.getMaxY());
    }
    
    
    public void saveFile(){
        
        fLogger.log(Level.INFO, "saveFile invoked");
        File file = new File("test.lpf");
        try {
                   
                    JAXBContext jaxbContext = JAXBContext.newInstance(LogicProblem.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                    // output pretty printed
                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    
//                    LogicProblem newProblem = DemoProblems.generateDemoProblem47();
                    jaxbMarshaller.marshal(logicProblem.get(), file);
                    
                    jaxbMarshaller.marshal(logicProblem.get(), System.out);
                    notify(WarningType.SUCCESS, "File "+file.getName()+" saved successfully!");
        } catch (JAXBException e) {
                    notify(WarningType.WARNING, "Unable to save file "+file.getName()+"!");
                    e.printStackTrace();
        }
    }

    public void setupNotifier() {
        nPane.setContent(bPane);
        //nPane.getStylesheets().add(getClass().getResource("Puzzled.css").toExternalForm());
        nPane.setOnShown(e -> {
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(
                new KeyFrame(Duration.millis(notificationTimer), f -> nPane.hide()));
            timeline.play();
        });
        nPane.setOnKeyPressed(e -> {
             KeyCode key = e.getCode();
             if (key == KeyCode.ESCAPE) {
                 nPane.hide();
             }
        });

    }
    
    
}
