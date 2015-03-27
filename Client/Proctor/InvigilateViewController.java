package Client.Proctor;

import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import DatabaseEntity.ExamRecord;

public class InvigilateViewController {
	ProctorController proctorController;
	@FXML
	private TableView<ExamRecord> InvigilateTable;
	@FXML
	private TableColumn<ExamRecord, String> OnlineStudentColumn;
	@FXML
	private Label matricNo;
	@FXML
	private Label courseCode;
	@FXML
	private Label courseName;

	@FXML
	public void initialize() {
		InvigilateTable
				.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		showStudentInfo(null);
		InvigilateTable.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<ExamRecord>() {
					@Override
					public void changed(
							ObservableValue<? extends ExamRecord> observable,
							ExamRecord oldValue, ExamRecord newValue) {
						showStudentInfo(newValue);
					}
				});
	}

	@FXML
	public void startHandler() {
		int selectedIndex = -1;
		if (selectedIndex >= 0) {
			System.out.println("...connected");
		} else {
			// noneSelected();
			try {
				FXMLLoader loader = new FXMLLoader(
						Client.MainApp.class
								.getResource("View/Proctor/ExamView.fxml"));
				AnchorPane page = (AnchorPane) loader.load();
				ExamViewController controller = loader.getController();

				pServer pServer = new pServer(8000);
				pServer.setController(controller);
				pServer.setCommunicator(proctorController.getMainApp()
						.getCommunicator());
				System.out.println("set Controller");
				pServer.start();

				System.out.println("started pServer");

				Stage dialogStage = new Stage();
				dialogStage.getIcons().add(
						new Image("file:src/res/connect_icon.png"));
				dialogStage.setTitle("Connect To Student");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				Scene scene = new Scene(page);
				dialogStage.setScene(scene);
				controller.setDialogStage(dialogStage);
				Screen screen = Screen.getPrimary();
				Rectangle2D bounds = screen.getVisualBounds();

				dialogStage.setX(bounds.getMinX());
				dialogStage.setY(bounds.getMinY());
				dialogStage.setWidth(bounds.getWidth());
				dialogStage.setHeight(bounds.getHeight());
				// dialogStage.setMaximized(true);
				dialogStage.showAndWait();
			} catch (IOException e) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						Dialogs.showErrorDialog(null,
								"ExamView.fxml could not be loaded.");
					}
				});
			}
		}
	}

	private void showStudentInfo(ExamRecord student) {
		if (student != null) {
			matricNo.setText(student.getStudentMatric());
			courseCode.setText(student.getCourseCode());
			courseName.setText("");
		} else {
			matricNo.setText("");
			courseCode.setText("");
			courseName.setText("");
		}
	}

	private void noneSelected() {
		Dialogs.showWarningDialog(new Stage(),
				"Please select a course in the list", "No course selected",
				"Invalid selection");
	}

	public void setController(ProctorController controller) {
		this.proctorController = controller;
	}
}
