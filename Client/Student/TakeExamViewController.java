package Client.Student;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import util.Communicator;
import util.StudentCommunicator;
import util.VideoManager;
import util.VideoUtil;
import util.WebcamUtil;
import ApplicationLayer.BookedCourse;
import ApplicationLayer.Controller;
import ApplicationLayer.Message;
import ApplicationLayer.Observer;
import DatabaseEntity.Course;
import DatabaseEntity.ExamRecord;
import DatabaseEntity.Student;
import java.awt.Desktop;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author phongnt
 */
public class TakeExamViewController extends Controller implements Observer {
	
    StudentController studentController;
    Student student;
    ObservableList<BookedCourse> coursesData;
    Communicator webcamCommunicator;
    Communicator screenCommunicator;
    Communicator identityComm;
    VideoManager webcamManager;
    VideoManager screenManager;

    @FXML
    private Button connectButton;
    @FXML
    private Button startExamButton;
    @FXML
    private TableView<BookedCourse> coursesTable;
    @FXML
    private TableColumn<BookedCourse, String> courseCodeColumn;
    @FXML
    private TableColumn<BookedCourse, String> courseNameColumn;
    @FXML
    private TableColumn<BookedCourse, String> examDateColumn;
    @FXML
    private ImageView webcamView;
    @FXML
    private Label statusLabel;

    public TakeExamViewController() {
    }

    @FXML
    private void initialize(){
        System.out.println("CoursesViewController");
        courseCodeColumn.setCellValueFactory(new PropertyValueFactory<BookedCourse, String>("courseCode"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<BookedCourse,String>("courseName"));
        examDateColumn.setCellValueFactory(new PropertyValueFactory<BookedCourse,String>("examDate"));
        coursesTable.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<BookedCourse>() {
                        @Override
                        public void changed(
                                        ObservableValue<? extends BookedCourse> observable,
                                        BookedCourse oldValue, BookedCourse newValue) {
                                toogleButton(newValue);
                        }
                });
        statusLabel.setText("Server offline");
        coursesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    @FXML
    private void disableStartExamButton()
    {
        startExamButton.setDisable(true);
        System.out.println("Start Exam Disabled");
    }
    
    @FXML
    private void enableStartExamButton()
    {
        startExamButton.setDisable(false);
        System.out.println("Start Exam enabled");
    }
    public void authorizeCourse(String courseCode)
    {
        for(BookedCourse course:coursesData)
        {
            if(course.getCourseCode().equals(courseCode))
                course.setEnableStartExam(true);
        }
    }
    private ObservableList<BookedCourse> getCoursesData(Student student) {
        ObservableList<BookedCourse> coursesData = FXCollections.observableArrayList();
        for(Course c:student.getRegisteredCourses()){
            BookedCourse bookedCourse = new BookedCourse(c.getCourseCode(),c.getCourseName(),student.getExamRecord());
            if(bookedCourse.isBooked())
                coursesData.add(bookedCourse);
        }
        return coursesData;
    }
    
    public void setController(StudentController studentController){
        this.studentController = studentController;
    }
    
    private void toogleButton(BookedCourse newValue) {
        if(newValue == null)
            return;
        if(newValue.isEnableStartExam())
            enableStartExamButton();
        else
            disableStartExamButton();
    }

    private boolean isBooked(Course course){
        for(ExamRecord record: student.getExamRecord()){
            if(record.getCourseCode()==course.getCourseCode())
                return true;
        }
        return false;
    }

    public void refresh(){
        this.student = studentController.getStudent();
        this.coursesData = getCoursesData(student);
        coursesTable.setItems(coursesData);
    }

    public void update(){
        refresh();
    }
    @FXML
    public void handleConnectButton() {
        
    	BookedCourse bookedCourse = coursesTable.getSelectionModel().getSelectedItem();
        if(bookedCourse == null)
        {
            noneSelected();
            return;
        }
    	if(connectToProctor(bookedCourse)){
            webcamManager = new WebcamUtil(webcamCommunicator,webcamView);
            webcamManager.start();
            screenManager = new VideoUtil(screenCommunicator);
            screenManager.start();
            System.out.println("started 2");
    	} else {
    	System.out.println("Cannot connect");
    	}    
    }
    
    @FXML
    public void handleStartExamButton() {
    	String URL = null;
    	BookedCourse bookedCourse = coursesTable.getSelectionModel().getSelectedItem();
        ExamRecord exam = null;
        if(bookedCourse != null)
        {
            for(ExamRecord record:studentController.getStudent().getExamRecord())
            {
                if(record.getCourseCode().equals(bookedCourse.getCourseCode()))
                {
                    exam = record;
                    break;
                }
            }
            URL = studentController.startExam(exam);
            try
            {
                URL url = new URL(URL);
                openWebpage(url.toURI());
            }   catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(TakeExamViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                    Logger.getLogger(TakeExamViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            System.out.println("starting exam...");
    	} else {
    		noneSelected();
    	}
    }
    
    public void openWebpage(URI uri)
    {
        Desktop desktop = Desktop.isDesktopSupported()?Desktop.getDesktop():null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        try {
            desktop.browse(uri);
            } catch (Exception e) {
            e.printStackTrace();
            }
        }
    }
    
    private void noneSelected() {
		Dialogs.showWarningDialog(new Stage(),
				"Please select a course in the list",
				"No Course Selected", "No Selection");
	}
    
    private boolean connectToProctor(BookedCourse bookedCourse){
        if(!bookedCourse.isBooked())
        {
            System.out.println("The course is not booked yet");
            return false;
        }
        java.util.Date date = new java.util.Date();
        Timestamp currentTimestamp = new Timestamp(date.getTime());
        long diff = bookedCourse.getExamDate().getTime() - currentTimestamp.getTime();
        int min = (int)diff/(1000*60);
        System.out.println("Difference: " + min);
        //this block check timing before make connection
//        if(min > 30)
//        {
//            Dialogs.showErrorDialog(null, "You can only connect 30 minutes before the exam. Please come back later");
//            return false;
//        }
            
        
        //get the exam record
        ExamRecord  exam = null;
        for(ExamRecord record: student.getExamRecord()){
            if(record.getCourseCode().equalsIgnoreCase(bookedCourse.getCourseCode()))
                exam = record;
        }
        //connect to proctor
        try{
            String proctorIP = studentController.getProctorIP(exam);
            System.out.println("Proctor ip get: " + proctorIP);
            if(proctorIP!=null)
            {
                identityComm = (Communicator) new StudentCommunicator(proctorIP);
                Message msg = new Message(Message.SET_AUTHORIZE);
                msg.addContent(exam);
                identityComm.sendMessage(msg);
                System.out.println("sent identity to proctor");
                msg=identityComm.getMessage();
                
                webcamCommunicator =(Communicator) new StudentCommunicator(proctorIP);  
                screenCommunicator =(Communicator) new StudentCommunicator(proctorIP);
            }
            else
            {
                Dialogs.showErrorDialog(null, "Proctor is not online yet. Please come back later");
                return false;
            }
            return true;
        }
        catch (NullPointerException e){
            //Cannot get proctor IP
           Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Dialogs.showErrorDialog(null, "Could not get proctor IP");
                }	
            }); 
            return false;
        }
    }
}
