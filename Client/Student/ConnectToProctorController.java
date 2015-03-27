package Client.Student;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ConnectToProctorController {
	
	@FXML
	private ImageView webcamFrame;
	@FXML
	private Label statusLabel;
	@FXML
	private Button OkButton;
	@FXML
	private Button CancelButton;
	
	private Stage dialogStage;
	
	@FXML
	private void initialize() {
		
		statusLabel.setText("Connecting...");
		
	}
	
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
        
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
        
        

}
