package birthdays.view.elements;

import birthdays.controller.ServiceController;
import birthdays.model.BDayUnit;
import birthdays.model.Status;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;

public class BirthdayTable extends TableView<BDayUnit> {

    public BirthdayTable() {
        super.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        super.getSelectionModel().setCellSelectionEnabled(true);
        super.getSelectionModel().getSelectedCells().addListener(this::selectCells);

        TableColumn<BDayUnit, String> fullName = new TableColumn<>("Name");
        fullName.setStyle("-fx-alignment: CENTER;");
        TableColumn<BDayUnit, DateTime> bDate = new TableColumn<>("Birthday");
        bDate.setStyle("-fx-alignment: CENTER;");
        TableColumn<BDayUnit, Integer> daysLeft = new TableColumn<>("DaysTo");
        daysLeft.setStyle("-fx-alignment: CENTER;");
        TableColumn<BDayUnit, String> phoneNumber = new TableColumn<>("Phone");
        phoneNumber.setStyle("-fx-alignment: CENTER;");

        super.getColumns().addAll(fullName, bDate, daysLeft, phoneNumber);

        fullName.setCellValueFactory(new PropertyValueFactory<BDayUnit, String>("fullName"));
        bDate.setCellValueFactory(new PropertyValueFactory<BDayUnit, DateTime>("date"));
        daysLeft.setCellValueFactory(new PropertyValueFactory<BDayUnit, Integer>("daysTo"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<BDayUnit, String>("phoneNumber"));

    }

    public void refreshTable(ArrayList<BDayUnit> input) {
        Collections.sort(input);
        ObservableList<BDayUnit> list = FXCollections.observableArrayList(input);
        super.setItems(list);
        super.refresh();
    }

    private void selectCells(ListChangeListener.Change<? extends TablePosition> c) {
        c.getList().forEach(tablePosition -> copyToBuffer(tablePosition.getTableColumn().getCellData(tablePosition.getRow())));
    }

    private void copyToBuffer(Object o) throws NullPointerException {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(o.toString());
        clipboard.setContent(content);
        ServiceController.getInstance().setInfo(Status.INFO, content.getString());
    }

    public BDayUnit getSelectedUnit() {
        return super.getSelectionModel().getSelectedItem();
    }
}