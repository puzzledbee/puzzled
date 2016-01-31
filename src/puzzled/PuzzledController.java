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
import java.util.HashSet;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
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
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
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
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.NotificationPane;
import puzzled.UI.Grid;
import puzzled.UI.SlideOutPane;
import puzzled.data.Category;
import puzzled.data.Clue;
import puzzled.data.Item;
import puzzled.data.ItemPair;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;
import puzzled.exceptions.RelationshipConflictException;
import puzzled.exceptions.SuperfluousRelationshipException;
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
    
    StringProperty filenameProperty = new SimpleStringProperty(null);
    
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
    private BorderPane bPane;
    
    @FXML
    private TabPane tPane;
    
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
    private CheckMenuItem automaticProcessingMenuItem;
    
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
    
    @FXML
    private CheckMenuItem hideClueEngineMenuItem;
    
    @FXML
    private HiddenSidesPane pane;

//    @FXML
    private SlideOutPane soPane;

//    @FXML
//    private void handleMouseClicked(MouseEvent event) {
//        pane.setPinnedSide((pane.getPinnedSide() != null)?null:Side.RIGHT);
//    }
    
    @FXML
    private VBox clueEngineVBox;
    
    ObjectProperty<LogicProblem> logicProblem = new SimpleObjectProperty<LogicProblem>();
    DoubleProperty scaleProperty = new SimpleDoubleProperty();
    
    private HashMap<Pair<Item,Item>,Relationship> relationships;
    private BooleanProperty dirtyLogicProperty = new SimpleBooleanProperty();
    private BooleanProperty dirtyFileProperty = new SimpleBooleanProperty();
    
    private static final Logger fLogger =
    Logger.getLogger(Puzzled.class.getPackage().getName());
    
    Grid logicProblemGrid;
    private boolean processingFlag = false;
    private String appTitle;
    private String appVersion;

    private StringProperty appTitleProperty = new SimpleStringProperty();
    
    @FXML
    private void handleAutomaticProcessingAction(ActionEvent event) {
        // Button was clicked, do something...
        process();
    }
    
    @FXML
    private void loadMe(ActionEvent event) {
//        loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem0.lpf");
        loadProblem("resources/samples/problem47.lpf");
    }
    
    
    public void setupTitleBinding(StringProperty puzzledTitleProperty, String banner, String version) {
        puzzledTitleProperty.bind(this.appTitleProperty);
        appTitle = banner;
        appVersion = version;
        this.appTitleProperty.set(banner+" - "+version);
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
        Double currentScale = logicProblem.get().getScale();
        logicProblem.get().setScale(1);
        
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.LANDSCAPE, Printer.MarginType.EQUAL_OPPOSITES);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(pageLayout.getPrintableWidth(), pageLayout.getPrintableHeight());
        borderPane.setMaxSize(pageLayout.getPrintableWidth(), pageLayout.getPrintableHeight());
        borderPane.setMinSize(pageLayout.getPrintableWidth(), pageLayout.getPrintableHeight());
        
        Label headerLabel = new Label(this.hideLabelsMenuItem.isSelected()?"":logicProblem.get().getTitle());
        Label sourceLabel = new Label(this.hideLabelsMenuItem.isSelected()?"":logicProblem.get().getSource());
        Label puzzledLabel = new Label("Grid printed using Puzzled! - http://puzzled.be");
        BorderPane.setAlignment(puzzledLabel, Pos.CENTER_LEFT);
        BorderPane footerPane = new BorderPane();
        footerPane.setLeft(puzzledLabel);

        BorderPane.setAlignment(headerLabel, Pos.CENTER);
        BorderPane.setMargin(headerLabel, new Insets(10, 20,0,20));
        borderPane.setTop(headerLabel);
        BorderPane.setAlignment(sourceLabel, Pos.CENTER_RIGHT);
        footerPane.setRight(sourceLabel);

        BorderPane.setMargin(footerPane, new Insets(0,20,3,20));
        borderPane.setBottom(footerPane);

        borderPane.getStylesheets().add("puzzled/Puzzled.css"); //necessary to preserve sytle
        int numcats = this.logicProblem.get().getNumCategories()-1;
        int numcells = numcats * this.logicProblem.get().getNumItems() + 1;
        int totalwidth = numcells*this.logicProblemGrid.cellwidth+this.logicProblemGrid.labelwidth;
        int totalheight = numcells*this.logicProblemGrid.cellwidth+this.logicProblemGrid.labelheight;
//        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        
//        System.out.println("label height="+fontLoader.computeStringWidth(headerLabel.getText(), headerLabel.getFont()));
//        System.out.println("label height="+borderPane.getTop().getBoundsInLocal().getHeight());
//        
        double scaleX = pageLayout.getPrintableWidth() / (totalwidth);
        //150 is the approximate heights of the header and footer labels. 
        //Unfortunatley, there appears to be no way to compute the label size before it is displayed on a scene
        //the FontLoader computeStringWidth(headerLabel.getText(),headerLabel.getFont()) does not have a height equivalent
        //
        double scaleY = pageLayout.getPrintableHeight()  / (totalheight+150); //
        double scaleMin = Double.min(scaleX, scaleY); //preserve aspect ratio
        
        Scale scaler = new Scale(scaleMin,scaleMin);
        
        VBox content = new VBox(this.logicProblemGrid); //VBox container necessary to prevent distortion 
                                                        //that occur on logicProblemGrid when calling setMinSize

        Translate translator = new Translate(100,-10); //fixed offsets appear to work best up to 6 categories
        content.getTransforms().add(translator);
        content.getTransforms().add(scaler);
        content.setMinSize(0,0); //necessary to prevent BorderPane center region from pushing out border regions 
        
        borderPane.setCenter(content);

//        System.out.println("bounds X="+this.logicProblemGrid.getBoundsInLocal().getWidth());
//        System.out.println("bounds Y="+this.logicProblemGrid.getBoundsInLocal().getHeight());
//        
//        System.out.println("numCats="+numcats);
//        System.out.println("numCells="+numcells);
//        
//        System.out.println("totalwidth="+totalwidth);
//        System.out.println("totalheight="+totalheight);
//        double scaled = totalwidth*scaleX;
//        System.out.println("scaled="+scaled);

        
        
//        System.out.println("pagewidth="+pageLayout.getPrintableWidth());
//        System.out.println("pageheight="+pageLayout.getPrintableHeight());
//        System.out.println("scaledwidth="+this.logicProblemGrid.getBoundsInParent().getWidth());
//        System.out.println("scaledheight="+this.logicProblemGrid.getBoundsInParent().getHeight());
//        System.out.println("scaleX="+scaleX);
//        System.out.println("scaleY="+scaleY);
        
        PrinterJob printerJob = PrinterJob.createPrinterJob();
        
        if (printerJob != null) {
            printerJob.getJobSettings().setPageLayout(pageLayout);
            
            boolean success = printerJob.printPage(borderPane);
            if (success) {
                printerJob.endJob();
                notify(WarningType.SUCCESS,"Printing successful!");
            } else {
                notify(WarningType.WARNING,"Printing not successful!");
            }
            //retore problem scale
            logicProblem.get().setScale(currentScale);
            //re-attach to scene
            mainGroup.getChildren().add(logicProblemGrid);
        }
    }
        
    @FXML
    private void addClueButtonAction(ActionEvent event) {
        Clue newClue = new Clue(clueText.getText());
        logicProblem.get().getClues().add(newClue);
        logicProblem.get().setFileDirty(true);
        Label label = new Label(Integer.toString(logicProblem.get().getFilteredClues().size()));
        label.setTooltip(new Tooltip(clueText.getText()+" ("+newClue.getType()+")"));
        clueGlyphBox.getChildren().add(label);
        clueText.clear();
        notify(WarningType.SUCCESS,"Clue "+logicProblem.get().getFilteredClues().size()+" was just added!");
    }
    
    @FXML
    private void quit(ActionEvent event) {
        //make sure logicProblem is not dirty!
        Platform.exit();
    }
    
    public void notify(WarningType type, String text) {
        nPane.setGraphic(new ImageView(new Image("/icons/notification-pane/"+type.getImageName())));  
        nPane.setText(text);
        nPane.show();
    }
    
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupNotifier();
        mainScroll.setPannable(true);
          
        //loadProblem("d:/lab/netbeans-projects/puzzled/resources/samples/problem47.lpf");  
        
        clueText.disableProperty().bind(logicProblem.isNull());
        addClueButton.disableProperty().bind(logicProblem.isNull());
        automaticProcessingMenuItem.disableProperty().bind(logicProblem.isNull());
        saveMenuItem.disableProperty().bind(Bindings.or(Bindings.or(this.logicProblem.isNull(),this.dirtyFileProperty.not()),this.filenameProperty.isNull()));
        saveAsMenuItem.disableProperty().bind(Bindings.or(logicProblem.isNull(),this.dirtyFileProperty.not()));
        
        saveButton.disableProperty().bind(Bindings.or(Bindings.or(this.logicProblem.isNull(),this.dirtyFileProperty.not()),this.filenameProperty.isNull()));
        propertiesMenuItem.disableProperty().bind(logicProblem.isNull());
        printMenuItem.disableProperty().bind(logicProblem.isNull());
        toolbar.managedProperty().bind(hideToolbarMenuItem.selectedProperty().not());
        
        clueEngineVBox.managedProperty().bind(hideClueEngineMenuItem.selectedProperty().not());
        clueEngineVBox.visibleProperty().bind(hideClueEngineMenuItem.selectedProperty().not());
        
        zoomInMenuItem.disableProperty().bind(Bindings.or(logicProblem.isNull(),this.scaleProperty.greaterThanOrEqualTo(maxZoom)));
        zoomOutMenuItem.disableProperty().bind(Bindings.or(logicProblem.isNull(),this.scaleProperty.lessThanOrEqualTo(minZoom)));
        zoomInButton.disableProperty().bind(Bindings.or(logicProblem.isNull(),this.scaleProperty.greaterThanOrEqualTo(maxZoom)));
        zoomOutButton.disableProperty().bind(Bindings.or(logicProblem.isNull(),this.scaleProperty.lessThanOrEqualTo(minZoom)));
        
//        this.logicProblem.addListener( (e, oldvalue, newvalue) -> {
////            System.out.println("unbinding and rebinding");
//            if (logicProblem.isNotNull().getValue()) {
////                zoomInButton.disableProperty().unbind();
////                zoomInMenuItem.disableProperty().unbind();
////                zoomOutButton.disableProperty().unbind();
////                zoomOutMenuItem.disableProperty().unbind();
////                zoomInButton.disableProperty().bind(logicProblem.get().scaleProperty().greaterThanOrEqualTo(maxZoom));
////                zoomInMenuItem.disableProperty().bind(logicProblem.get().scaleProperty().greaterThanOrEqualTo(maxZoom));
////                zoomOutButton.disableProperty().bind(logicProblem.get().scaleProperty().lessThanOrEqualTo(minZoom));
////                zoomOutMenuItem.disableProperty().bind(logicProblem.get().scaleProperty().lessThanOrEqualTo(minZoom));
//            }
//        });
        
        
        mainGrid.sceneProperty().addListener((observable, oldvalue, newvalue) -> {
            if (newvalue!=null) setupDragNDrop(mainGrid.getScene());
        });
        
        
        //mangle with borderPane to create the slide out using ControlsFX HiddenSidesPane
        //whilst this can (and probably should) be added within Scene Builder
        //it becomes impossible to use HiddenSidesPane as a parent
        //manually editing the FXML file works, but breaks Scene Builder functionality
        HiddenSidesPane hsPane = new HiddenSidesPane();
        soPane = new SlideOutPane(hsPane);
        //steal tPane from scene
        hsPane.setContent(tPane);
        hsPane.setRight(soPane);
        //inject into scene
        bPane.setCenter(hsPane);
        
        
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
                    this.filenameProperty.set(file.getName());
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
                                    newProblem.addClue(newClue);
                                }
                            }
                        } //else there are no clues present
                    }
                }
                if (!problemText.trim().isEmpty()) newProblem.setText(problemText);

                logicProblem.set(newProblem);
                initializeProblem();
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
                        logicProblem.get().addClue(newClue);
                        logicProblem.get().setFileDirty(true);
                        if (newClue.getType() != Clue.ClueType.CONSTRAINT) {
                            Label label = new Label(Integer.toString(logicProblem.get()
                                    .getFilteredClues().indexOf(newClue)+1));
                            label.setTooltip(new Tooltip(newClue.getText()+" ("+newClue.getType()+")"));
                            clueGlyphBox.getChildren().add(label);
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
            logicProblemGrid = new Grid(this,logicProblem.get());
            mainGroup.getChildren().clear();
            mainGroup.getChildren().add(logicProblemGrid);
            
            clueGlyphBox.getChildren().clear();
            
            //bind labels layer visibility to checkMenuItem
            logicProblemGrid.getChildren().get(1).visibleProperty().bind(hideLabelsMenuItem.selectedProperty().not());
            //bind relationships layer visibility to checkMenuItem        
            logicProblemGrid.getChildren().get(2).visibleProperty().bind(hideRelationshipsMenuItem.selectedProperty().not());
            
            
           //test
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
                    appTitle +" v."+appVersion+" -  "+logicProblem.get().titleProperty().getValue()+"*":
                    appTitle +" v."+appVersion+" -  "+logicProblem.get().titleProperty().getValue(),this.dirtyFileProperty));
//          
            for (Clue clue : logicProblem.get().getClues()){
//                Label label = new Label(Integer.toString(logicProblem.get().getClues().indexOf(clue)+1));
                Label label = new Label(Integer.toString(logicProblem.get().getFilteredClues().indexOf(clue)+1));
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
    
    private void addConstraintClue(ItemPair pair, Relationship relationship) {
        String clueString = new String(pair.first().getName() 
                + ((relationship.getValue()==Relationship.ValueType.VALUE_YES)?" is ":" is not ") 
                + pair.last().getName());
        System.out.println("clue added "+ clueString);
        logicProblem.get().getClues().add(new Clue(clueString,Clue.ClueType.CONSTRAINT));
        
    }
    
    public void saveFile(){
        
        fLogger.log(Level.INFO, "saveFile invoked");
        File file = new File("test.lpf");
        
        //remove constraints from clue list
        //better add to a list and then removing
        ArrayList<Clue> constraints = new ArrayList<Clue>();
        logicProblem.get().getClues().stream().filter(e -> e.getType() == Clue.ClueType.CONSTRAINT).forEach(e -> constraints.add(e));
        
        constraints.forEach(e -> logicProblem.get().getClues().remove(e));
        //add constraints to clue list
        logicProblem.get().getRelationshipTable().entrySet().stream().filter( e -> 
                    e.getValue().getValue() != Relationship.ValueType.VALUE_UNKNOWN && e.getValue().getLogic() == Relationship.LogicType.CONSTRAINT)
            .forEach(s -> 
                addConstraintClue(s.getKey(),s.getValue())
            );
            
        try {
                   
                    JAXBContext jaxbContext = JAXBContext.newInstance(LogicProblem.class);
                    Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

                    // output pretty printed
                    jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                    
//                    LogicProblem newProblem = DemoProblems.generateDemoProblem47();
                    jaxbMarshaller.marshal(logicProblem.get(), file);
                    
                    jaxbMarshaller.marshal(logicProblem.get(), System.out);
                    logicProblem.get().dirtyFileProperty().set(false);
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
