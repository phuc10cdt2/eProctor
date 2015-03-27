/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client;

import ApplicationLayer.Controller;
import ApplicationLayer.Message;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import DatabaseEntity.*;
import java.sql.Date;
import java.sql.Timestamp;
import javafx.application.Platform;
import javafx.scene.control.Dialogs;
/**
 *
 * @author phongnt
 */
public class LoginController extends Controller {
    //Domain Constants
    private static final int STUDENT_DOMAIN = 0;
    private static final int PROCTOR_DOMAIN = 1;
    
    
    @FXML
    private PasswordField password;
    @FXML
    private TextField username;
    @FXML
    private ComboBox domainComboBox;
    @FXML
    private Label errorLabel;

    @FXML
    private void initialize(){
        
    }
    @FXML
    private void loginClicked(){
        //get username, password, domain
        String passwd = password.getText();
        String usrname = username.getText();
        int domain = domainComboBox.getSelectionModel().getSelectedIndex();
        //domain = 0 or 1
        User tempUser = null;
        if(domain!=-1)
            tempUser = processLogin(usrname, passwd, domain);
        if(tempUser!=null){
            if(domain == STUDENT_DOMAIN){//Student
                getMainApp().showStudentCP((Student)tempUser);
            }
            else  if(domain == PROCTOR_DOMAIN){
                getMainApp().showProctorCP((Proctor)tempUser);
                System.out.println("Exam record Size received: " + ((Proctor)tempUser).getExamRecord().size());
            }
        }
        else{
            //Display error message
//            Dialogs.showErrorDialog(null, "Wrong username or password. Try again!");
            errorLabel.setText("Wrong username or password!");
        }
        
    }
    
    private User processLogin(String username, String password, int domain){
//        //Sua o day de login ko can server
//        //Dummy Data
//        Date dob = new Date(1,2,4);
//        java.util.Date date = new java.util.Date();
//        Student student = new Student("U122", "username", "password", "John", "Doe", dob);
//        student.addCourse(new Course("CE0001","course A"));
//        student.addCourse(new Course("CE0002","course B"));
//        student.addExamRecord(new ExamRecord(student.getMatricNo(),"CE0001",new Timestamp(date.getTime() ) ));
//        return student;
        //
        User user=null;
        Message msg = new Message(Message.LOGIN_STUDENT);
        if(domain==STUDENT_DOMAIN){
            msg.setMessageCode(Message.LOGIN_STUDENT);
            user = new Student();
        }
        else if( domain == PROCTOR_DOMAIN){
            msg.setMessageCode(Message.LOGIN_PROCTOR);
            user = new Proctor();
        }
        System.out.println("Domain: " + domain);
        user.setUsername(username);
        user.setPassword(password);
        msg.addContent(user);
        getMainApp().sendMessage(msg);
        Message reply = null;
        reply=getMainApp().getMessage();
        System.out.println("AAAAAAAAAAAA:" + reply.getMessageCode());
        if(reply.getMessageCode() == Message.NEGATIVE)
            return null;
        else{
            if (reply.getMessageCode() == Message.ALREADY_LOGGED_IN) {            
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Dialogs.showErrorDialog(null, "Already logged in");
                    }	
                });     
                return null;
            }
            else {
                //System.out.println("User received: " + (reply.getMessageCode() == 1 ? "Student" : "Proctor"));
                //System.out.println(reply.getContent().get(0));
                try{
                    user=(User) (reply.getContent().get(0) );
                }
                catch(Exception e)
                {
                     errorLabel.setText("Login failed. Please try again");
                     return null;
                }
                
                return user;
            }
        }
    }  
    
}
