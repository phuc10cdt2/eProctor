/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.Student;

import ApplicationLayer.Controller;
import ApplicationLayer.Observer;
import ApplicationLayer.Subject;
import DatabaseEntity.Student;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author phongnt
 */
public class MyProfileController extends Controller implements Observer{
    StudentController studentController;
    Student student;
    List<Observer> observersList = new ArrayList<Observer>();
    
    @FXML private TextArea firstNameField;
    @FXML private TextArea lastNameField;
    @FXML private TextArea matricNoField;
    @FXML private TextArea dobField;
    
    @FXML 
    private void initialize(){

    }
    @FXML
    private void saveButtonClicked(){
        student.setFirstName(firstNameField.getText());
        student.setLastName(lastNameField.getText());
        System.out.println(student.getLastName());
    }
    
    public void setController(StudentController studentController){
        this.studentController = studentController;

    }
    
    public void refresh(){
        this.student = studentController.getStudent();
        firstNameField.setText(student.getFirstName());
        lastNameField.setText(student.getLastName());
        matricNoField.setText(student.getMatricNo());
        dobField.setText(student.getDateOfBirth().toString());
    }
    
    public void update(){
        refresh();
    }
}
