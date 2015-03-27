package Client.Proctor;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import ApplicationLayer.Controller;
import ApplicationLayer.Message;
import util.Communicator;

public class ExamViewController extends Controller {
	
	@FXML
	private SplitPane splitPane;
	@FXML
	private Button OkButton;
	@FXML
	private Button CancelButton;
	@FXML
	private ImageView webcamView1;
	@FXML 
	private ImageView screen1;
	@FXML
	private ImageView webcamView2;
	@FXML 
	private ImageView screen2;
        @FXML
        private Label statusLabel;
	private Stage dialogStage;
        boolean student1Connected;
        boolean student2Connected;
        boolean authenticateStudent1;
        boolean authenticateStudent2;
        Thread comm1;
        Thread comm2;
        List<ImageView> student1;
        List<ImageView> student2;

	@FXML
	private void initialize() {
		//webcamView1.fitWidthProperty().bind(pane1.widthProperty());
	    //pane1.setCenter(webcamView1);
		
		student1Connected = false;
		student2Connected = false;
		authenticateStudent1 = false;
		authenticateStudent2 = false;
		student1 = new ArrayList();
		student1.add(screen1);
		student1.add(webcamView1);
		student2 = new ArrayList();
		student2.add(screen2);
		student2.add(webcamView2);
		statusLabel.setText("Started");
	}
        
        public List<ImageView> getImageView(){
            if(!student1Connected){
                student1Connected = true;
                return student1;
            }
                
            else if(!student2Connected){
                student2Connected = true;
                return student2;
            }
            return null;
        }
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
        @FXML
	public void OkHandler()
	{
    	System.out.println("Open Connection");

	}
	@FXML
        private void handleAuthenticate1(){
            comm1.interrupt();
            System.out.println("Authenticated1");
        }
        
        @FXML
        private void handleAuthenticate2(){
            comm2.interrupt();
            System.out.println("Authenticated2");
        }
        public void setComm(Thread comm){
            if(!student1Connected){
                comm1=comm;
            }
            else if(!student2Connected){
                comm2=comm;
            }
        }

}
