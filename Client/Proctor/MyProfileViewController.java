/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.Proctor;
import ApplicationLayer.Controller;
import DatabaseEntity.Proctor;
import DatabaseEntity.User;

import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

import javax.swing.JOptionPane;

import util.CalendarUtil;
public class MyProfileViewController extends Controller{
    private ProctorController proctorController; 
    private Proctor proctor;
    
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ListView<String> myProfileDetails;
    @FXML
    private Label userName;
    @FXML
    private Label fullName;
    @FXML
    private Label proctorId;
    @FXML
    private Label birthday;

    @FXML
    private void initialize() {
        System.out.println("MPVC initialize called.");
    }
    
    public MyProfileViewController() {
        
    }
    
    void addDataToProfileTable() {
        /*ObservableList<String> details = FXCollections.observableArrayList();
        details.add("Username: " + this.proctor.getUsername());
        details.add("Full name: " + this.proctor.getFirstName() + " " + this.proctor.getLastName());
        myProfileDetails.setItems(details);*/
    	userName.setText(this.proctor.getUsername());
    	fullName.setText(this.proctor.getFirstName() + " " + this.proctor.getLastName());
    	proctorId.setText(this.proctor.getProctorID());
    	Calendar c = Calendar.getInstance();
    	c.setTime(this.proctor.getDateOfBirth());
    	birthday.setText(CalendarUtil.format(c));
    }
    @FXML
    void changeUserName() {
        String newUserName = JOptionPane.showInputDialog("New user name: ");
        this.proctor.setUsername(newUserName);    
        //addDataToProfileTable();
    }
    void setController(ProctorController proctorController) {
        this.proctorController = proctorController;
        this.proctor = proctorController.getProctor();
        addDataToProfileTable();
    }
}

