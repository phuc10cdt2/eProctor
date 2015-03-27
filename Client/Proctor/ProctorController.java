/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.Proctor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import ApplicationLayer.Controller;
import ApplicationLayer.Message;
import Client.MainApp;
import DatabaseEntity.Proctor;
import DatabaseEntity.User;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * 
 * @author phongnt
 */
public class ProctorController extends Controller {

	private Proctor proctor;
	private MainApp mainApp;
	@FXML
	private AnchorPane myProfileView;
	@FXML
	private MyProfileViewController myProfileViewController;
	@FXML
	private AnchorPane invigilateView;
	@FXML
	private InvigilateViewController invigilateViewController;
	@FXML
	private AnchorPane manageInvigilationView;
	@FXML
	private ManageInvigilationViewController manageInvigilationViewController;
	@FXML
	private Button logOutButton;
        
        @FXML
        private Label timerLabel;
        
        final CountdownTimer countdownClock = new CountdownTimer(0);

	@FXML
	private void initialize() {
		System.out.println("Initialize PC called.");
	}

	public ProctorController() {

	}

	// ---Getters and Setters---
	public Proctor getProctor() {
		return proctor;
	}

	public void setProctor(Proctor proctor) {
		this.proctor = proctor;
	}

	@FXML
	private void processLogOut(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
                countdownClock.stop();
		getMainApp().showLogin();
		Message msg = new Message(Message.PROCTOR_LOG_OUT);
		msg.addContent((User)proctor);
		getMainApp().sendMessage(msg);

	}

	public void process() {
		myProfileViewController.setController(this);
		// invigilateViewController.setController(this);
		// Hard code for proctor
		/*
		 * proctor = new Proctor("S007", "1.0.0.1", "Tri", "12345", "Tri",
		 * "Tran", Date.valueOf("1994-03-25")); ExamRecord examRecord1 = new
		 * ExamRecord(1, Timestamp.valueOf("2014-04-11 10:00:00"), "S007",
		 * "U124", "None", "CZ2006"); ExamRecord examRecord2 = new ExamRecord(2,
		 * Timestamp.valueOf("2014-04-05 12:00:00"), "S007", "U129", "None",
		 * "CZ3006"); proctor.getExamRecord().add(examRecord1);
		 * proctor.getExamRecord().add(examRecord2);
		 */
		// end of hard cord
                invigilateViewController.setController(this);
		manageInvigilationViewController.setController(this);
		manageInvigilationViewController.setProctor(proctor);
		manageInvigilationViewController.process();
//                long temp = manageInvigilationViewController.getTimeToExam();
                
                countdownClock.start();
	}
	// --------------------------

          
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
                long temp = manageInvigilationViewController.getTimeToExam();
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
}
