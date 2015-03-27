/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.Proctor;

import DatabaseEntity.*;
import ApplicationLayer.*;
import java.util.ArrayList;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author ASUS User
 */
public class BookNewDialogController extends Controller {
    private ManageInvigilationViewController manageInvigilationViewController;
    private Stage dialogStage;
    private ArrayList<ExamRecord> newBookList;
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TableView<ExamRecord> BookTable;
    @FXML
    private TableColumn<ExamRecord, String> DateColumn;
    @FXML
    private TableColumn<ExamRecord, String> CourseColumn;

    @FXML
    void bookHandler() {
        int selectedIndex = BookTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            addBookedStudent(selectedIndex);
            dialogStage.close();
        } else {
            noneSelected();
	}
    }

    @FXML
    void returnHandler() {
        dialogStage.close();
    }

    @FXML
    void initialize() {
        DateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ExamRecord, String>, ObservableValue<String>>() {
            @Override public ObservableValue<String> call(TableColumn.CellDataFeatures<ExamRecord, String> p) {
             String displayDate = new SimpleDateFormat("dd/MM/yyyy hh:mm").format(p.getValue().getExamDate());
             ReadOnlyStringWrapper sp = new ReadOnlyStringWrapper(displayDate);                 
             return sp;
            }
        });
        CourseColumn.setCellValueFactory(new PropertyValueFactory<ExamRecord, String>("courseCode"));
        BookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    
    public void process() {
        BookTable.setItems(getNewBookListData());        
    }
    public void setManageInvigilationViewController(ManageInvigilationViewController manageInvigilationViewController) {
        this.manageInvigilationViewController = manageInvigilationViewController;
    }
    public ManageInvigilationViewController getManageInvigilationViewController() {
        return this.manageInvigilationViewController;
    }
    private void addBookedStudent(int selectedIndex) {
        this.manageInvigilationViewController.addBookedStudent(newBookList.get(selectedIndex));
    }
    private void noneSelected() {
        Dialogs.showWarningDialog(manageInvigilationViewController.getProctorController().getMainApp()
				.getPrimaryStage(), "Please select a student in the list",
				"No course selected", "Invalid selection");
    }       
    public void setDialogStage(Stage dialogStage) {
            this.dialogStage = dialogStage;
    }
    public void setNewBookList(ArrayList<ExamRecord> newBookList) {
        this.newBookList = newBookList;
    }
    private ObservableList<ExamRecord> getNewBookListData() {
            ObservableList data = FXCollections.observableArrayList();
            for(int i = 0; i < newBookList.size(); i ++) {
                data.add(newBookList.get(i));                
            }
            return data;
    }
}
