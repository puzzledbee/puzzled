/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.util.Pair;
import puzzled.data.Clue;
import puzzled.data.ClueNumber;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class ClueTabController implements Initializable {
    
    private PuzzledController parentController;
    
    @FXML
    TableView<Pair<ClueNumber, Clue>> clueTableView;
    
    @FXML
    TableColumn<Pair<ClueNumber, Clue>, ClueNumber> clueNumberColumn;
    
    @FXML
    TableColumn<Pair<ClueNumber, Clue>, Clue> clueTextColum;
    
    public void setParentController(PuzzledController parent) {
        this.parentController = parent;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // controller available in initialize method
        
        ObservableList<Pair<ClueNumber, Clue>> data = FXCollections.observableArrayList(
                pair(new ClueNumber(1,1,1), new Clue("Bach Cello Suite 2")),
                pair(new ClueNumber(1,1,2), new Clue("Bfadsfadsf")),
                pair(new ClueNumber(1,2,1), new Clue("fdsfadsfa")),
                pair(new ClueNumber(1,2,2), new Clue("fdasfasdf")),
                pair(new ClueNumber(3,1,1), new Clue("fdsafadf"))
        );

        clueTableView.getItems().setAll(data);
        //clueTableView.getItems().setAll(parentController.getLogicProblemProperty().get().clues);
        
        
        // table column definitions in FXML file
        clueNumberColumn.setCellValueFactory(new PairKeyFactory());
        clueTextColum.setCellValueFactory(new PairValueFactory());

        //clueTableView.getColumns().setAll(clueNumberColumn, clueTextColum); --> in FXML
        clueTextColum.setCellFactory(new Callback<TableColumn<Pair<ClueNumber, Clue>, Clue>, TableCell<Pair<ClueNumber, Clue>, Clue>>() {
            @Override
            public TableCell<Pair<ClueNumber, Clue>, Clue> call(TableColumn<Pair<ClueNumber, Clue>, Clue> column) {
                return new PairValueCell();
            }
        });
        clueNumberColumn.setCellFactory(new Callback<TableColumn<Pair<ClueNumber, Clue>, ClueNumber>, TableCell<Pair<ClueNumber, Clue>, ClueNumber>>() {
            @Override
            public TableCell<Pair<ClueNumber, Clue>, ClueNumber> call(TableColumn<Pair<ClueNumber, Clue>, ClueNumber> column) {
                return new PairKeyCell();
            }
        });
    }
    
    private Pair<ClueNumber, Clue> pair(ClueNumber number, Clue clue) {
        return new Pair<>(number, clue);
    }
    
    //can't set that during initialize
    public void setData(ObservableList<Pair<ClueNumber, Clue>> clues) {
        clueTableView.getItems().setAll(clues);
    }
    
    
    class PairKeyFactory implements Callback<TableColumn.CellDataFeatures<Pair<ClueNumber, Clue>, ClueNumber>, ObservableValue<ClueNumber>> {
    @Override
    public ObservableValue<ClueNumber> call(TableColumn.CellDataFeatures<Pair<ClueNumber, Clue>, ClueNumber> data) {
        return new ReadOnlyObjectWrapper<>(data.getValue().getKey());
    }
}

    class PairValueFactory implements Callback<TableColumn.CellDataFeatures<Pair<ClueNumber, Clue>, Clue>, ObservableValue<Clue>> {
        @SuppressWarnings("unchecked")
        @Override
        public ObservableValue<Clue> call(TableColumn.CellDataFeatures<Pair<ClueNumber, Clue>, Clue> data) {
            Clue value = data.getValue().getValue();
            return (value instanceof ObservableValue)
                    ? (ObservableValue) value
                    : new ReadOnlyObjectWrapper<Clue>(value);
        }
    }

    class PairValueCell extends TableCell<Pair<ClueNumber, Clue>, Clue> {
        @Override
        protected void updateItem(Clue item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                if (item instanceof Clue) {
                    setText((String) item.getText());
                    setGraphic(null);
                }
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
    
     class PairKeyCell extends TableCell<Pair<ClueNumber, Clue>, ClueNumber> {
        @Override
        protected void updateItem(ClueNumber item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                if (item instanceof ClueNumber) {
                    setText((String) item.getClueNumberString());
                    setGraphic(null);
                }
            } else {
                setText(null);
                setGraphic(null);
            }
        }
    }
}
