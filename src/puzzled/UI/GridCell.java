/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.UI;

import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import puzzled.Puzzled;
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
    
    private ObjectProperty<ValueType> valueProperty = new SimpleObjectProperty<>(this, "value" , ValueType.VALUE_UNKNOWN);
    
    private ContextMenu contextMenu = new ContextMenu();  
//    private CirclePopupMenu contextMenu = new CirclePopupMenu(this,MouseButton.SECONDARY);;
    //private Pane xPane = new Pane();
    
    private Relationship linkedRelationship;
    private BooleanProperty fileDirtyProperty;
    private StringProperty highlight = new SimpleStringProperty();
    //private StringProperty relationshipText = new SimpleStringProperty();
    private BooleanProperty investigateProperty = new SimpleBooleanProperty(false);
    private ObjectProperty<Relationship.LogicType> logicTypeProperty = new SimpleObjectProperty<Relationship.LogicType>();

    public GridCell(int cellwidth, Relationship relationship, BooleanProperty arg_fileDirtyProperty) {
        linkedRelationship = relationship;
        fileDirtyProperty = arg_fileDirtyProperty;
        valueProperty.bindBidirectional(linkedRelationship.valueProperty());
        investigateProperty.bindBidirectional(linkedRelationship.investigateProperty());
        logicTypeProperty.bind(linkedRelationship.logicTypeProperty());
        highlight.bind(Bindings.createStringBinding(() -> linkedRelationship.logicTypeProperty().getValue().toString(),linkedRelationship.logicTypeProperty()));
        
        investigateProperty.addListener((e,oldValue,newValue) -> {
            if (newValue==true) {
                this.getStyleClass().add("highlight-PREDECESSOR");
            } else {
                FilteredList<String> styles = this.getStyleClass().filtered(g -> g.contains("highlight"));
                this.getStyleClass().remove(styles.get(0));
            }
        });
        
        
        Rectangle myRectangle = new Rectangle(cellwidth, cellwidth, Color.TRANSPARENT);

        Circle circle = new Circle((float)cellwidth*2/5,Color.TRANSPARENT);
 
        Line line1 = new Line(5,5,cellwidth -5 ,cellwidth -5 );
        Line line2 = new Line(cellwidth -5 ,5,5,cellwidth -5 );
        
        
        valueProperty.addListener( (e,oldValue,newValue) -> {
//            fLogger.info("GridCell valueProperty chaged to:" + newValue)
                });
        
//        myRectangle.setStroke(Color.TRANSPARENT);
        
        circle.setMouseTransparent(true);
        circle.getStyleClass().add("o");
        //circle.relocate(5, 5);
        
        circle.visibleProperty().bind(valueProperty.isEqualTo(ValueType.VALUE_YES));
        
        //line1.setMouseTransparent(true);
        line1.getStyleClass().add("x");
        line1.setMouseTransparent(true);
        line2.getStyleClass().add("x");
        line2.setMouseTransparent(true);
        //xPane.getChildren().addAll(line1,line2);
        line1.visibleProperty().bind(valueProperty.isEqualTo(ValueType.VALUE_NO));
        line2.visibleProperty().bind(valueProperty.isEqualTo(ValueType.VALUE_NO));
        
        MenuItem setFalseMenuItem = new MenuItem("Set as FALSE");
        //        <div>Icon made by <a href="http://www.amitjakhu.com" title="Amit Jakhu">Amit Jakhu</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>

        setFalseMenuItem.setGraphic(new ImageView("/icons/context-menus/x.png"));
        setFalseMenuItem.disableProperty().bind(valueProperty.isNotEqualTo(ValueType.VALUE_UNKNOWN));
        setFalseMenuItem.setOnAction(e -> {
            try {
                this.setFalse();
            } catch (RelationshipConflictException ex) {
//                Logger.getLogger(GridCell.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        MenuItem setTrueMenuItem = new MenuItem("Set as TRUE");
                
        setTrueMenuItem.setGraphic(new ImageView("/icons/context-menus/o.png"));
        //<div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        setTrueMenuItem.disableProperty().bind(valueProperty.isNotEqualTo(ValueType.VALUE_UNKNOWN));
//        item2.setDisable(true);
        setTrueMenuItem.setOnAction(e -> {
            try {
                this.setTrue();
            } catch (RelationshipConflictException ex) {
                //Logger.getLogger(GridCell.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //needs to add this to constraint table
        
        MenuItem resetMenuItem = new MenuItem("Clear relationship");
        resetMenuItem.setGraphic(new ImageView("/icons/context-menus/clear.png"));
        //<div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        resetMenuItem.disableProperty().bind(Bindings.or(valueProperty.isEqualTo(ValueType.VALUE_UNKNOWN), this.logicTypeProperty.isNotEqualTo(Relationship.LogicType.CONSTRAINT)));
        
        resetMenuItem.setOnAction(e -> {
            this.reset();
            linkedRelationship.clearInvestigate();
        });
        
        MenuItem investigateMenuItem = new MenuItem("Investigate");
        investigateMenuItem.setGraphic(new ImageView("/icons/context-menus/investigate.png"));
        //<div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        investigateMenuItem.disableProperty().bind(Bindings.or(valueProperty.isEqualTo(ValueType.VALUE_UNKNOWN),this.investigateProperty));
        investigateMenuItem.setOnAction(e -> {
           System.out.println("about to draw a special line with funky style: "+highlight.get());
           linkedRelationship.clearInvestigate();
           this.investigateProperty.set(true);
           this.getStyleClass().remove("highlight-PREDECESSOR"); //undoing previous
           this.getStyleClass().add("highlight-"+highlight.get()); //should override the one set by the property listener
//           System.out.println(this.localToParent(this.boundsInParentProperty().get()).getMinX());
//           System.out.println(this.localToParent(this.boundsInParentProperty().get()).getMaxX());
//           System.out.println(this.localToParent(this.boundsInParentProperty().get()).getMinY());
//           System.out.println(this.localToParent(this.boundsInParentProperty().get()).getMaxY());
           
           
//           Grid logicProblemGrid = (Grid) this.parentProperty().get().parentProperty().get();
//           
//           System.out.println(logicProblemGrid.getScaleX());
////           System.out.println(this.localToScene(circle.centerXProperty().get(),circle.centerYProperty().get()));
//           StackPane mainStack = (StackPane) this //stackPane gridCell
//                .parentProperty().get() //anchorPane (layer 3)
//                    .parentProperty().get() //Grid extends StackPane
//                            .parentProperty().get() //group
//                                    .parentProperty().get() //scrollpane skin
//                                        .parentProperty().get() //scrollpane viewport
//                                                .parentProperty().get() //scrollpane mainScroll
//                                                    .parentProperty().get() //mainGrid
//                                                        .parentProperty().get(); //mainStack
//           linkedRelationship.drawPredecessors(mainStack);
        });
        
        
        
        MenuItem clearInvestigateMenuItem = new MenuItem("Clear investigation");
        clearInvestigateMenuItem.setGraphic(new ImageView("/icons/context-menus/noinvestigate.png"));
        //<div>Icon made by <a href="http://www.freepik.com" title="Freepik">Freepik</a> from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>.com</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a></div>
        clearInvestigateMenuItem.disableProperty().bind(this.investigateProperty.not());
        clearInvestigateMenuItem.setOnAction(e -> {
//           System.out.println("about to draw a special line with funky style: "+highlight.get());
           linkedRelationship.clearInvestigate();
//           this.investigateProperty.set(true);
        });
        
        contextMenu.getItems().addAll(setFalseMenuItem,setTrueMenuItem, resetMenuItem, investigateMenuItem, clearInvestigateMenuItem);
        
        myRectangle.setOnMouseClicked(e -> contextMenu.show(myRectangle, Side.RIGHT, 0, 0));  
//        myRectangle.setOnMouseClicked(e -> contextMenu.show(e));
        this.getStyleClass().add("gridCell");
//        this.setMouseTransparent(false);
        this.getChildren().addAll(myRectangle,circle,line1,line2);
        
        Tooltip tooltip = new Tooltip();
        tooltip.textProperty().bind(this.linkedRelationship.relationshipTextProperty());
        Tooltip.install(myRectangle, tooltip);
        
    
        
        linkedRelationship.centerXProperty().bind(Bindings.createDoubleBinding(
            ()->localToScene(circle.centerXProperty().get(),circle.centerYProperty().get()).getX(),
            circle.centerXProperty(),this.boundsInParentProperty(),this.scaleXProperty()));
        linkedRelationship.centerYProperty().bind(Bindings.createDoubleBinding(
            ()->localToScene(circle.centerXProperty().get(),circle.centerYProperty().get()).getY(),
            circle.centerYProperty(),this.boundsInParentProperty(),this.scaleXProperty()));      
    
    }

    
    public ObjectProperty<ValueType> valueProperty() {
        return this.valueProperty;
    }
    
    public ValueType getValue(){
        return this.valueProperty.get();
    }
    
    public void setFalse() throws RelationshipConflictException {
        //this.valueProperty.set(ValueType.VALUE_NO);
        fLogger.info("setting FALSE");
        try {
            linkedRelationship.setValue(ValueType.VALUE_NO, Relationship.LogicType.CONSTRAINT);
            fLogger.info("setting FALSE");
        } catch (Exception e) {
            fLogger.info("exception setting FALSE");
        }
    }
     
    
    public void setTrue() throws RelationshipConflictException {
        //this.valueProperty.set(ValueType.VALUE_YES);
        fLogger.info("setting TRUE");
        try {
            linkedRelationship.setValue(ValueType.VALUE_YES, Relationship.LogicType.CONSTRAINT);
            fLogger.info("setting TRUE");
        } catch (Exception e) {
            fLogger.info("exception setting TRUE");
        }
        
        fileDirtyProperty.set(true);
    }
 
    public void reset() {
        this.valueProperty.set(ValueType.VALUE_UNKNOWN);
        fLogger.info("setting UNKNOWN");
        fileDirtyProperty.set(true);
    }
}
