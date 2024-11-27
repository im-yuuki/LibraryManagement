package assignment.librarymanager.functions;

import assignment.librarymanager.data.Borrowing;
import assignment.librarymanager.managers.BorrowingStorage;
import assignment.librarymanager.managers.DocumentStorage;
import assignment.librarymanager.utils.AlertPopup;
import assignment.librarymanager.utils.TimeUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class NewBorrowingForm implements FormInterface {

	private final DocumentStorage documentStorage;
	private final BorrowingStorage borrowingStorage;
	private final Stage popup;
	private final int userModeId;

	@FXML
	public TextField documentIdField;

	@FXML
	public TextField readerIdField;

	@FXML
	public DatePicker dueDateField;

	public NewBorrowingForm(DocumentStorage documentStorage, BorrowingStorage borrowingStorage, int documentId, int readerId, boolean userMode) throws IOException {
		this.documentStorage = documentStorage;
		this.borrowingStorage = borrowingStorage;
		if (userMode) userModeId = readerId;
		else userModeId = -1;
		FXMLLoader root = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/new_borrowing.fxml")));
		root.setController(this);
		popup = new Stage();
		popup.setScene(new Scene(root.load()));
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initStyle(StageStyle.UTILITY);
		popup.setResizable(false);
		popup.setTitle("Borrow");
		documentIdField.setTextFormatter(new TextFormatter<String>(change -> {
			String text = change.getControlNewText();
			if (text.matches("\\d*")) {
				return change;
			}
			return null;
		}));
		if (userMode) readerIdField.setEditable(false);
		else readerIdField.setTextFormatter(new TextFormatter<String>(change -> {
			String text = change.getControlNewText();
			if (text.matches("\\d*")) {
				return change;
			}
			return null;
		}));
		if (documentId > 0) documentIdField.setText(String.valueOf(documentId));
		if (readerId > 0) readerIdField.setText(String.valueOf(readerId));
		dueDateField.setValue(TimeUtils.toLocalDate(TimeUtils.getTimestamp() + 604800));
		popup.showAndWait();
	}

	@Override
	public void onSubmit() {
		try {
			long dueTime = TimeUtils.fromLocalDate(dueDateField.getValue());
			if (dueTime < TimeUtils.getTimestamp()) {
				AlertPopup.open("Invalid input", "Due date must be in the future");
				return;
			}
			Borrowing borrowing = new Borrowing(
					Integer.parseInt(readerIdField.getText()),
					Integer.parseInt(documentIdField.getText()),
					dueTime
			);
			if (userModeId != -1 && userModeId != borrowing.getReaderId()) {
				AlertPopup.open("Invalid input", "You can only borrow for yourself");
				return;
			}
			ArrayList<Borrowing> borrowings = borrowingStorage.findBorrowings(borrowing.getDocumentId(), -1, false, false);
			if (borrowings.size() >= Objects.requireNonNull(documentStorage.getEntry(borrowing.getDocumentId())).getQuantity()) {
				AlertPopup.open("Invalid input", "Document is out of stock");
				return;
			}
			borrowingStorage.setEntry(borrowing);
			popup.close();
		} catch (NumberFormatException e) {
			AlertPopup.open("Invalid input", "Please enter valid numbers");
		} catch (Exception e) {
			AlertPopup.open("Error", e.getMessage());
			popup.close();
		}
	}

	@Override
	public void onCancel() {
		popup.close();
	}

}
