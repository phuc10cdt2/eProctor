package Client.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import DatabaseEntity.Course;
import eu.schudt.javafx.controls.calendar.DatePicker;

public class BookEditExamController {

	@FXML
	private Label courseCodeLabel;
	@FXML
	private Label courseNameLabel;
	@FXML
	private GridPane gridPane;
	private DatePicker datePicker;
	@FXML
	private ComboBox timeSelectComboBox;

	private Stage primaryStage;
	private Stage dialogStage;
	private boolean okClicked = false;
	private Date examTime = null;
	private Date examDate = null;

	@FXML
	private void initialize() {
		// initialize DatePicker
		datePicker = new DatePicker(Locale.ENGLISH);
		datePicker.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
		datePicker.getCalendarView().todayButtonTextProperty().set("Today");
		datePicker.getCalendarView().setShowWeeks(false);
		datePicker.getStylesheets().add("res/DatePicker.css");

		// add DatePicker to grid
		gridPane.add(datePicker, 1, 1);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public void editDateAndTime(Course course) {
		courseCodeLabel.setText(course.getCourseCode());
		courseNameLabel.setText(course.getCourseName());
		ObservableList<String> options = FXCollections.observableArrayList(
				"10:30:00", "12:00:00", "14:00:00");
		timeSelectComboBox.setItems(options);
		timeSelectComboBox.setValue("10:30:00");
	}

	@FXML
	private void handleOk() throws ParseException {
		System.out.println("Book/Edit done!");
		examDate = datePicker.getSelectedDate();
		examTime = new SimpleDateFormat("HH:mm:ss").parse(timeSelectComboBox
				.getSelectionModel().getSelectedItem().toString());
		Calendar c = new GregorianCalendar();
		if (getExamDate().before(c.getTime())) {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {

					Dialogs.showErrorDialog(primaryStage,
							"Can't book the day before today");
                                        examTime = null;
				}
			});
		} else {
			okClicked = true;
			dialogStage.close();
		}
	}

	// }
	public Date getExamDate() {
		Date newDate = new Date();
		newDate.setTime(examTime.getTime());
		newDate.setYear(examDate.getYear());
		newDate.setMonth(examDate.getMonth());
		newDate.setDate(examDate.getDate());
		return newDate;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
