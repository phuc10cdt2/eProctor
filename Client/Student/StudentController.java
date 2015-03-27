/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.Student;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.layout.AnchorPane;
import ApplicationLayer.Controller;
import ApplicationLayer.Message;
import ApplicationLayer.Observer;
import ApplicationLayer.Subject;
import Client.MainApp;
import DatabaseEntity.Course;
import DatabaseEntity.ExamRecord;
import DatabaseEntity.Student;
import DatabaseEntity.User;
import java.util.Timer;
import java.util.TimerTask;
import javafx.scene.control.Label;

/**
 * 
 * @author phongnt
 */
public class StudentController extends Controller implements Subject {
	private Student student;
	private boolean isAuthorised = false;
	private List<Observer> observerList = new ArrayList();
	@FXML
	private AnchorPane myProfileView; // profileView
	@FXML
	private MyProfileController myProfileViewController;
	@FXML
	private AnchorPane manageExamView;// manageExamView
	@FXML
	private ManageExamViewController manageExamViewController;
	@FXML
	private AnchorPane takeExamView;// takeExamView
	@FXML
	private TakeExamViewController takeExamViewController;
	@FXML
	private Button logOutButton;
	private MainApp application;
        Thread TimerThread;
        @FXML
        private Label timerLabel;
        
        private long interval;
        
        final CountdownTimer countdownClock = new CountdownTimer(0);

	@FXML
	private void initialize() {
		myProfileViewController.setController(this);
		manageExamViewController.setController(this);
		takeExamViewController.setController(this);
		attach(myProfileViewController);
		attach(manageExamViewController);
		attach(takeExamViewController);

	}

	@FXML
	private void processLogOut(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		getMainApp().showLogin();
                countdownClock.stop();
		Message msg = new Message(Message.STUDENT_LOG_OUT);
		msg.addContent((User)student);
		getMainApp().sendMessage(msg);
	}

	public void start() {
		manageExamViewController.refresh();
		takeExamViewController.refresh();
                myProfileViewController.update();
//                long temp = manageExamViewController.getTimeToExam();
                countdownClock.start();
                
	}
  class CountdownTimer
        {
            Timer timer = new Timer();
            long interval;
            TimerTask timerTask;
            String timing;
            public CountdownTimer(long interval)
            {
                this.interval = interval;
            }
            public void setInterval(long interval)
            {
                this.interval = interval;
            }
            public void start()
            {
                long temp = manageExamViewController.getTimeToExam();
                interval = temp/1000;
                timerTask = new TimerTask()
                    {
                        public void run(){
                            interval --;
                            timing = "";
                            int hour = (int)interval/3600;
                            int min = (int) (interval/60 - hour*60);
                            int sec = (int) (interval - min*60 - hour*3600);
                            if(hour<10)
                                timing = "0" + hour + ":" ;
                            else
                                timing = timing+hour+ ":";
                            if(min<10)
                                timing = timing + "0" + min + ":";
                            else
                                timing = timing+min+ ":";
                            if(sec<10)
                                timing =timing + "0" + sec;
                            else
                                timing = timing+sec;
                            CountdownTimer.this.setTimer(timing);
                        }
                        
            };
                timer.scheduleAtFixedRate(timerTask, 1000, 1000);
            
        }
            public void stop()
            {
                timer.cancel();
            }
            public void setTimer(final String timing)
        {
            
            Platform.runLater(new Runnable() {
                public void run() {
                    timerLabel.setText(timing);
                }
            });
        }
            public String getTiming()
            {
                return timing;
            }
            
        }
	public boolean book(String courseCode, Date date) {
		Timestamp ts = new Timestamp(date.getTime());
		ExamRecord record = new ExamRecord(student.getMatricNo(), courseCode,
				ts);
		// debug without server
		// student.addExamRecord(record);
		// observerNotify();
		Message msg = new Message(Message.BOOK_EXAM);
		msg.addContent(record);
		try {
			System.out.println(courseCode);
			getMainApp().sendMessage(msg);
			Message ack = getMainApp().getMessage();
                        System.out.println("Message code ack: "+ack.getMessageCode());
			if (ack.getMessageCode() == Message.BOOK_EXAM) {
				student.addExamRecord(record);
				observerNotify();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
                        e.printStackTrace();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Dialogs.showErrorDialog(null,
							"Could not get message from server");
				}
			});
		}
		return false;
	}

	public boolean cancelBooking(String courseCode) {
		ExamRecord cancel = null;
		for (ExamRecord er : student.getExamRecord()) {
			if (er.getCourseCode().equalsIgnoreCase(courseCode)) {
				cancel = er;
			}
		}
		System.out.println(cancel.getRecordID());
		// //test without sever
		// student.getExamRecord().remove(cancel);
		// System.out.println(student.getExamRecord().size());
		// observerNotify();
		// //test without server
		//

		Message msg = new Message(Message.DELETE_EXAM);
		msg.addContent(cancel);
		try {
			getMainApp().sendMessage(msg);
			Message ack = getMainApp().getMessage();
                        System.out.println("Message code: "+ack.getMessageCode());
			if (ack.getMessageCode() == Message.DELETE_EXAM) {
				student.getExamRecord().remove(cancel);
				observerNotify();
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException e) {
                    e.printStackTrace();
                    Dialogs.showErrorDialog(null,"Could not proceed cancel instruction");
		}
		return false;

	}
        public String startExam(ExamRecord exam)
        {
            Message startExamMsg = new Message(Message.START_EXAM);
            startExamMsg.addContent(exam);
            try{
                getMainApp().sendMessage(startExamMsg);
                Message ack = getMainApp().getMessage();
                System.out.println("Message code ack of Start exam: " + ack.getMessageCode());
                if(ack.getMessageCode()== Message.START_EXAM)
                {
                    String URL = (String)ack.getContent().get(1);
                    System.out.println("ack: " + URL);
                    return URL;
                }
                else
                {
                    Dialogs.showErrorDialog(null,"You are not authorized to take this exam!!");
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
                System.out.println("Can not get paper url");
            }
            return null;
        }
	// ---Getters and Setters---
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public void update() {
		System.out.println("Changed");
	}

	public void attach(Observer observer) {
		this.observerList.add(observer);
	}

	public void detach(Observer observer) {
		this.observerList.remove(observer);
	}

	public void observerNotify() {
		for (Observer o : observerList) {
			o.update();
		}
	}

	public void getData() {

	}

	public String getProctorIP(ExamRecord examRecord) {
		Message msg = new Message(Message.GET_PROCTOR_IP);
		msg.addContent(examRecord);
		getMainApp().sendMessage(msg);

		Message reply;
		reply = getMainApp().getMessage();
		try {
			// get string from message
			String proctorIP = (String) reply.getContent().get(0);
			return proctorIP;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

	public boolean isAuthorised() {
		return isAuthorised;
	}

	private boolean getAuthorisedStatus(ExamRecord examRecord) {
		Message msg = new Message(Message.GET_AUTHORISED_STATUS);
		msg.addContent(examRecord);
		getMainApp().sendMessage(msg);
		Message reply;
		try {
			reply = getMainApp().getMessage();
			if (reply.getMessageCode() == 0) {
				isAuthorised = true;
				return true;
			} else
				return false;
		} catch (Exception e) {
			// Cannot get Authorises status
                        e.printStackTrace();
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Dialogs.showErrorDialog(null,
							"Could not receive message from server");
				}
			});
		}
		return false;
	}

}
