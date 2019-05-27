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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.NotificationPane;
import puzzled.UI.Grid;
import puzzled.UI.SlideOutPane;
import puzzled.data.Category;
import puzzled.data.Clue;
import puzzled.data.Item;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;
import puzzled.exceptions.RelationshipConflictException;
import puzzled.exceptions.SuperfluousRelationshipException;
import puzzled.processor.Processor;



/**
 * FXML Controller class
 *
 * @author https://github.com/bepuzzled
 */
public class PuzzledController {
    
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
    private AnchorPane mainGrid;
    
    @FXML
    private NotificationPane nPane;
    
    @FXML
    private BorderPane bPane;
    
    @FXML
    private VBox clueEngineVBox;
    
    @FXML
    private ToolBar toolbar;
    
    @FXML
    private HBox clueGlyphBox;
    
    @FXML
    private CheckMenuItem automaticProcessingMenuItem;
    
    @FXML
    private Label clueCounter;
    
    @FXML
    private Parent root;
    
    @FXML
    private Group mainGroup;
    
    private SlideOutPane soPane;
    
    @FXML
    private MenuItem propertiesMenuItem;
    
    @FXML
    private CheckMenuItem hideLabelsMenuItem;
    @FXML
    private CheckMenuItem hideRelationshipsMenuItem;
    
    @FXML
    private CheckMenuItem hideClueEngineMenuItem;
    
    @FXML
    private HiddenSidesPane pane;
    
    ObjectProperty<LogicProblem> logicProblem = new SimpleObjectProperty<LogicProblem>();
    DoubleProperty scaleProperty = new SimpleDoubleProperty();
    
    private HashMap<Pair<Item,Item>,Relationship> relationships;
    private BooleanProperty dirtyLogicProperty = new SimpleBooleanProperty();
    private BooleanProperty dirtyFileProperty = new SimpleBooleanProperty();
    private static final Logger fLogger =
    Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private String appTitle;
    private String appVersion;
    private StringProperty appTitleProperty = new SimpleStringProperty();
    
    Grid logicProblemGrid;
    private boolean processingFlag = false;

    private StringProperty filenameProperty = new SimpleStringProperty(null); //used to store loaded file name    
    
    
    public void notify(WarningType type, String text) {
        nPane.setGraphic(new ImageView(new Image("/icons/notification-pane/"+type.getImageName())));  
        nPane.setText(text);
        nPane.show();
    }
    
    

    
    /**
     * Initializes the controller class.
     */
    public void initialize() {
        setupNotifier(); //configures the notification pane slide down
        fLogger.log(Level.INFO, "java version:" + System.getProperty("java.version"));
        fLogger.log(Level.INFO, "javafx version:" + System.getProperty("javafx.version"));
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
    
    //invoked by main class Puzzled.java
    public void setupTitleBinding(StringProperty puzzledTitleProperty, String banner, String version) {
        puzzledTitleProperty.bind(this.appTitleProperty);
        appTitle = banner;
        appVersion = version;
        this.appTitleProperty.set(banner+" - "+version);
    }
    
    /** 
     * FXML UI actions event handlers
     * 
     */
    @FXML
    public void openAction() {
        fLogger.log(Level.INFO, "opening file invoked");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Logic Problem File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Logic Problem Files", "*.lpf"),
                new FileChooser.ExtensionFilter("Logic Problem Shorthand", "*.lps"),
                new FileChooser.ExtensionFilter("Logic Problem Clues", "*.lpc"));
        Stage mainStage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            fLogger.log(Level.INFO, "file "+selectedFile.getName());
            loadProblem(selectedFile);
        } else {
            fLogger.log(Level.INFO, "no file selected");
        }
    }
    
    @FXML
    private void quit(ActionEvent event) {
        //make sure logicProblem is not dirty, else trigger a "save dialog"!
        Platform.exit();
    }
    
    @FXML
    private void zoomInButtonAction(ActionEvent event) {
        
        //if (logicProblem.get().getScale() <= maxZoom) {
        //    logicProblem.get().setScale(logicProblem.get().getScale()*zoomFactor);
        //} else {
            notify(WarningType.WARNING, "Maximum zoom level reached!");
        //}
        //fLogger.log(Level.INFO, "scale:"+logicProblem.get().getScale());
        fLogger.log(Level.INFO, "zooming");
    }    
    
    
    public ObjectProperty<LogicProblem> getLogicProblemProperty(){
        return logicProblem;
    }
 
    
    /*
    * This routine loads a logic problem file by de-serializing
    * the object from file
    * @param myFile string representing the filename to be loaded.
    */
    public void loadProblem(String myFile){
        loadProblem(new File(myFile));
    }
        
    private void loadProblem(File file){
        //check that there are no problem already loaded
        fLogger.log(Level.INFO, "loading logic problem file: " + file.getName());
        LogicProblem newProblem = null;
        String extension = file.getName().replaceAll("^.*\\.([^.]+)$", "$1");
        if (extension.equalsIgnoreCase("lpf")) {
            try {

                    JAXBContext jaxbContext = JAXBContext.newInstance(LogicProblem.class);

                    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                    newProblem = (LogicProblem) jaxbUnmarshaller.unmarshal(file);
                    fLogger.log(Level.INFO, newProblem.toString());
                    logicProblem.set(newProblem);
                    this.filenameProperty.set(file.getName());
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

                //using the VARARG constructor to feed all relevant puzzle information
                newProblem = new LogicProblem(lines.get(0).split(";"));
                int i=1;
                int catIndex=1;
                Boolean problemTextToggle = false;
                String problemText = "";
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
                            if (lines.get(i).trim().isEmpty()) {
                                problemTextToggle = true; 
                                i++; //skip over blank line
                            } else {
                                if (problemTextToggle) {
                                    System.out.println("appending text "+lines.get(i));
                                    problemText += lines.get(i++) + "\n";
                                } else {
                                    System.out.println("adding clue "+lines.get(i)+" ("+i+")");
                                    String[] clueInfo = lines.get(i++).split(";");
                                    Clue newClue = new Clue(clueInfo);
                                    newProblem.addClue(newClue); //this will parse the clue
                                }
                            }
                        } //else there are no clues present
                    }
                }
                if (!problemText.trim().isEmpty()) newProblem.setText(problemText);

                logicProblem.set(newProblem);
                this.dirtyFileProperty.set(true);//to enable save as (one cannot save an .lps file)
                this.filenameProperty.set(file.getName());
                initializeProblem(); //this will add the clue glyphs
                notify(WarningType.SUCCESS, "Problem file "+file.getName()+" loaded successfully!");
            
            } catch (IOException e) {
                notify(WarningType.WARNING, "Unable to load shorthand problem file "+file.getName()+ "!");
                e.printStackTrace();
            }
        } else if (extension.equalsIgnoreCase("lpc")) {
            if (logicProblem.isNotNull().getValue()) {
                try {
                    List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
                    for (String line : lines) {
                        String[] clueInfo = line.split(";");
                        Clue newClue = new Clue(clueInfo);
                        logicProblem.get().addClue(newClue); //this will parse the clue
                        logicProblem.get().setFileDirty(true);
                        if (newClue.getType() != Clue.ClueType.CONSTRAINT) {
                            clueGlyphBox.getChildren().add(generateClueGlyph(newClue));
                        }
                    }
                } catch (IOException e) {
                    notify(WarningType.WARNING, "Unable to load problem clues file "+file.getName()+ "!");
                    e.printStackTrace();
                }
            } else  {
                notify(WarningType.WARNING, "A logic problem must be created or loaded before adding a .lpc file!");
            }
        } else {
            notify(WarningType.WARNING, "Filetype not supported. Please choose .lpf, .lps or .lpc files!");
        }
    }    
private void initializeProblem(){
        
            logicProblem.get().generateRelationships();
            logicProblemGrid = new Grid(this,logicProblem.get());
            mainGroup.getChildren().clear();
            mainGroup.getChildren().add(logicProblemGrid);
            
            clueGlyphBox.getChildren().clear();
            
            //bind labels layer visibility to checkMenuItem
            logicProblemGrid.getChildren().get(1).visibleProperty().bind(hideLabelsMenuItem.selectedProperty().not());
            //bind relationships layer visibility to checkMenuItem        
            logicProblemGrid.getChildren().get(2).visibleProperty().bind(hideRelationshipsMenuItem.selectedProperty().not());
            
            this.dirtyLogicProperty.bind(logicProblem.get().dirtyLogicProperty());
            this.dirtyFileProperty.bind(logicProblem.get().dirtyFileProperty());
            this.scaleProperty.bind(logicProblem.get().scaleProperty());
//            this.titleLabel.textProperty().bind(logicProblem.get().getTitleProperty());
//            soPane.textProperty().bind(logicProblem.get().problemTextProperty());
            soPane.textProperty().bindBidirectional(logicProblem.get().problemTextProperty());
            this.dirtyLogicProperty.addListener((e,oldValue,newValue) -> {
                System.out.println("change detected to dirtyLogicProperty");
                this.process();
            });
            
            //logicProblem.get().getClues().stream().filter(e -> e.getType() != Clue.ClueType.CONSTRAINT).count()
            //                    .stream().filter(e -> e.getType() != Clue.ClueType.CONSTRAINT).collect(collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)))
            
            clueCounter.textProperty().bind(Bindings.size(logicProblem.get().getFilteredClues()).add(1).asString().concat("->"));
            
            this.appTitleProperty.bind(Bindings.createStringBinding(() -> logicProblem.get().dirtyFileProperty().get()?
                    appTitle +" v."+appVersion+" -  "+logicProblem.get().titleProperty().getValue()+(this.filenameProperty.getValue()==null?"": "   ("+this.filenameProperty.getValue()+") *"):
                    appTitle +" v."+appVersion+" -  "+logicProblem.get().titleProperty().getValue()+(this.filenameProperty.getValue()==null?"": "   ("+this.filenameProperty.getValue()+")"),this.dirtyFileProperty, this.filenameProperty));
//          

            //clues have already been added to the problem and parsed when loading the file
            //this is only to draw the glyph
            for (Clue clue : logicProblem.get().getFilteredClues()) clueGlyphBox.getChildren().add(generateClueGlyph(clue));
//                Label label = new Label(Integer.toString(logicProblem.get().getClues().indexOf(clue)+1));
//                Label label = new Label(Integer.toString(logicProblem.get().getFilteredClues().indexOf(clue)+1));
//                label.setTooltip(new Tooltip(clue.getText()+" ("+clue.getType()+")"));
//                
//                label.getStyleClass().add("clue_"+clue.getType());
    }
    
    private Label generateClueGlyph(Clue clue){
        Label label = new Label(Integer.toString(logicProblem.get().getFilteredClues().indexOf(clue)+1));
        label.setTooltip(new Tooltip(clue.getText()+" ("+clue.getType()+")"));
        label.getStyleClass().add("clue_"+clue.getType());
        return label;
    }
    public void process() {
//        System.out.println("process invoked "+automaticProcessingMenuItem.isSelected());
        
        if (automaticProcessingMenuItem.isSelected() && !processingFlag) {//prevents the changeHandler from triggering multiple
            //concurrent processingFlag loops
            processingFlag = true;
//            System.out.println("entering processingFlag loop");
            while (logicProblem.get().isLogicDirty()){
//                System.out.println("executing processingFlag loop");
                logicProblem.get().setLogicDirty(false);
                
                //re-process SPECIAL clues (with streams and filters maybe?)
                try {
                    Processor.cross(logicProblem.get());
                    Processor.uniqueness(logicProblem.get());
                    Processor.transpose(logicProblem.get());
                if (logicProblem.get().getNumItems() >3) Processor.commonality(logicProblem.get());
                } catch (SuperfluousRelationshipException | RelationshipConflictException e) {
                    notify(WarningType.WARNING, e.toString());
                }
            }
            processingFlag = false;
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
        
    
}
