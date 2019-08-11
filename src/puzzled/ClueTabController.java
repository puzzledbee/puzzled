/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzled;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import puzzled.data.Clue;
import puzzled.data.ClueNumber;

/**
 *
 * @author https://github.com/bepuzzled
 */
public class ClueTabController implements Initializable {
    
    private PuzzledController parentController;
    
    @FXML TableView<Clue> clueTableView;
    
    @FXML TableColumn<Clue,String> clueNumberColumn;
    @FXML TableColumn<Clue,String> clueTextColumn;
    @FXML TableColumn<Clue,String> actionColumn;
    
    public void setParentController(PuzzledController parent) {
        this.parentController = parent;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb){
        // controller available in initialize method

        // table column definitions in FXML file
        //this next one does not work because clueNumberStringProperty() is a method of ClueNumber, not Clue
//        clueNumberColumn.setCellValueFactory(new PropertyValueFactory<Clue,String>("clueNumberString"));
//        clueTextColumn.setCellValueFactory(new PropertyValueFactory<Clue,String>("clueText"));
        clueNumberColumn.setCellValueFactory(cellData -> cellData.getValue().getClueNumber().clueNumberStringProperty());
        clueTextColumn.setCellValueFactory(cellData -> cellData.getValue().clueTextProperty());
        actionColumn.setCellValueFactory(cellData -> new SimpleStringProperty("DUMMY"));
        
        Callback<TableColumn<Clue, String>, TableCell<Clue, String>> cellFactory
                =  new Callback<TableColumn<Clue, String>, TableCell<Clue, String>>() {
            
            @Override
            public TableCell call(final TableColumn<Clue, String> param) {
                final TableCell<Clue, String> cell = new TableCell<Clue, String>() {

                    ToggleButton btn = new ToggleButton("");
                    
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.getStyleClass().add("enablebutton");
                            btn.setSelected(true);
                            btn.setOnAction(event -> {
                                Clue clue = getTableView().getItems().get(getIndex());
                                System.out.println(clue.getClueText());
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
        
//        clueNumberColumn.setCellValueFactory(new PairKeyFactory());
//        clueTextColum.setCellValueFactory(new PairValueFactory());

        //clueTableView.getColumns().setAll(clueNumberColumn, clueTextColum); --> in FXML
//        clueTextColum.setCellFactory(new Callback<TableColumn<Pair<ClueNumber, Clue>, Clue>, TableCell<Pair<ClueNumber, Clue>, Clue>>() {
//            @Override
//            public TableCell<Pair<ClueNumber, Clue>, Clue> call(TableColumn<Pair<ClueNumber, Clue>, Clue> column) {
//                return new PairValueCell();
//            }
//        });
//        clueNumberColumn.setCellFactory(new Callback<TableColumn<Pair<ClueNumber, Clue>, ClueNumber>, TableCell<Pair<ClueNumber, Clue>, ClueNumber>>() {
//            @Override
//            public TableCell<Pair<ClueNumber, Clue>, ClueNumber> call(TableColumn<Pair<ClueNumber, Clue>, ClueNumber> column) {
//                return new PairKeyCell();
//            }
//        });
    }
    
//    private Pair<ClueNumber, Clue> pair(ClueNumber number, Clue clue) {
//        return new Pair<>(number, clue);
//    }
    
    //can't set that during initialize
    public void setData(ObservableList<Clue> clues) {
        SortedList<Clue> sortedclues = new SortedList<>(clues.sorted());
        clueTableView.setItems(sortedclues);
        sortedclues.comparatorProperty().bind(clueTableView.comparatorProperty()); //lets users change the sort direction
    }
    
    public void refreshTable() {
        clueTableView.refresh();
    }
    
    
//    class PairKeyFactory implements Callback<TableColumn.CellDataFeatures<Pair<ClueNumber, Clue>, ClueNumber>, ObservableValue<ClueNumber>> {
//    @Override
//    public ObservableValue<ClueNumber> call(TableColumn.CellDataFeatures<Pair<ClueNumber, Clue>, ClueNumber> data) {
//        return new ReadOnlyObjectWrapper<>(data.getValue().getKey());
//    }
}

//    class PairValueFactory implements Callback<TableColumn.CellDataFeatures<Pair<ClueNumber, Clue>, Clue>, ObservableValue<Clue>> {
//        @SuppressWarnings("unchecked")
//        @Override
//        public ObservableValue<Clue> call(TableColumn.CellDataFeatures<Pair<ClueNumber, Clue>, Clue> data) {
//            Clue value = data.getValue().getValue();
//            return (value instanceof ObservableValue)
//                    ? (ObservableValue) value
//                    : new ReadOnlyObjectWrapper<Clue>(value);
//        }
//    }

//    class PairValueCell extends TableCell<Pair<ClueNumber, Clue>, Clue> {
//        @Override
//        protected void updateItem(Clue item, boolean empty) {
//            super.updateItem(item, empty);
//
//            if (item != null) {
//                if (item instanceof Clue) {
//                    setText((String) item.getText());
//                    setGraphic(null);
//                }
//            } else {
//                setText(null);
//                setGraphic(null);
//            }
//        }
//    }
    
//     class PairKeyCell extends TableCell<Pair<ClueNumber, Clue>, ClueNumber> {
//        @Override
//        protected void updateItem(ClueNumber item, boolean empty) {
//            super.updateItem(item, empty);
//
//            if (item != null) {
//                if (item instanceof ClueNumber) {
//                    setText((String) item.getClueNumberString());
//                    setGraphic(null);
//                }
//            } else {
//                setText(null);
//                setGraphic(null);
//            }
//        }
//    }
//}
