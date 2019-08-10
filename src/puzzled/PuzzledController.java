/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.control.MenuButton;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javax.xml.bind.JAXBException;
import org.controlsfx.control.HiddenSidesPane;
import org.controlsfx.control.NotificationPane;
import org.fxmisc.flowless.Cell;
import org.fxmisc.flowless.VirtualFlow;
import puzzled.UI.Grid;
import puzzled.UI.SlideOutPane;
import puzzled.data.Clue;
import static puzzled.data.Clue.labelGenerator;
import puzzled.data.DistinctMappingList;
import puzzled.data.LogicProblem;
import puzzled.data.Relationship;
import puzzled.processor.Parser;
import puzzled.processor.Processor;

/**
 *
 * @author phiv
 */
public class PuzzledController implements Initializable {
    
    private static double zoomFactor = 1.3;
    private static double maxZoom = 2.3;
    private static double minZoom = 0.4;
    private static int notificationTimer = 3000;
    private LongProperty pendingRelationshipsCountProperty = new SimpleLongProperty(0);
    //tracks the number of Relationships discovered by the Processor that have not
    //yet been set (useful when automatic processing is disabled)
    
    
    private Puzzled mainApplication;
   
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

    @FXML private Parent root;
    @FXML private BorderPane bPane;
    @FXML private TabPane tPane;
    @FXML private HBox clueMajorGlyphBox;
    @FXML private NotificationPane nPane;
    @FXML private Button addClueButton;
    @FXML private TextField clueText;
    @FXML private AnchorPane mainGrid;
    @FXML private StackPane mainStack;
    @FXML private ScrollPane mainScroll;
    @FXML private Group mainGroup;
    
    @FXML private MenuButton nextClueNumberMenuButton;
    @FXML private MenuItem openMenuItem;    
    @FXML private MenuItem loadCluesMenuItem;
    @FXML private MenuItem saveMenuItem;
    @FXML private MenuItem closeMenuItem;
    @FXML private CheckMenuItem automaticProcessingMenuItem;
    @FXML private MenuItem saveAsMenuItem;
    @FXML private MenuItem printMenuItem;
    @FXML private MenuItem zoomInMenuItem;
    @FXML private MenuItem zoomOutMenuItem;
    @FXML private MenuItem nextMajorMenuItem;
    @FXML private MenuItem nextMinorMenuItem;
    @FXML private MenuItem nextSubMenuItem;
    @FXML private CheckMenuItem hideToolbarMenuItem;
    @FXML private MenuItem propertiesMenuItem;
    @FXML private CheckMenuItem hideLabelsMenuItem;
    @FXML private CheckMenuItem hideRelationshipsMenuItem;
    @FXML private CheckMenuItem hideClueEngineMenuItem;
    @FXML private ToolBar toolbar;
    @FXML private Button saveButton;
    @FXML private Button resetButton;
    @FXML private Button undoButton;
    @FXML private Button processButton;
    @FXML private Button zoomOutButton;
    @FXML private Button zoomInButton;    
    @FXML private HiddenSidesPane pane;

    //connects with nested controller?
    @FXML private Parent clueTab;
    @FXML private ClueTabController clueTabController; // $embeddedElement+Controller
    
//    @FXML
    private SlideOutPane soPane;

//    @FXML
//    private void handleMouseClicked(MouseEvent event) {
//        pane.setPinnedSide((pane.getPinnedSide() != null)?null:Side.RIGHT);
//    }
    
    @FXML private VBox clueEngineVBox;
  
    ObjectProperty<LogicProblem> logicProblemProperty = new SimpleObjectProperty<LogicProblem>();
    DoubleProperty scaleProperty = new SimpleDoubleProperty();
    private ObservableList<Clue> clues; //?
    
    //private HashMap<Pair<Item,Item>,Relationship> relationships;

    private BooleanProperty dirtyLogicProperty = new SimpleBooleanProperty(); 
    private BooleanProperty dirtyFileProperty = new SimpleBooleanProperty();
    
    private static final Logger fLogger =
    Logger.getLogger(Puzzled.class.getPackage().getName());
    
    Grid logicProblemGrid;

    //private String appTitle;
    //private String appVersion;

    private StringProperty appTitleProperty = new SimpleStringProperty();
    private StringProperty filenameProperty = new SimpleStringProperty(null); //used to store loaded file name
    
    @FXML
    private void handleAutomaticProcessingAction(ActionEvent event) {
        // Button was clicked, do something...
        System.out.println("automatic processing toggled");
        //trigger a dirty logic to force processing
        if (this.automaticProcessingMenuItem.isSelected() && this.getLogicProblem() != null) {
            this.getLogicProblem().setDirtyLogic(true); //this should trigger the changeListener
        }
    }
    
    @FXML
    private void loadMe(ActionEvent event) {
        String filename = new String("resources/samples/problem47.lpd");
        try {
            this.logicProblemProperty.set(PuzzledFileIO.loadProblem(filename));
            this.filenameProperty.set(filename);
            initializeProblem();
            notify(PuzzledController.WarningType.SUCCESS, "Problem file "+filename+" loaded successfully!");
        } catch (Exception e) {
            notify(PuzzledController.WarningType.WARNING, "Unable to load problem file "+filename+ "!");
            e.printStackTrace();
        }
    }
    
    
    public void setupTitleBinding(StringProperty puzzledTitleProperty) {
        puzzledTitleProperty.bind(this.appTitleProperty);
        //this.appTitleProperty.set(banner+" - "+version);
    }
    
    @FXML
    private void zoomInAction(ActionEvent event) {
        
        if (logicProblemProperty.get().getScale() <= maxZoom) {
            logicProblemProperty.get().setScale(logicProblemProperty.get().getScale()*zoomFactor);
        } else {
            notify(WarningType.WARNING, "Maximum zoom level reached!");
        }
        fLogger.log(Level.INFO, "scale:"+logicProblemProperty.get().getScale());
    }    

    @FXML
    private void processAction(ActionEvent event) {
        fLogger.log(Level.INFO, "process action invoked");
//        Processor.process(this.getLogicProblem(), this.pendingRelationshipsCounterProperty(),this.isAutomaticProcessing(), true);
        //go through RelationshipTable and apply all
        for (Relationship rel : this.getLogicProblem().getRelationshipTable().values()) {
            rel.apply(true);
        }
    }    

    @FXML
    private void zoomOutAction(ActionEvent event) {
        if (this.getLogicProblem().getScale() >= minZoom) {
            this.getLogicProblem().setScale(logicProblemProperty.get().getScale()/zoomFactor);
        } else {
            notify(WarningType.WARNING,"Minimum zoom level reached!");
        }
        fLogger.log(Level.INFO,"scale:"+logicProblemProperty.get().getScale());
    }
    
    @FXML
    private void closeAction(ActionEvent event) {
        //
        fLogger.log(Level.INFO,"close action");
    }

    @FXML
    private void print(ActionEvent event) {
        Printer printer = Printer.getDefaultPrinter();
        Double currentScale = logicProblemProperty.get().getScale();
        logicProblemProperty.get().setScale(1);
        
        PageLayout pageLayout = printer.createPageLayout(Paper.NA_LETTER, PageOrientation.LANDSCAPE, Printer.MarginType.EQUAL_OPPOSITES);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(pageLayout.getPrintableWidth(), pageLayout.getPrintableHeight());
        borderPane.setMaxSize(pageLayout.getPrintableWidth(), pageLayout.getPrintableHeight());
        borderPane.setMinSize(pageLayout.getPrintableWidth(), pageLayout.getPrintableHeight());
        
        Label headerLabel = new Label(this.hideLabelsMenuItem.isSelected()?"":logicProblemProperty.get().getTitle());
        Label sourceLabel = new Label(this.hideLabelsMenuItem.isSelected()?"":logicProblemProperty.get().getSource());
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
        int numcats = this.logicProblemProperty.get().getNumCategories()-1;
        int numcells = numcats * this.logicProblemProperty.get().getNumItems() + 1;
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
        
        if (printerJob != null && printerJob.showPrintDialog(null)) {
            printerJob.getJobSettings().setPageLayout(pageLayout);
            
            boolean success = printerJob.printPage(borderPane);
            if (success) {
                printerJob.endJob();
                notify(WarningType.SUCCESS,"Printing successful!");
            } else {
                notify(WarningType.WARNING,"Printing not successful!");
            }
            //retore problem scale
            logicProblemProperty.get().setScale(currentScale);
            //re-attach to scene
            mainGroup.getChildren().add(logicProblemGrid);
        }
    }
        
    
    @FXML
    private void addClueButtonClick(MouseEvent event) {
    //private void addClueButtonAction(ActionEvent event) {
        //Clue newClue = new Clue(clueText.getText());
        //Parse first, with modifier, which will add the clue to the list
        Parser.parse(logicProblemProperty.get(), clueText.getText(), event.isControlDown(), event.isAltDown());
        
//        this.getLogicProblem().setDirtyFile(true);
        //logicProblem.get().getNumberedClueList().addMajorClue(newClue); //this invokes clue parsing
        clueText.clear();
//        this.getLogicProblem().setDirtyLogic(true);
//        clueTabController.refreshTable();

        //how is the glyph generation going to work if we no longer create the clue here?
        //clueGlyphBox.getChildren().add(generateClueGlyph(newClue));
        
        //notify(WarningType.SUCCESS,"Clue "+logicProblem.get().getFilteredClues().size()+" was just added!");
    }
    
    
    @FXML
    public void clueTextKeyListener(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER) {
            Parser.parse(logicProblemProperty.get(), clueText.getText(), event.isControlDown(), event.isAltDown());
            //logicProblem.get().getNumberedClueList().addMajorClue(newClue); //this invokes clue parsing
            clueText.clear();
//            this.getLogicProblem().setDirtyLogic(true);
            //how is the glyph generation going to work if we no longer create the clue here?
            //clueGlyphBox.getChildren().add(generateClueGlyph(newClue));
//            logicProblem.get().setDirtyFile(true);
        }
     }
    
    @FXML
    private void nextMajorAction(ActionEvent event) {
        this.getLogicProblem().getNumberedClueList().setNextClueNumber(
                    this.getLogicProblem().getNumberedClueList().getLastClueNumber().getNextMajorClueNumber());
    }

    @FXML
    private void nextMinorAction(ActionEvent event) {
        this.getLogicProblem().getNumberedClueList().setNextClueNumber(
                    this.getLogicProblem().getNumberedClueList().getLastClueNumber().getNextMinorClueNumber());
    }

    @FXML
    private void nextSubAction(ActionEvent event) {
        this.getLogicProblem().getNumberedClueList().setNextClueNumber(
                    this.getLogicProblem().getNumberedClueList().getLastClueNumber().getNextSubClueNumber());

    }
    
    @FXML
    private void quitAction(ActionEvent event) {
        this.quitProcedure();
    }
    
    //can be called from a menu ActionEvent or a WindowEvent (see Puzzled.java)
    public void quitProcedure() {
        //make sure logicProblem is not dirty!
        //save UI preferences
        fLogger.log(Level.INFO,"quit action");
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        prefs.putBoolean("AUTOMATIC_PROCESSING",this.automaticProcessingMenuItem.isSelected());
        Platform.exit(); 
    }
    
    public void notify(WarningType type, String text) {
        nPane.setGraphic(new ImageView(new Image("/icons/notification-pane/"+type.getImageName())));  
        nPane.setText(text);
        nPane.show();
      }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupNotifier(); //configures the notification pane slide down
        mainScroll.setPannable(true);
        
//        this.dirtyLogicProperty.addListener(logicChangeListener);

        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        this.automaticProcessingMenuItem.setSelected(prefs.getBoolean("AUTOMATIC_PROCESSING",true));
        
//        this.automaticProcessingMenuItem.
        
        clueText.disableProperty().bind(this.logicProblemProperty.isNull());
        toolbar.managedProperty().bind(this.hideToolbarMenuItem.selectedProperty().not());

        addClueButton.disableProperty().bind(this.logicProblemProperty.isNull().or(clueText.textProperty().isEmpty()));
//        automaticProcessingMenuItem.disableProperty().bind(this.logicProblemProperty.isNull());
        processButton.disableProperty().bind(this.logicProblemProperty.isNull().or(
                        this.automaticProcessingMenuItem.selectedProperty().or(
                        this.pendingRelationshipsCountProperty.isEqualTo(0))));
        Tooltip processButtonTooltip = new Tooltip();
        processButtonTooltip.textProperty().bind(Bindings.createStringBinding(() -> "There are "+ this.pendingRelationshipsCountProperty.get()+" newly discovered relationships",
                this.pendingRelationshipsCountProperty));
        processButton.setTooltip(processButtonTooltip);
        resetButton.disableProperty().bind(this.logicProblemProperty.isNull());
        undoButton.disableProperty().bind(this.logicProblemProperty.isNull());


        saveMenuItem.disableProperty().bind(Bindings.or(Bindings.and(this.logicProblemProperty.isNull(),this.dirtyFileProperty.not()),this.filenameProperty.isNull()));
        saveAsMenuItem.disableProperty().bind(Bindings.and(this.logicProblemProperty.isNull(),this.dirtyFileProperty.not()));
        loadCluesMenuItem.disableProperty().bind(this.logicProblemProperty.isNull());
        saveButton.disableProperty().bind(Bindings.or(Bindings.and(this.logicProblemProperty.isNull(),this.dirtyFileProperty.not()),this.filenameProperty.isNull()));
        propertiesMenuItem.disableProperty().bind(this.logicProblemProperty.isNull());
        printMenuItem.disableProperty().bind(this.logicProblemProperty.isNull());
        
        clueEngineVBox.managedProperty().bind(this.hideClueEngineMenuItem.selectedProperty().not());
        clueEngineVBox.visibleProperty().bind(this.hideClueEngineMenuItem.selectedProperty().not());
        
        zoomInMenuItem.disableProperty().bind(Bindings.or(this.logicProblemProperty.isNull(),this.scaleProperty.greaterThanOrEqualTo(maxZoom)));
        zoomOutMenuItem.disableProperty().bind(Bindings.or(this.logicProblemProperty.isNull(),this.scaleProperty.lessThanOrEqualTo(minZoom)));
        zoomInButton.disableProperty().bind(Bindings.or(this.logicProblemProperty.isNull(),this.scaleProperty.greaterThanOrEqualTo(maxZoom)));
        zoomOutButton.disableProperty().bind(Bindings.or(this.logicProblemProperty.isNull(),this.scaleProperty.lessThanOrEqualTo(minZoom)));
        
        nextMajorMenuItem.disableProperty().bind(this.logicProblemProperty.isNull());
        nextMinorMenuItem.disableProperty().bind(this.logicProblemProperty.isNull());
        nextSubMenuItem.disableProperty().bind(this.logicProblemProperty.isNull());
        
        //no binding required here, the values are static properties
        appTitleProperty.set(Puzzled.banner +" v."+Puzzled.version);
         
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
        
        clueTabController.setParentController(this);
        mainGrid.sceneProperty().addListener((observable, oldvalue, newvalue) -> {
            if (newvalue!=null) {
                setupDragNDrop(mainGrid.getScene());
//                mainGrid.getScene().getStylesheets().add(PuzzledController.class.getResource("Puzzled.css").toExternalForm());
            }
        });
        
        
        //mangle with borderPane to create the slide out using ControlsFX HiddenSidesPane
        //whilst this can (and probably should) be added within Scene Builder
        //it becomes impossible to use HiddenSidesPane as a parent
        //manually editing the FXML file works, but breaks Scene Builder functionality
        HiddenSidesPane hsPane = new HiddenSidesPane();
        soPane = new SlideOutPane(hsPane);
        //steal tPane from scene
        hsPane.setContent(tPane);
//        System.out.println("trigger distance:"+hsPane.getTriggerDistance());
        hsPane.setTriggerDistance(3);
        hsPane.setRight(soPane);
        //inject into scene
        bPane.setCenter(hsPane);
        
    //    relationships = new HashMap<Pair<Item,Item>,Relationship>(logicProblem.getNumItems()*logicProblem.getNumItems()*(logicProblem.getNumCategories()-1)*logicProblem.getNumCategories()/2);
    }    
    
    
    public Logger getLogger(){
        return fLogger;
    }
    
    public LogicProblem getLogicProblem() {
        return this.logicProblemProperty.get();
    }
    
    public ObjectProperty<LogicProblem> logicProblemProperty(){
        return this.logicProblemProperty;
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
                    String extension = file.getName().replaceAll("^.*\\.([^.]+)$", "$1");
                    if (extension.equalsIgnoreCase("lpf") || extension.equalsIgnoreCase("lpd")) {
                        try {
                            //only load clues when there is a non-null LogicProblem
                            this.logicProblemProperty.set(PuzzledFileIO.loadProblem(Paths.get(file.toURI()).toString()));
                            notify(PuzzledController.WarningType.SUCCESS, "Problem file "+file.getName()+" loaded successfully!");
                            this.initializeProblem();
                        } catch (Exception ex) {
                            notify(PuzzledController.WarningType.WARNING, "Unable to load problem file "+file.getName()+ "!");
                            ex.printStackTrace();
                        }
                    } else if (extension.equalsIgnoreCase("lpc") && this.logicProblemProperty.get() != null) {
                        try {
                            //only load clues when there is a non-null LogicProblem
                            PuzzledFileIO.loadClues(file,this.getLogicProblem());
                            notify(PuzzledController.WarningType.SUCCESS, "Problem clues file "+file.getName()+" loaded successfully!");
                        } catch (Exception ex) {
                            notify(PuzzledController.WarningType.WARNING, "Unable to load problem clues file "+file.getName()+ "!");
                            ex.printStackTrace();
                        }
                    }
                });
            }

            event.setDropCompleted(true);
            event.consume();
           
        });
    }

    @FXML
    public void resetAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Reset Logic Problem");
        alert.initStyle(StageStyle.UTILITY);
        alert.setHeaderText(null);
        alert.setContentText("Resetting the Logic Problem will clear all relationships and clues\nDo you wish to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            this.getLogicProblem().getNumberedClueList().clear();
            this.initializeProblem();
            notify(WarningType.SUCCESS, "Logic problem reset successfully!");
        }
    }
    
    //setup or re-set bindings once problem is loaded
    private void initializeProblem(){
        this.getLogicProblem().initializeRelationshipTable();
        this.dirtyLogicProperty.bind(this.getLogicProblem().dirtyLogicProperty());
        this.dirtyLogicProperty.addListener((arg, oldVal, newVal)  -> {
            //prevent recursion, dirtyLogicProperty changes will be confined to the LogicProblem object
            //and not propagate to the PuzzledController
//            fLogger.log(Level.INFO, "process action triggered through dirtyLogic change propagation");
//            System.out.println("controller's dirtyLogic value is "+this.dirtyLogicProperty.get());
//            this.getLogicProblem().setDirtyLogic(false);
//            System.out.println("unbinding");
            this.dirtyLogicProperty.unbind();
            if (newVal == true) Processor.process(this.getLogicProblem(), this.isAutomaticProcessing());
            //rebinding may trigger a change!
            //System.out.println("rebinding -> triggering another change?");
            
            this.dirtyLogicProperty.bind(this.getLogicProblem().dirtyLogicProperty());
                });
            
            pendingRelationshipsCountProperty.bind(
                Bindings.createLongBinding(
                    () -> this.getLogicProblem().getRelationshipTable().values().stream().filter(rel -> !rel.isApplied()).count(),
                    this.getLogicProblem().getRelationshipTable().values().stream().map(rel -> rel.appliedProperty()).toArray(SimpleBooleanProperty[]::new)
                ));
//        List<BooleanProperty> appliedRelationshipList = new ArrayList<>();
//        appliedRelationshipList = this.getLogicProblem().getRelationshipTable().values().forEach(l -> l.appliedProperty()).collect();
//        for (Relationship rel : this.getLogicProblem().getRelationshipTable().values()) {
//            relationshipList
//        }
        
//        this.getLogicProblem().getRelationshipTable().values().toArray()
//        createIntegerbinding(filter(thosenotapplied).count(),relationshiplist.toArray(new [locations.size()]));
        
        
        logicProblemGrid = new Grid(this,logicProblemProperty.get());
        mainGroup.getChildren().clear();
        mainGroup.getChildren().add(logicProblemGrid);
        this.clues = this.getLogicProblem().getNumberedClueList().getObservableClueList();
        
        //setup data source for the Clue Table ?!
//            clues = FXCollections.observableList(logicProblem.get().getNumberedClueList());
        clueTabController.setData(this.clues);

        //bind labels layer visibility to checkMenuItem
        logicProblemGrid.getChildren().get(1).visibleProperty().bind(hideLabelsMenuItem.selectedProperty().not());
        //bind relationships layer visibility to checkMenuItem        
        logicProblemGrid.getChildren().get(2).visibleProperty().bind(hideRelationshipsMenuItem.selectedProperty().not());

//        this.dirtyLogicProperty.bind(logicProblemProperty.get().dirtyLogicProperty());
        this.dirtyFileProperty.bind(logicProblemProperty.get().dirtyFileProperty());
        this.scaleProperty.bind(logicProblemProperty.get().scaleProperty());
//            this.titleLabel.textProperty().bind(logicProblem.get().getTitleProperty());
//            soPane.textProperty().bind(logicProblem.get().problemTextProperty());
        soPane.textProperty().bindBidirectional(logicProblemProperty.get().problemTextProperty());


        //logicProblem.get().getClues().stream().filter(e -> e.getType() != Clue.ClueType.CONSTRAINT).count()
        //                    .stream().filter(e -> e.getType() != Clue.ClueType.CONSTRAINT).collect(collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)))

        //nextClueNumber.textProperty().bind(logicProblem.get().getNextClueNumber().concat("->"));

        this.appTitleProperty.bind(Bindings.createStringBinding(() -> this.getLogicProblem().dirtyFileProperty().get()?
                Puzzled.banner +" v."+Puzzled.version+" -  "+this.getLogicProblem().getTitle()+(this.filenameProperty.get()==null?"": "   ("+this.filenameProperty.get()+") *"):
                Puzzled.banner +" v."+Puzzled.version+" -  "+this.getLogicProblem().getTitle()+(this.filenameProperty.get()==null?"": "   ("+this.filenameProperty.get()+")"),
                this.dirtyFileProperty, this.filenameProperty));
//          
        //binds to the next ClueNumber string property, but adds ->
        nextClueNumberMenuButton.textProperty().bind(Bindings.createStringBinding(() -> 
                this.getLogicProblem().getNumberedClueList().
                        getNextClueNumber().toString()+" ->",
                this.getLogicProblem().getNumberedClueList().nextClueNumberProperty()));


        //ClueNumber label context menu bindings (disable properties and text properties)
        nextMajorMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                if (this.clues.isEmpty()) {
                    return true; //disable menu item when there are no clues
                } else {
                    return this.getLogicProblem().getNumberedClueList().getNextClueNumber().equals(
                    this.getLogicProblem().getNumberedClueList().getLastClueNumber().getNextMajorClueNumber());
                }
                //both objects are necessary because we need to test a match with the nextClueNumber
            }, this.clues, 
            this.getLogicProblem().getNumberedClueList().nextClueNumberProperty()));

        nextMajorMenuItem.textProperty().bind(Bindings.createStringBinding(() -> {
                if (this.clues.isEmpty()) {
                    return "Change to next major";
                } else {
                    return "Change to " + 
                    this.getLogicProblem().getNumberedClueList().getLastClueNumber().getNextMajorClueNumber();
                }
            }, this.clues, 
            this.getLogicProblem().getNumberedClueList().nextClueNumberProperty()));

        nextMinorMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                if (this.clues.isEmpty()) {
                    return true; //disable menu item when there are no clues
                } else {
                    return this.getLogicProblem().getNumberedClueList().getNextClueNumber().equals(
                    this.getLogicProblem().getNumberedClueList().getLastClueNumber().getNextMinorClueNumber());
                }
                //both objects are necessary because we need to test a match with the nextClueNumber
            }, this.clues,
            this.getLogicProblem().getNumberedClueList().nextClueNumberProperty(), 
            this.getLogicProblem().getNumberedClueList().nextClueNumberProperty()));       
        nextMinorMenuItem.textProperty().bind(Bindings.createStringBinding(() -> {
                if (this.clues.isEmpty()) {
                    return "Change to next minor";
                } else {
                    return "Change to " + 
                    this.getLogicProblem().getNumberedClueList().getLastClueNumber().getNextMinorClueNumber();
                }
            }, this.clues, 
            this.getLogicProblem().getNumberedClueList().nextClueNumberProperty()));

        nextSubMenuItem.disableProperty().bind(Bindings.createBooleanBinding(() -> {
                if (this.clues.isEmpty()) {
                    return true; //disable menu item when there are no clues
                } else {
                    return this.getLogicProblem().getNumberedClueList().getNextClueNumber().equals(
                    this.getLogicProblem().getNumberedClueList().getLastClueNumber().getNextSubClueNumber());
                }
                //both objects are necessary because we need to test a match with the nextClueNumber
            }, this.clues,
            this.getLogicProblem().getNumberedClueList().nextClueNumberProperty()));        
        nextSubMenuItem.textProperty().bind(Bindings.createStringBinding(() -> {
                if (this.clues.isEmpty()) {
                    return "Change to next sub";
                } else {
                    return "Change to " + 
                    this.getLogicProblem().getNumberedClueList().getLastClueNumber().getNextSubClueNumber();
                }
            }, this.clues, 
            this.getLogicProblem().getNumberedClueList().nextClueNumberProperty()));
        //clues have already been added to the problem and parsed when loading the file
        //this is only to draw the glyph
        //for (Clue clue : logicProblem.get().getFilteredClues()) clueGlyphBox.getChildren().add(generateClueGlyph(clue));
//                Label label = new Label(Integer.toString(logicProblem.get().getClues().indexOf(clue)+1));
//                Label label = new Label(Integer.toString(logicProblem.get().getFilteredClues().indexOf(clue)+1));
//                label.setTooltip(new Tooltip(clue.getText()+" ("+clue.getType()+")"));
//                
//                label.getStyleClass().add("clue_"+clue.getType());

        Function<Clue, Integer> cluemapper = c -> c.clueNumberProperty().get().getMajor();
        ObservableList<Integer> mappedcluemajors = new DistinctMappingList<>(this.clues, cluemapper);
        //https://stackoverflow.com/questions/43890528/observablelist-bind-content-with-elements-conversion

//        ListView<Integer> cluemajorlist = new ListView<>(mappedcluemajors.sorted());

        //https://github.com/FXMisc/Flowless
        VirtualFlow<Integer,Cell<Integer,Label>> vflow = 
                    VirtualFlow.createHorizontal(
                            mappedcluemajors.sorted(),
                            element -> org.fxmisc.flowless.Cell.wrapNode(labelGenerator(this.clues,element)),
                            VirtualFlow.Gravity.REAR);
//        vflow.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE );
        clueMajorGlyphBox.getChildren().addAll(vflow);
        HBox.setHgrow(vflow, Priority.ALWAYS);
    }
    
    private Label generateClueGlyph(Clue clue){
        Label label = new Label(clue.clueNumberProperty().get().clueNumberStringProperty().get());
        label.setTooltip(new Tooltip(clue.getClueText()+" ("+clue.getClueType()+")"));
        label.getStyleClass().add("clue_"+clue.getClueType());
        return label;
    }
    
    @FXML
    public void openAction() {
        fLogger.log(Level.INFO, "opening file invoked");
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String lastDir = prefs.get("LAST_DIR","");
        
        FileChooser fileChooser = new FileChooser();
        File lastDirFile = new File(lastDir);
        if (lastDirFile.isDirectory() && lastDirFile.canRead()) fileChooser.setInitialDirectory(lastDirFile);
        fileChooser.setTitle("Open Logic Problem File");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Logic Problem Files", "*.lpf"),
                new ExtensionFilter("Logic Problem Definition", "*.lpd"));//,
        Stage mainStage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            fLogger.log(Level.INFO, "file "+selectedFile.getName());
            try {
                this.logicProblemProperty.set(PuzzledFileIO.loadProblem(selectedFile));
                notify(PuzzledController.WarningType.WARNING, "Logic problem file "+selectedFile.getName()+ " loaded successfully!");
                this.filenameProperty.set(selectedFile.getName());
            } catch (Exception e) {
                notify(PuzzledController.WarningType.WARNING, "Unable to load problem file "+selectedFile.getName()+ "!");
                this.filenameProperty.set(null);
            }
            prefs.put("LAST_DIR", selectedFile.getParentFile().getAbsolutePath());
//            System.out.println("preference last dir should be:"+selectedFile.getParentFile().getAbsolutePath());
        } else {
            fLogger.log(Level.INFO, "no file selected");
        }
        
    }
    
    @FXML
    private void loadCluesAction(){
        //through bound properties, this action cannot be invoked if no logic problem is active
        fLogger.log(Level.INFO, "loading clues invoked");
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String lastDir = prefs.get("LAST_DIR","");
        
        FileChooser fileChooser = new FileChooser();
        File lastDirFile = new File(lastDir);
        if (lastDirFile.isDirectory() && lastDirFile.canRead()) fileChooser.setInitialDirectory(lastDirFile);
        fileChooser.setTitle("Loading Clues File");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Logic Problem Clues", "*.lpc"));
        Stage mainStage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(mainStage);
        if (selectedFile != null) {
            fLogger.log(Level.INFO, "file "+selectedFile.getName());
            try {
                //clueGlyphBox.getChildren().add(generateClueGlyph(newClue));
                PuzzledFileIO.loadClues(selectedFile,this.getLogicProblem());
                notify(PuzzledController.WarningType.SUCCESS, "Problem clues file "+selectedFile.getName()+" loaded successfully!");
            } catch (Exception e) {
                notify(PuzzledController.WarningType.WARNING, "Unable to load problem clues file "+selectedFile.getName()+ "!");
            }
            prefs.put("LAST_DIR", selectedFile.getParentFile().getAbsolutePath());
        } else {
            fLogger.log(Level.INFO, "no file selected");
        }
    }
    
    public void setMainApp(Puzzled parent) {
        this.mainApplication = parent;
    }
    
    
    @FXML
    public void saveAction(ActionEvent event) {
        try {
            PuzzledFileIO.saveFile(this.filenameProperty.get(), this.getLogicProblem());
            notify(PuzzledController.WarningType.SUCCESS, "File "+this.filenameProperty.get()+" saved successfully!");
            this.dirtyFileProperty.set(false);
        } catch (JAXBException e) {
            notify(WarningType.WARNING, "Unable to save file "+this.filenameProperty.get()+"!");
            e.printStackTrace();
        }
    }
    
    @FXML
    public void saveAsAction(ActionEvent event) {
        fLogger.log(Level.INFO, "save as action invoked");
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String lastDir = prefs.get("LAST_DIR","");
//        System.out.println("preference LAST_DIR is:" + lastDir);
        
        FileChooser fileChooser = new FileChooser();
        File lastDirFile = new File(lastDir);
        if (lastDirFile.isDirectory() && lastDirFile.canRead()) fileChooser.setInitialDirectory(lastDirFile);
        fileChooser.setTitle("Save Logic Problem File as");
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Logic Problem Files", "*.lpf"));

        Stage mainStage = (Stage) root.getScene().getWindow();
        File selectedFile = fileChooser.showSaveDialog(mainStage);
        if (selectedFile != null) {
            fLogger.log(Level.INFO, "file "+selectedFile.getName());
            try {
                PuzzledFileIO.saveFile(selectedFile.getName(), this.getLogicProblem());
                notify(PuzzledController.WarningType.SUCCESS, "File "+selectedFile.getName()+" saved successfully!");
                this.dirtyFileProperty.set(false);
                prefs.put("LAST_DIR", selectedFile.getParentFile().getAbsolutePath());
                this.filenameProperty.set(selectedFile.getName());
            } catch (JAXBException e) {
                notify(WarningType.WARNING, "Unable to save file "+this.filenameProperty.get()+"!");
                e.printStackTrace();
            }
        } else {
            fLogger.log(Level.INFO, "no file selected");
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
    
//    public IntegerProperty pendingRelationshipsCountProperty() {
//        return this.pendingRelationshipsCountProperty;
//    }

    public boolean isAutomaticProcessing() {
            return this.automaticProcessingMenuItem.isSelected();
    }
}
