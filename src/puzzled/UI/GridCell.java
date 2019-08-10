/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.Optional;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import puzzled.Puzzled;
import puzzled.data.Constraint;
import puzzled.data.Relationship;
import puzzled.data.Relationship.ValueType;
import puzzled.exceptions.RelationshipConflictException;

/**
 *
 * @author phiv
 */
public class GridCell extends StackPane {
    private static final Logger fLogger =
        Logger.getLogger(Puzzled.class.getPackage().getName());
    
    private Relationship linkedRelationship;
    
    private Tooltip tooltip = new Tooltip();
    private BooleanProperty fileDirtyProperty;
    private StringProperty highlight = new SimpleStringProperty();
    

    public GridCell(int cellwidth, Relationship relationship, BooleanProperty arg_fileDirtyProperty) {
        Rectangle cellRectangle = new Rectangle(cellwidth, cellwidth, Color.TRANSPARENT);
        
        this.linkedRelationship = relationship;
        this.fileDirtyProperty = arg_fileDirtyProperty;
        
        this.highlight.bind(Bindings.createStringBinding(() -> linkedRelationship.getLogicType().toString(),linkedRelationship.logicTypeProperty()));
        
        linkedRelationship.explainProperty().addListener((e,oldValue,newValue) -> {
            if (newValue==true) {
                this.getStyleClass().add("highlight-PREDECESSOR");
            } else {
                FilteredList<String> styles = this.getStyleClass().filtered(g -> g.contains("highlight"));
                this.getStyleClass().remove(styles.get(0));
            }
        });
        
        
        //graphical representation of the cell
        //rectangle overlay for mouse events, all others elements are transparent to the mouse
        //VALUE_YES
        Circle circle = new Circle((float)cellwidth*2/5,Color.TRANSPARENT);
        //VALUE_NO
        Line line1 = new Line(5,5,cellwidth -5 ,cellwidth -5 );
        Line line2 = new Line(cellwidth -5 ,5,5,cellwidth -5 );
        
        circle.setMouseTransparent(true);
        circle.getStyleClass().add("o");
        //circle.relocate(5, 5);
        
        circle.visibleProperty().bind(linkedRelationship.valueProperty().isEqualTo(ValueType.VALUE_YES).and(linkedRelationship.appliedProperty()));
        
        //line1.setMouseTransparent(true);
        line1.getStyleClass().add("x");
        line1.setMouseTransparent(true);
        line2.getStyleClass().add("x");
        line2.setMouseTransparent(true);
        //xPane.getChildren().addAll(line1,line2);
        line1.visibleProperty().bind(linkedRelationship.valueProperty().isEqualTo(ValueType.VALUE_NO).and(linkedRelationship.appliedProperty()));
        line2.visibleProperty().bind(linkedRelationship.valueProperty().isEqualTo(ValueType.VALUE_NO).and(linkedRelationship.appliedProperty()));

        ContextMenu contextMenu = this.buildContextMenu();
        cellRectangle.setOnMouseClicked(e -> contextMenu.show(cellRectangle, Side.RIGHT, 0, 0)); 
        
        this.getStyleClass().add("gridCell");
        this.getChildren().addAll(cellRectangle,circle,line1,line2, createDecorator(cellwidth));
        setTooltipBinding(cellRectangle);
        
        linkedRelationship.centerXProperty().bind(Bindings.createDoubleBinding(
            ()->localToScene(circle.centerXProperty().get(),circle.centerYProperty().get()).getX(),
            circle.centerXProperty(),this.boundsInParentProperty(),this.scaleXProperty()));
        linkedRelationship.centerYProperty().bind(Bindings.createDoubleBinding(
            ()->localToScene(circle.centerXProperty().get(),circle.centerYProperty().get()).getY(),
            circle.centerYProperty(),this.boundsInParentProperty(),this.scaleXProperty()));     
    }

    
    public void setFalseConstraint() throws RelationshipConflictException {
        //this.valueProperty.set(ValueType.VALUE_NO);
//        fLogger.info("setting FALSE");
        try {
            //create a new Constraint object
            Constraint constraint = new Constraint(linkedRelationship.getItemPair(),Relationship.ValueType.VALUE_NO);
//            System.out.println("Constraint object created:\n:");
//            System.out.println(constraint);
            //needs to add this relationship to constraint table
            linkedRelationship.getParentLogicProblem().addConstraint(constraint);
            //set the relationship and set the Constraint as its predecessor
            linkedRelationship.setValue(ValueType.VALUE_NO, Relationship.LogicType.CONSTRAINT,true, constraint);
//            fLogger.info("setting FALSE");
//            linkedRelationship.getParentLogicProblem().setDirtyLogic(true);
           
            //constraintProperty.set(linkedRelationship.getParent().addConstraint(linkedRelationship));
//            setTooltipBinding(); //text binding changes after setting up a constraint
        } catch (Exception e) {
            fLogger.info("exception setting FALSE");
        }
    }
     
    
    public void setTrueConstraint() throws RelationshipConflictException {
        //this.valueProperty.set(ValueType.VALUE_YES);
//        fLogger.info("setting TRUE");
        try {
            
            //create a new Constraint object
            Constraint constraint = new Constraint(linkedRelationship.getItemPair(),Relationship.ValueType.VALUE_YES);
            //needs to add this relationship to constraint table
            linkedRelationship.getParentLogicProblem().addConstraint(constraint);
            //set the relationship and set the Constraint as its predecessor
            linkedRelationship.setValue(ValueType.VALUE_YES, Relationship.LogicType.CONSTRAINT,true, constraint);
            fLogger.info("setting TRUE");
//            linkedRelationship.getParentLogicProblem().setDirtyLogic(true);
            
//            setTooltipBinding(); //text binding changes after setting up a constraint
            //constraintProperty.set(linkedRelationship.getParent().addConstraint(linkedRelationship));
        } catch (Exception e) {
            fLogger.info("exception setting TRUE");
        }
        fileDirtyProperty.set(true);
    }
    
    private void setTooltipBinding(Rectangle cellRectangle) {
        tooltip.textProperty().bind(this.linkedRelationship.relationshipTextProperty());
        Tooltip.install(cellRectangle, tooltip);
    }
 
    public void reset() {
        linkedRelationship.valueProperty().set(ValueType.VALUE_UNKNOWN);
        fLogger.info("setting UNKNOWN");
        fileDirtyProperty.set(true);
    }
    

    private void showAnnotationDialog() {
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Annotation");
        dialog.setHeaderText("Please enter the annotation text for this relationship");

        dialog.setGraphic(new ImageView("icons/context-menus/annotate.png"));

        // Set the button types.
//        ButtonType okButton = new ButtonType("Ok", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Stage dialogStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(Puzzled.class.getResourceAsStream("/icons/puzzle_32.png")));

        // Create the username and password labels and fields.
        BorderPane bPane = new BorderPane();
//        bPane.setHgap(10);
//        bPane.setVgap(10);
        bPane.setPadding(new Insets(10, 30, 10, 30));

        String annotationString = linkedRelationship.getAnnotation();
        TextArea annotationText = new TextArea(annotationString);
        
        annotationText.setPromptText("Enter some text that can help you solve the puzzle....");
//        PasswordField password = new PasswordField();
//        password.setPromptText("Password");

//        grid.add(new Label("Annotation text:"), 0, 0);
        bPane.setCenter(annotationText);
//        grid.add(new Label("Password:"), 0, 1);
//        grid.add(password, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node okButton = dialog.getDialogPane().lookupButton(ButtonType.OK);
        Node cancelButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
        okButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        annotationText.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(bPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> cancelButton.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return annotationText.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(weirdness -> {
            linkedRelationship.setAnnotation(weirdness);
//            System.out.println("text=" + weirdness);
        });
    }
    
    private AnchorPane createDecorator(int cellwidth) {
        AnchorPane decoratorPane = new AnchorPane();
        decoratorPane.setMouseTransparent(true);
        
        Label decoratorLabel = new Label();
        decoratorLabel.setMaxSize(cellwidth/4,cellwidth/4);
        decoratorLabel.setMinSize(cellwidth/4,cellwidth/4);
        
//        System.out.println("gridcell setup--->" + linkedRelationship+ "\n");
        
        decoratorLabel.getStyleClass().add("decorator"); //generic shape information
//        decoratorLabel.idProperty().set("decorator-ANNOTATION");
        decoratorLabel.idProperty().bind(
                Bindings.when(linkedRelationship.annotationProperty().isEqualTo(""))
                        .then("decorator-NORMAL")
                        .otherwise("decorator-ANNOTATION"));
        
        decoratorPane.setTopAnchor(decoratorLabel, 1.0);
        decoratorPane.setRightAnchor(decoratorLabel, 1.0);
        decoratorPane.getChildren().add(decoratorLabel);

        return decoratorPane;
    }
    
    private ContextMenu buildContextMenu(){
        
        ContextMenu contextMenu = new ContextMenu();
                
        //building up menus
        MenuItem setFalseMenuItem = new MenuItem("Set as FALSE");
        //        <div>Icon made by <a href="http://www.amitjakhu.com" title="Amit Jakhu">Amit Jakhu</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>

        setFalseMenuItem.setGraphic(new ImageView("/icons/context-menus/x.png"));
        setFalseMenuItem.disableProperty().bind(linkedRelationship.valueProperty().isNotEqualTo(ValueType.VALUE_UNKNOWN).
                and(linkedRelationship.appliedProperty()));
        setFalseMenuItem.setOnAction(e -> {
            try {
                this.setFalseConstraint();
            } catch (RelationshipConflictException ex) {
//                Logger.getLogger(GridCell.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        MenuItem setTrueMenuItem = new MenuItem("Set as TRUE");
                
        setTrueMenuItem.setGraphic(new ImageView("/icons/context-menus/o.png"));
        //<div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        setTrueMenuItem.disableProperty().bind(linkedRelationship.valueProperty().isNotEqualTo(ValueType.VALUE_UNKNOWN).
                and(linkedRelationship.appliedProperty()));
//        item2.setDisable(true);
        setTrueMenuItem.setOnAction(e -> {
            try {
                this.setTrueConstraint();
            } catch (RelationshipConflictException ex) {
                //Logger.getLogger(GridCell.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        MenuItem annotateMenuItem = new MenuItem("Annotate");
        annotateMenuItem.setGraphic(new ImageView("/icons/context-menus/annotate.png"));
        //<div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
//        annotateMenuItem.disableProperty().bind(
//                linkedRelationship.valueProperty().isEqualTo(Relationship.ValueType.VALUE_UNKNOWN).or(
//                        linkedRelationship.logicTypeProperty().isNotEqualTo(Relationship.LogicType.CONSTRAINT))); //grey out item unless it has been set as a constraint
        annotateMenuItem.setOnAction(e -> {
//           System.out.println("linked relationship's logicType:" + linkedRelationship.logicTypeProperty().get());
           this.showAnnotationDialog();
//           setTooltipBinding(); //text binding changes after changing the annotation text
//           linkedRelationship.getPredecessorConstraint().ifPresent(constraint -> constraint.setAnnotation("test me now"));
           });
        
        
        MenuItem clearMenuItem = new MenuItem("Clear relationship");
        clearMenuItem.setGraphic(new ImageView("/icons/context-menus/clear.png"));
        //<div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        clearMenuItem.disableProperty().bind(Bindings.or(
                linkedRelationship.valueProperty().isEqualTo(ValueType.VALUE_UNKNOWN), 
                linkedRelationship.logicTypeProperty().isNotEqualTo(Relationship.LogicType.CONSTRAINT)));
        clearMenuItem.setOnAction(e -> {
            this.reset();
            linkedRelationship.clearInvestigate();
        });
        
        
        MenuItem explainMenuItem = new MenuItem("Explain");
        explainMenuItem.setGraphic(new ImageView("/icons/context-menus/investigate.png"));
        //<div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        explainMenuItem.disableProperty().bind(Bindings.or(linkedRelationship.valueProperty().isEqualTo(ValueType.VALUE_UNKNOWN),
                linkedRelationship.explainProperty()));
        explainMenuItem.setOnAction(e -> {
           System.out.println("about to draw a special line with funky style: "+highlight.get());
           linkedRelationship.clearInvestigate();
           linkedRelationship.explainProperty().set(true);
           this.getStyleClass().remove("highlight-PREDECESSOR"); //undoing previous
           this.getStyleClass().add("highlight-"+highlight.get()); //should override the one set by the property listener
//           System.out.println(this.localToParent(this.boundsInParentProperty().get()).getMinX());
//           System.out.println(this.localToParent(this.boundsInParentProperty().get()).getMaxX());
//           System.out.println(this.localToParent(this.boundsInParentProperty().get()).getMinY());
//           System.out.println(this.localToParent(this.boundsInParentProperty().get()).getMaxY());
        });
        
        MenuItem clearInvestigateMenuItem = new MenuItem("Clear explanation");
        clearInvestigateMenuItem.setGraphic(new ImageView("/icons/context-menus/noinvestigate.png"));
        //<div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        clearInvestigateMenuItem.disableProperty().bind(linkedRelationship.explainProperty().not());
        clearInvestigateMenuItem.setOnAction(e -> {
//           System.out.println("about to draw a special line with funky style: "+highlight.get());
           linkedRelationship.clearInvestigate();
//           this.explainProperty.set(true);
        });
        
        contextMenu.getItems().addAll(setFalseMenuItem,setTrueMenuItem, clearMenuItem, annotateMenuItem, explainMenuItem, clearInvestigateMenuItem);
        return contextMenu;
    }
}
