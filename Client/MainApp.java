package Client;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialogs;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import util.BaseCommunicator;
import util.Communicator;
import ApplicationLayer.Controller;
import ApplicationLayer.Message;
import Client.Proctor.ProctorController;
import Client.Student.StudentController;
import DatabaseEntity.Proctor;
import DatabaseEntity.Student;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;
	private BaseCommunicator communicator;

	public MainApp() {
		String host = "172.22.228.210";
		int port = 8008;
		communicator = new BaseCommunicator(host, port);
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("eProctor");
		this.primaryStage.getIcons().add(
				new Image("file:src/res/globe_icon.png"));

		try {
			// Load the root layout from the fxml file
			FXMLLoader loader = new FXMLLoader(
					MainApp.class.getResource("View/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			Dialogs.showErrorDialog(primaryStage,
					"RootLayout.fxml could not be loaded.");
		}

		showLogin();
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void showLogin() {
		try {
			FXMLLoader loader = new FXMLLoader(
					MainApp.class.getResource("View/Login.fxml"));
			AnchorPane loginPage = (AnchorPane) loader.load();
			rootLayout.setCenter(loginPage);
			primaryStage.show();
			Controller controller = loader.getController();
			controller.setMainApp(this);
		} catch (IOException e) {
			Dialogs.showErrorDialog(primaryStage,
					"Login.fxml could not be loaded.");
		}
	}

	public void showStudentCP(Student student) {
		try {
			FXMLLoader loader = new FXMLLoader(
					MainApp.class.getResource("View/Student.fxml"));
			AnchorPane studentPage = (AnchorPane) loader.load();
			rootLayout.setCenter(studentPage);
			primaryStage.getIcons().add(new Image("file:src/res/student.png"));
			StudentController controller = loader.getController();
			controller.setMainApp(this);
			controller.setStudent(student);
			controller.start();
		} catch (IOException e) {
			e.printStackTrace();
			Dialogs.showErrorDialog(primaryStage,
					"Student.fxml could not be loaded.");
		}
	}

	public void showProctorCP(Proctor proctor) {
		try {
			FXMLLoader loader = new FXMLLoader(
					MainApp.class.getResource("View/Proctor.fxml"));
			AnchorPane proctorPage = (AnchorPane) loader.load();
			rootLayout.setCenter(proctorPage);
			primaryStage.getIcons().add(new Image("file:src/res/staff.png"));
			ProctorController controller = loader.getController();

			// proctor get from message with server
			// proctor = new Proctor("007", "1.0.0.1", "MinhTri", "12345",
			// "Tri", "Tran", Date.valueOf("1994-03-25"));
			// end of dumb code

			controller.setMainApp(this);
			controller.setProctor(proctor);
			controller.process();
		} catch (IOException e) {
			Dialogs.showErrorDialog(primaryStage,
					"Proctor.fxml could not be loaded.");
		}
	}

	public static void main(String[] args) {

		launch(args);

	}

	public Communicator getCommunicator() {
		return communicator;
	}

	public void sendMessage(Message msg) {
		communicator.sendMessage(msg);
	}

	public Message getMessage() {
		return communicator.getMessage();
	}
}
