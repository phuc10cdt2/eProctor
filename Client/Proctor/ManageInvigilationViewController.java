package Client.Proctor;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import ApplicationLayer.Controller;
import ApplicationLayer.Message;
import DatabaseEntity.ExamRecord;
import DatabaseEntity.Proctor;

public class ManageInvigilationViewController extends Controller {
	private Proctor proctor;
	private ProctorController proctorController;

	@FXML
	private TableView<ExamRecord> ManageInvigilationTable;
	@FXML
	private TableColumn<ExamRecord, String> DateColumn;
	@FXML
	private TableColumn<ExamRecord, String> StudentColumn;
	@FXML
	private TableColumn<ExamRecord, String> ManageColumn;

	@FXML
	public void initialize() {
		System.out.println("Manage Invigilation View called.");
		DateColumn
				.setCellValueFactory(new Callback<CellDataFeatures<ExamRecord, String>, ObservableValue<String>>() {
					@Override
					public ObservableValue<String> call(
							CellDataFeatures<ExamRecord, String> p) {
						String displayDate = new SimpleDateFormat(
								"dd/MM/yyyy hh:mm").format(p.getValue()
								.getExamDate());
						ReadOnlyStringWrapper sp = new ReadOnlyStringWrapper(
								displayDate);
						return sp;
					}
				});
		// DateColumn.setCellValueFactory(new PropertyValueFactory<ExamRecord,
		// String>("examDate"));
		StudentColumn
				.setCellValueFactory(new PropertyValueFactory<ExamRecord, String>(
						"studentMatric"));
		ManageColumn
				.setCellValueFactory(new PropertyValueFactory<ExamRecord, String>(
						"courseCode"));
		ManageInvigilationTable
				.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	@FXML
	void bookNewHandler() {
		System.out.println("This is new.");
		try {
			FXMLLoader loader = new FXMLLoader(
					Client.MainApp.class
							.getResource("View/Proctor/bookNewDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.getIcons().add(
					new Image("file:src/res/booking_icon.png"));
			dialogStage.setTitle("Edit Exam Booking");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			BookNewDialogController controller = loader.getController();
			controller.setManageInvigilationViewController(this);
			// RECEIVE NEW LIST FROM SERVER

			Message sendMessage = new Message();
			sendMessage.setMessageCode(Message.GET_NEW_INVIGILATION_BOOK_LIST);
			this.proctorController.getMainApp().sendMessage(sendMessage);
			System.out.println("Start waiting...");
			Message receiveMessage = this.proctorController.getMainApp()
					.getMessage();
			System.out.println("Message received 1.");
			System.out.println(receiveMessage.getContent().get(0).getClass());
			System.out.println("Code: " + receiveMessage.getMessageCode());
			System.out.println(receiveMessage.getContent().size());

			ArrayList<ExamRecord> newBookList = (ArrayList<ExamRecord>) receiveMessage
					.getContent().get(0);
			System.out.println("Message received 2.");

			// Hard code here

			// ArrayList<ExamRecord> newBookList = new ArrayList();
			// ExamRecord examRecord1 = new ExamRecord(1,
			// Timestamp.valueOf("2014-05-25 12:00:00"), "", "U125", "None",
			// "CZ2001");
			// ExamRecord examRecord2 = new ExamRecord(2,
			// Timestamp.valueOf("2014-05-09 17:00:00"), "", "U126", "None",
			// "CZ3003");
			// newBookList.add(examRecord1);
			// newBookList.add(examRecord2);

			// End of hard code
			controller.setNewBookList(newBookList);
			controller.process();
			controller.setDialogStage(dialogStage);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Dialogs.showErrorDialog(null,
							"bookNewDialog.fxml could not be loaded.");
				}
			});
		}
	}

	@FXML
	void cancelHandler() {
		int selectedIndex = ManageInvigilationTable.getSelectionModel()
				.getSelectedIndex();
		if (selectedIndex >= 0) {
			Dialogs.DialogResponse response = Dialogs.showConfirmDialog(
					proctorController.getMainApp().getPrimaryStage(),
					"Cancel invigilation book?", "Cancel confirmation",
					"Select an option", Dialogs.DialogOptions.YES_NO);
			if (response == Dialogs.DialogResponse.YES) {
				bookedStudentCancel(selectedIndex);
			}
		} else {
			noneSelected();
		}
	}

	private void bookedStudentCancel(int selectedIndex) {
		// Send message

		Message newMessage = new Message();
		newMessage.setMessageCode(Message.CANCEL_INVIGILATION_BOOK);
		newMessage.addContent(proctor.getExamRecord().get(selectedIndex));
		this.proctorController.getMainApp().sendMessage(newMessage);

		Message ackMessage = this.proctorController.getMainApp().getMessage();
		if (ackMessage.getMessageCode() > 0) {
			proctor.getExamRecord().remove(selectedIndex);
			ManageInvigilationTable.setItems(getBookedStudentData());
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Dialogs.showErrorDialog(null, "Could not cancel on server");
				}
			});
		}
	}

	private void noneSelected() {
		Dialogs.showWarningDialog(proctorController.getMainApp()
				.getPrimaryStage(), "Please select a student in the list",
				"No course selected", "Invalid selection");
	}

	public void addBookedStudent(ExamRecord newBookedRecord) {
		newBookedRecord.setProctorID(proctor.getProctorID());
		// Send message

		Message newMessage = new Message();
		newMessage.setMessageCode(Message.NEW_INVIGILATION_BOOK);
		newMessage.addContent(newBookedRecord);
		this.proctorController.getMainApp().sendMessage(newMessage);
		// Acknowledgement
		Message ackMessage = this.proctorController.getMainApp().getMessage();
		if (ackMessage.getMessageCode() > 0) {
			proctor.getExamRecord().add(newBookedRecord);
			ManageInvigilationTable.setItems(getBookedStudentData());
		} else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Dialogs.showErrorDialog(null, "Could not book on server");
				}
			});
		}

	}

	public void setController(ProctorController proctorController) {
		this.proctorController = proctorController;
	}

	public ProctorController getProctorController() {
		return this.proctorController;
	}

	public void setProctor(Proctor proctor) {
		this.proctor = proctor;
	}

	private ObservableList<ExamRecord> getBookedStudentData() {
		ObservableList data = FXCollections.observableArrayList();
		for (int i = 0; i < proctor.getExamRecord().size(); i++) {
			data.add(proctor.getExamRecord().get(i));
		}
		return data;
	}

	public void process() {
		System.out.println("Manage Invigilation Process called");
		ManageInvigilationTable.setItems(getBookedStudentData());
	}

	public long getTimeToExam() {
		long temp, min = 0;
		List<ExamRecord> records = proctor.getExamRecord();
		for (int i = 0; i < records.size(); i++) {
			java.util.Date date = new java.util.Date();
			Timestamp currentTimestamp = new Timestamp(date.getTime());
			temp = records.get(i).getExamDate().getTime()
					- currentTimestamp.getTime();
			if (i == 0)
				min = temp;
			if (temp < min)
				min = temp;
		}
		return min;
	}
}
