package assignment.librarymanager.functions;

import assignment.librarymanager.data.Reader;
import assignment.librarymanager.managers.ReaderStorage;
import assignment.librarymanager.utils.AlertPopup;
import assignment.librarymanager.utils.PasswordHash;
import assignment.librarymanager.utils.TimeUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class EditReaderForm implements FormInterface {

	private final ReaderStorage readerStorage;
	private final Stage popup;

	@Nullable
	private final Reader reader;

	@FXML
	public TextField nameField;

	@FXML
	public PasswordField passwordField;

	@FXML
	public TextField emailField;

	@FXML
	public TextField phoneNumberField;

	@FXML
	public DatePicker expDateField;

	public EditReaderForm(ReaderStorage readerStorage, @Nullable Reader reader) throws IOException {
		this.reader = reader;
		this.readerStorage = readerStorage;

		FXMLLoader root = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/edit_reader.fxml")));
		root.setController(this);
		popup = new Stage();
		popup.setScene(new Scene(root.load()));
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initStyle(StageStyle.UTILITY);
		popup.setResizable(false);
		if (reader == null) {
			popup.setTitle("Create reader");
			expDateField.setValue(TimeUtils.toLocalDate(TimeUtils.getTimestamp() + 31536000));
		} else {
			popup.setTitle("Edit reader " + reader.getId());
			nameField.setText(reader.getName());
			emailField.setText(reader.getEmail());
			phoneNumberField.setText(reader.getPhoneNumber());
			expDateField.setValue(TimeUtils.toLocalDate(reader.getExpirationTime()));
		}
		phoneNumberField.setTextFormatter(new TextFormatter<String>(change -> {
			String text = change.getControlNewText();
			if (text.matches("\\d*")) {
				return change;
			}
			return null;
		}));
		popup.showAndWait();
	}

	@Override
	public void onSubmit() {
		String name = nameField.getText();
		if (name.isBlank()) {
			AlertPopup.open("Error", "Name cannot be empty");
			return;
		}
		String password = passwordField.getText();
		if (password.isBlank() && (reader == null || reader.getId() == -1)) {
			AlertPopup.open("Error", "Password cannot be empty");
			return;
		}
		String email = emailField.getText();
		if (email.isBlank()) {
			AlertPopup.open("Error", "Email cannot be empty");
			return;
		}
		String phoneNumber = phoneNumberField.getText();
		if (phoneNumber.isBlank()) {
			AlertPopup.open("Error", "Phone number cannot be empty");
			return;
		}
		long expireTime = TimeUtils.fromLocalDate(expDateField.getValue());
		if (expireTime < TimeUtils.getTimestamp()) {
			AlertPopup.open("Error", "You cannot set expiration time in the past");
			return;
		}
		Reader newReader;
		if (reader == null) {
			newReader = new Reader(name, password, expireTime, email, phoneNumber);
		} else {
			if (password.isBlank()) {
				password = reader.getPasswordHash();
			} else {
				password = PasswordHash.hash(password);
			}
			newReader = new Reader(reader.getId(), name, password, reader.getRegistrationTime(), expireTime, email, phoneNumber);
		}
		try {
			readerStorage.setEntry(newReader);
		} catch (Exception e) {
			AlertPopup.open("Error", e.getMessage());
			return;
		}
		popup.close();
	}

	@Override
	public void onCancel() {
		popup.close();
	}

}
