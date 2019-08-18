/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled.data;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Point2D;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import org.fxmisc.flowless.Cell;
import org.fxmisc.flowless.VirtualFlow;

/**
 *
 * @author phiv
 */

@XmlRootElement
@XmlType(propOrder={"text","type"})

public class Clue extends Dependable implements Comparable<Clue> {
    //private HashSet<Dependable> successors = new HashSet<Dependable>();
    //private HashSet<Dependable> predecessors = new HashSet<Dependable>();
    //private BooleanProperty investigateProperty = new SimpleBooleanProperty(false);

    public enum ClueType {
        NORMAL,
        SPECIAL //needs re-assessment, see notebook for SpecialClue    
        //CONSTRAINT
    }
    
    //private ClueNumber clueNumber;
    //properties are necessary for the TableView
    private ObjectProperty<ClueNumber> clueNumberProperty = new SimpleObjectProperty<>();
    //properties are necessary for the TableView
    private StringProperty clueTextProperty = new SimpleStringProperty();
    
    private ClueType clueType = ClueType.NORMAL;
     //whether the clue is enabled or disabled
    private BooleanProperty activeProperty = new SimpleBooleanProperty(true); 
    //whether the clue has been resolved or if it needs to be parsed
    //can be bound to visual property of the clue in the tooltip or the ClueTab
    private BooleanProperty resolvedProperty = new SimpleBooleanProperty(true);
    
    
    //necessary for iterator?
    public Clue(){
    }
       
    public Clue(ClueNumber clueNumber, String clueText, ClueType arg_clueType) {
        clueTextProperty.set(clueText);
        clueNumberProperty.set(clueNumber);
        clueType = arg_clueType;
//        this.activeProperty.set(false);
    }
    
    public Clue(ClueNumber clueNumber, String ... clueInfo) { //clue text and clue type
        clueTextProperty.set(clueInfo[0].trim());
        if (clueInfo.length>1) clueType = ClueType.valueOf(clueInfo[1].trim());
        clueNumberProperty.set(clueNumber);
//        this.activeProperty.set(false);
    }
    
    
    @XmlElement //?
    public String getClueText(){
        return this.clueTextProperty.get();
    }

    public String displayClue(){
        return this.getClueNumber().toString() + " -> " + this.clueTextProperty.get();
    }
    
    public void setClueText(String arg_text) {
        this.clueTextProperty.set(arg_text);
    }
    
    public void setClueType(ClueType arg_type) {
        this.clueType = arg_type;
    }
    
    //used for ClueNumber computation, alternatively, calls to this method
    //could be replaced by calls to getClueNumberProperty().get()
    public ClueNumber getClueNumber() {
        return this.clueNumberProperty.get();
    }
    
    //necessary for the TableView celldata column assignment
    public ObjectProperty<ClueNumber> clueNumberProperty() {
        return this.clueNumberProperty;
    }
    
   
    //necessary for the TableView
    public StringProperty clueTextProperty() {
        return this.clueTextProperty;
    }
    
    public BooleanProperty activeProperty() {
        return this.activeProperty;
    }
    
    public boolean isActive() {
        return this.activeProperty.get();
    }
    
    public void setActive(boolean active) {
        this.activeProperty.set(active);
    }
    
    //
//    public String getClueNumberAsString() {
//        return (this.getClueNumber() == null)?"":this.getClueNumber().getStringProperty().get();
//    }
    
    
    @XmlElement
    public ClueType getClueType(){
        return clueType;
    }
    
    @XmlTransient
    public Point2D getCenterPosition(){
        return null;
    }
    
    @Override
    public int compareTo(Clue o) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return this.getClueNumber().compareTo(o.getClueNumber());
    }
    
    
    public static Label majorGlyphGenerator(ObservableList<Clue> clues, Integer clueMajor) {
        Label label = new Label(clueMajor.toString());
        Tooltip tooltip = new Tooltip();//else we get a NPE
        
        Predicate<Clue> firstMajor = i -> (i.getClueNumber().getMajor() == clueMajor);
        FilteredList<Clue> filteredClues = new FilteredList(clues, firstMajor);
        
        Function<Clue, Text> textmapper = clue -> {
            Text clueText = new Text();
            clueText.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
            //bind clue text
            clueText.textProperty().bind(clue.getClueNumber().clueNumberStringProperty().concat(" -> ").concat(clue.clueTextProperty));
            //bind clue colour
            clueText.fillProperty().bind(
                    Bindings.when(clue.activeProperty).then(Color.BLUEVIOLET).otherwise(Color.GRAY)
                    );
            return clueText;
        };
        
        //the technique below creates a property binding from an observable
        //list that also reacts to property changes in the individual elements
        //inspired from https://stackoverflow.com/questions/46300540/javafx-how-to-make-a-binding-to-a-list-of-properties
        List<BooleanProperty> l = filteredClues.stream()
            .map(c -> c.activeProperty())
            .collect(Collectors.toList());
//                
        ObservableList<BooleanProperty> cluewatcher = 
                FXCollections.observableArrayList(w -> new Observable[] {w});
        
        cluewatcher.addAll(l);
        
        label.idProperty().bind(Bindings.when(
                Bindings.createBooleanBinding(
            () -> cluewatcher.stream().allMatch(BooleanProperty::get), cluewatcher))
            .then("cluesactive").otherwise(
                    Bindings.when(Bindings.createBooleanBinding(
                    () -> cluewatcher.stream().noneMatch(BooleanProperty::get), cluewatcher))
                    .then("cluesinactive")
                    .otherwise("cluespartiallyactive")
            ));
        
        //distinct is not required, but it saves creating a new class that omits the .distinct() stream method 
        ObservableList<Text> textList = new DistinctMappingList<>(filteredClues, textmapper);
        
        VBox vbox = new VBox();
        
        Bindings.bindContent(vbox.getChildren(), textList);
        
        //this is probably the thing that took me the most time figuring out
        //without these bindings, it looks as if the filtered textList binding in the 
        //VBox container does not react to new clues
        tooltip.prefWidthProperty().bind(vbox.widthProperty());
        tooltip.prefHeightProperty().bind(vbox.heightProperty());
        tooltip.setGraphic(vbox);
        tooltip.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        
        label.setTooltip(tooltip);
        return label;
    }
    
    private static Text clueText(Clue clue) {
        Text clueText = new Text();
        clueText.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, Font.getDefault().getSize()));
        //bind clue text
        clueText.textProperty().bind(clue.getClueNumber().clueNumberStringProperty().concat(" -> ").concat(clue.clueTextProperty));
        //bind clue colour
        clueText.fillProperty().bind(
                Bindings.when(clue.activeProperty).then(Color.BLUEVIOLET).otherwise(Color.GRAY)
                );
        return clueText;
    }
    
}
