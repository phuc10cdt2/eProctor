/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Client.Student;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ApplicationLayer.BookedCourse;
import ApplicationLayer.Controller;
import ApplicationLayer.Observer;
import DatabaseEntity.Course;
import DatabaseEntity.Student;

/**
 * FXML Controller class
 * 
 * @author phongnt
 */
public class ManageExamViewController extends Controller implements Observer {

	StudentController studentController;
	Student student;
	ObservableList<BookedCourse> coursesData;

	private Stage primaryStage;

	@FXML
	private TableView<BookedCourse> coursesTable;
	@FXML
	private TableColumn<BookedCourse, String> courseCodeColumn;
	@FXML
	private TableColumn<BookedCourse, String> courseNameColumn;
	@FXML
	private TableColumn<BookedCourse, Timestamp> courseDateColumn;
	@FXML
	private TextField courseField;
	@FXML
	private Button bookButton;
	@FXML
	private Button cancelButton;

	public ManageExamViewController() {
	}

	public void initialize() {
		courseCodeColumn
				.setCellValueFactory(new PropertyValueFactory<BookedCourse, String>(
						"courseCode"));
		courseNameColumn
				.setCellValueFactory(new PropertyValueFactory<BookedCourse, String>(
						"courseName"));
		courseDateColumn
				.setCellValueFactory(new PropertyValueFactory<BookedCourse, Timestamp>(
						"examDate"));
		coursesTable.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<BookedCourse>() {
					@Override
					public void changed(
							ObservableValue<? extends BookedCourse> observable,
							BookedCourse oldValue, BookedCourse newValue) {
						showCourse(newValue);
						toogleButton(newValue);
					}
				});
		coursesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	@FXML
	public void handleBook() {
		BookedCourse selectedCourse = coursesTable.getSelectionModel()
				.getSelectedItem();
		if (selectedCourse != null) {
			// exam time and exam date after booking returned here
			Date date = showBookDialog();
			if (date != null) {
				studentController.book(selectedCourse.getCourseCode(),
						new java.sql.Date(date.getTime()));
				toogleButton(selectedCourse);
			} else
				System.out.println("Booking canceled");
		} else {
			// Nothing selected
			noneSelected();
		}
	}

	@FXML
	public void handleCancel() {
		int selectedIndex = coursesTable.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			System.out.println("handle cancel " + studentController);
			System.out.println(studentController.getStudent());
			studentController.cancelBooking(coursesData.get(selectedIndex)
					.getCourseCode());
			toogleButton(coursesData.get(selectedIndex));
			System.out.println("course exam canceled...");
		} else {
			noneSelected();
		}
	}

	public long getTimeToExam() {
		long temp, min = 0;
		System.out.println("Course data size: " + coursesData.size());
		for (int i = 0; i < coursesData.size(); i++) {
			if (!coursesData.get(i).isBooked())
				continue;
			System.out.println(i);
			java.util.Date date = new java.util.Date();
			Timestamp currentTimestamp = new Timestamp(date.getTime());
			temp = coursesData.get(i).getExamDate().getTime()
					- currentTimestamp.getTime();
			if (i == 0)
				min = temp;
			if (temp < min)
				min = temp;
		}
		return min;
	}

	private Date showBookDialog() {
		BookedCourse selectedCourse = coursesTable.getSelectionModel()
				.getSelectedItem();
		try {
			// load the fxml file and create a new stage for the popup
			FXMLLoader loader = new FXMLLoader(
					Client.MainApp.class
							.getResource("View/Student/BookEditExamDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Exam Booking");
			dialogStage.getIcons().add(
					new Image("file:src/res/booking_icon.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// set the person into the controller
			BookEditExamController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.editDateAndTime(selectedCourse);
			// show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			return controller.getExamDate();

		} catch (IOException e) {
			// exception when the fxml file cannot be loaded
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					Dialogs.showErrorDialog(primaryStage,
							"BookEditExamDialog.fxml could not be loaded");
				}
			});
			return null;
		}

	}

	private void noneSelected() {
		Dialogs.showWarningDialog(studentController.getMainApp()
				.getPrimaryStage(), "Please select a course in the list",
				"No course selected", "Invalid selection");
	}

	private void showCourse(BookedCourse newValue) {
		if (newValue == null)
			courseField.setText(null);
		else
			courseField.setText(newValue.getCourseCode());
	}

	private void toogleButton(BookedCourse newValue) {
		System.out.println(studentController.getStudent());
		if (newValue == null) {
			bookButton.setDisable(true);
			cancelButton.setDisable(true);
		} else if (newValue.isBooked()) {
			bookButton.setDisable(true);
			cancelButton.setDisable(false);
		} else {
			bookButton.setDisable(false);
			cancelButton.setDisable(true);
		}
	}

	private ObservableList<BookedCourse> getCoursesData(Student student) {
		ObservableList coursesData = FXCollections.observableArrayList();
		for (Course c : student.getRegisteredCourses()) {
			BookedCourse bookedCourse = new BookedCourse(c.getCourseCode(),
					c.getCourseName(), student.getExamRecord());
			coursesData.add(bookedCourse);
		}
		return coursesData;
	}

	public void setController(StudentController studentController) {
		this.studentController = studentController;

	}

	public void refresh() {
		this.student = studentController.getStudent();
		this.coursesData = getCoursesData(student);
		coursesTable.setItems(coursesData);
	}

	public void update() {
		refresh();
		System.out.println(student.getExamRecord().size());
	}
}
