package assignment.librarymanager.functions;

import assignment.librarymanager.data.Borrowing;
import assignment.librarymanager.data.Document;
import assignment.librarymanager.data.Reader;
import assignment.librarymanager.managers.BorrowingStorage;
import assignment.librarymanager.managers.Database;
import assignment.librarymanager.managers.DocumentStorage;
import assignment.librarymanager.managers.ReaderStorage;
import assignment.librarymanager.utils.AlertPopup;
import assignment.librarymanager.utils.QRGenerator;
import assignment.librarymanager.utils.TimeUtils;
import com.google.zxing.WriterException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class AdminBoard {

	private final DocumentStorage documentStorage;
	private final ReaderStorage readerStorage;
	private final BorrowingStorage borrowingStorage;

	private int getSelectedDocumentId() {
		try {
			return Integer.parseInt(documentIdInput.getText());
		} catch (Exception e) {
			return -1;
		}
	}

	private int getSelectedReaderId() {
		try {
			return Integer.parseInt(readerIdInput.getText());
		} catch (Exception e) {
			return -1;
		}
	}

	private int getSelectedBorrowingId() {
		try {
			return Integer.parseInt(borrowingIdInput.getText());
		} catch (Exception e) {
			return -1;
		}
	}

	public void updateDocumentDescription() {
		if (getSelectedDocumentId() < 1) {
			documentIdInput.clear();
			documentDescription.setVisible(false);
			return;
		}
		documentIdInput.setText(String.valueOf(getSelectedDocumentId()));
		documentDescription.setVisible(true);
		try {
			Document document = documentStorage.getEntry(getSelectedDocumentId());
			if (document == null) {
				AlertPopup.open(
						"Document not found",
						"The document with ID " + getSelectedDocumentId() + " was not exist in the database"
				);
				documentIdInput.clear();
				updateDocumentDescription();
				return;
			}
			documentIdDisplay.setText(String.valueOf(document.getId()));
			documentNameDisplay.setText(document.getName());
			documentQtyDisplay.setText(String.valueOf(document.getQuantity()));
			documentAuthorDisplay.setText(document.getAuthor());
			documentPublisherDisplay.setText(document.getPublisher());
			documentCategoryDisplay.setText(document.getCategory());
			qrCode.setImage(QRGenerator.generateQRCode(document.represent()));
		} catch (SQLException e) {
			AlertPopup.open("Error", e.getMessage());
			documentIdInput.clear();
			updateDocumentDescription();
		}
		catch (IOException | WriterException e) {
			AlertPopup.open("Generate QR code failed", e.getMessage());
		}
	}

	@FXML
	public TextField documentNameFilter;

	@FXML
	public TextField documentAuthorFilter;

	@FXML
	public TextField documentPublisherFilter;

	@FXML
	public TextField documentCategoryFilter;

	@FXML
	public TableView<Document> documentsTable;

	@FXML
	public TableColumn<Document, Integer> documentIdColumn;

	@FXML
	public TableColumn<Document, String> documentNameColumn;

	@FXML
	public TableColumn<Document, String> documentAuthorColumn;

	@FXML
	public TableColumn<Document, String> documentPublisherColumn;

	@FXML
	public TableColumn<Document, String> documentCategoryColumn;

	@FXML
	public TextField documentIdInput;

	@FXML
	public VBox documentDescription;

	@FXML
	public ImageView qrCode;

	@FXML
	public Text documentIdDisplay;

	@FXML
	public Text documentNameDisplay;

	@FXML
	public Text documentQtyDisplay;

	@FXML
	public Text documentAuthorDisplay;

	@FXML
	public Text documentPublisherDisplay;

	@FXML
	public Text documentCategoryDisplay;

	@FXML
	public TextField readerNameFilter;

	@FXML
	public TextField readerEmailFilter;

	@FXML
	public TextField readerPhoneNumberFilter;

	@FXML
	public CheckBox readerNotExpiredFilter;

	@FXML
	public TableView<Reader> readersTable;

	@FXML
	public TableColumn<Reader, Integer> readerIdColumn;

	@FXML
	public TableColumn<Reader, String> readerNameColumn;

	@FXML
	public TableColumn<Reader, String> readerRegistrationColumn;

	@FXML
	public TableColumn<Reader, String> readerExpirationColumn;

	@FXML
	public TableColumn<Reader, String> readerEmailColumn;

	@FXML
	public TableColumn<Reader, String> readerPhoneNumberColumn;

	@FXML
	public TextField readerIdInput;

	@FXML
	public TextField borrowingDocumentFilter;

	@FXML
	public TextField borrowingReaderFilter;

	@FXML
	public CheckBox borrowingOverdueOnlyFilter;

	@FXML
	public CheckBox borrowingHistoryFilter;

	@FXML
	public TableView<Borrowing> borrowingsTable;

	@FXML
	public TableColumn<Borrowing, Integer> borrowingIdColumn;

	@FXML
	public TableColumn<Borrowing, Integer> borrowingDocumentColumn;

	@FXML
	public TableColumn<Borrowing, Integer> borrowingReaderColumn;

	@FXML
	public TableColumn<Borrowing, String> borrowingBorrowTimeColumn;

	@FXML
	public TableColumn<Borrowing, String> borrowingDueTimeColumn;

	@FXML
	public TableColumn<Borrowing, String> borrowingReturnTimeColumn;

	@FXML
	public TextField borrowingIdInput;

	public AdminBoard(Stage stage) throws IOException, SQLException {
		Database database = new Database();
		documentStorage = new DocumentStorage(database);
		readerStorage = new ReaderStorage(database);
		borrowingStorage = new BorrowingStorage(database);

		FXMLLoader root = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/admin.fxml")));
		root.setController(this);
		Scene scene = new Scene(root.load());
		stage.setScene(scene);

		documentIdInput.setTextFormatter(new TextFormatter<String>(change -> {
			String text = change.getControlNewText();
			if (text.matches("\\d*")) {
				return change;
			}
			return null;
		}));

		documentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		documentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		documentAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		documentPublisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		documentCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));

		documentsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				documentIdInput.clear();
			} else {
				documentIdInput.setText(String.valueOf(newValue.getId()));
			}
			updateDocumentDescription();
		});

		readerIdInput.setTextFormatter(new TextFormatter<String>(change -> {
			String text = change.getControlNewText();
			if (text.matches("\\d*")) {
				return change;
			}
			return null;
		}));

		readerIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		readerNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		readerRegistrationColumn.setCellValueFactory(new PropertyValueFactory<>("registrationTimeFormatted"));
		readerExpirationColumn.setCellValueFactory(new PropertyValueFactory<>("expirationTimeFormatted"));
		readerEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
		readerPhoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

		readersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				readerIdInput.clear();
			} else {
				readerIdInput.setText(String.valueOf(newValue.getId()));
			}
		});

		borrowingIdInput.setTextFormatter(new TextFormatter<String>(change -> {
			String text = change.getControlNewText();
			if (text.matches("\\d*")) {
				return change;
			}
			return null;
		}));

		borrowingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		borrowingDocumentColumn.setCellValueFactory(new PropertyValueFactory<>("documentId"));
		borrowingReaderColumn.setCellValueFactory(new PropertyValueFactory<>("readerId"));
		borrowingBorrowTimeColumn.setCellValueFactory(new PropertyValueFactory<>("borrowTimeFormatted"));
		borrowingDueTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dueTimeFormatted"));
		borrowingReturnTimeColumn.setCellValueFactory(new PropertyValueFactory<>("returnTimeFormatted"));

		borrowingsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null) {
				borrowingIdInput.clear();
			} else {
				borrowingIdInput.setText(String.valueOf(newValue.getId()));
			}
		});

		filterDocumentsAction();
		filterReadersAction();
		filterBorrowingsAction();
	}

	public void exitAction() {
		Platform.exit();
	}

	public void showTutorials() {
		AlertPopup.open("Tutorials", """
				This is a simple library management system
				1. Create a document by clicking the 'Create document' button in the menu
				2. Edit a document by selecting a document from the list and clicking the 'Edit document' button
				3. Delete a document by selecting a document from the list and clicking the 'Delete document' button
				4. Create a reader by clicking the 'Create reader account' button in the menu
				5. Edit a reader by selecting a reader from the list and clicking the 'Edit reader' button
				6. Delete a reader by selecting a reader from the list and clicking the 'Delete reader' button
				7. Filter documents and readers by entering the search criteria and clicking the 'Filter' button
				8. Borrow a document by selecting a reader and a document and clicking the 'Borrow' button
				9. Return a document by select a borrowing and clicking the 'Return' button
				"""
		);
	}

	public void showAbout() {
		AlertPopup.open("About", """
				This application is developed by a (group) of students
				Databases design: Yuuki
				Logical operations: Yuuki
				User interface: Yuuki
				...
				Everything: Yuuki
				For more information, please contact Yuuki :)
				"""
		);
	}

	public void createDocumentAction() {
		try {
			new EditDocumentForm(documentStorage, null);
			filterDocumentsAction();
		} catch (Exception e) {
			e.printStackTrace();
			AlertPopup.open("An error occurred while creating a document", e.getMessage());
		}
	}

	public void filterDocumentsAction() {
		String name = documentNameFilter.getText();
		String author = documentAuthorFilter.getText();
		String publisher = documentPublisherFilter.getText();
		String category = documentCategoryFilter.getText();

		try {
			ArrayList<Document> documents = documentStorage.queryDocuments(name, author, publisher, category);
			ObservableList<Document> data = FXCollections.observableArrayList(documents);
			documentsTable.setItems(data);
		} catch (SQLException e) {
			AlertPopup.open("Error", e.getMessage());
		}
	}

	public void editDocumentAction() {
		if (getSelectedDocumentId() < 1) {
			AlertPopup.open("No document selected", "Please select a document to edit");
			return;
		}
		try {
			Document document = documentStorage.getEntry(getSelectedDocumentId());
			if (document == null) {
				AlertPopup.open(
						"Document not found",
						"The document with ID " + getSelectedDocumentId() + " was not exist in the database"
				);
				documentIdInput.clear();
			} else {
				new EditDocumentForm(documentStorage, document);
				filterDocumentsAction();
			}
		} catch (Exception e) {
			AlertPopup.open("Error", e.getMessage());
		}
		updateDocumentDescription();
	}

	public void deleteDocumentAction() {
		if (getSelectedDocumentId() < 1) {
			AlertPopup.open("No document selected", "Please select a document to delete");
			return;
		}
		if (documentStorage.deleteEntry(getSelectedDocumentId())) {
			filterDocumentsAction();
			filterBorrowingsAction();
		} else {
			AlertPopup.open("Document not found", "The document with ID " + getSelectedDocumentId() + " was not exist in the database");
		}
		documentIdInput.clear();
		updateDocumentDescription();
	}

	public void createReaderAction() {
		try {
			new EditReaderForm(readerStorage, null);
			filterReadersAction();
		} catch (IOException e) {
			e.printStackTrace();
			AlertPopup.open("An error occurred while creating a reader", e.getMessage());
		}
	}

	public void filterReadersAction() {
		String name = readerNameFilter.getText();
		String email = readerEmailFilter.getText();
		String phoneNumber = readerPhoneNumberFilter.getText();
		boolean notExpired = readerNotExpiredFilter.isSelected();

		try {
			ArrayList<Reader> readers = readerStorage.getAllReaders(name, email, phoneNumber, notExpired);
			ObservableList<Reader> data = FXCollections.observableArrayList(readers);
			readersTable.setItems(data);
		} catch (SQLException e) {
			AlertPopup.open("Error", e.getMessage());
		}
	}

	public void editReaderAction() {
		if (getSelectedReaderId() < 1) {
			AlertPopup.open("No reader selected", "Please select a reader to edit");
			return;
		}
		try {
			Reader reader = readerStorage.getEntry(getSelectedReaderId());
			if (reader == null) {
				AlertPopup.open(
						"Reader not found",
						"The reader with ID " + getSelectedReaderId() + " was not exist in the database"
				);
				readerIdInput.clear();
			} else {
				new EditReaderForm(readerStorage, reader);
				filterReadersAction();
			}
		} catch (Exception e) {
			AlertPopup.open("Error", e.getMessage());
		}
	}

	public void deleteReaderAction() {
		if (getSelectedReaderId() < 1) {
			AlertPopup.open("No reader selected", "Please select a reader to delete");
			return;
		}
		if (readerStorage.deleteEntry(getSelectedReaderId())) {
			filterReadersAction();
			filterBorrowingsAction();
		} else {
			AlertPopup.open("Reader not found", "The reader with ID " + getSelectedReaderId() + " was not exist in the database");
		}
		readerIdInput.clear();
	}

	public void filterBorrowingsAction() {
		int documentId = -1;
		int readerId = -1;
		boolean overdueOnly = borrowingOverdueOnlyFilter.isSelected();
		boolean history = borrowingHistoryFilter.isSelected();

		try {
			if (!borrowingDocumentFilter.getText().isBlank()) {
				documentId = Integer.parseInt(borrowingDocumentFilter.getText());
			}
			if (!borrowingReaderFilter.getText().isBlank()) {
				readerId = Integer.parseInt(borrowingReaderFilter.getText());
			}
		} catch (NumberFormatException e) {
			AlertPopup.open("Invalid input", "Please enter a valid number for document ID and reader ID");
			return;
		}

		try {
			ArrayList<Borrowing> borrowings = borrowingStorage.findBorrowings(documentId, readerId, overdueOnly, history);
			ObservableList<Borrowing> data = FXCollections.observableArrayList(borrowings);
			borrowingsTable.setItems(data);
		} catch (SQLException e) {
			AlertPopup.open("Error", e.getMessage());
		}
	}

	public void borrowDocumentAction() {
		try {
			new NewBorrowingForm(documentStorage, borrowingStorage, getSelectedDocumentId(), getSelectedReaderId(), false);
			filterBorrowingsAction();
		} catch (IOException e) {
			AlertPopup.open("An error occurred while borrowing a document", e.getMessage());
		}
	}

	public void returnDocumentAction() {
		if (getSelectedBorrowingId() < 1) {
			AlertPopup.open("No borrowing selected", "Please select a borrowing to return");
			return;
		}
		try {
			Borrowing borrowing = borrowingStorage.getEntry(getSelectedBorrowingId());
			if (borrowing == null) {
				AlertPopup.open(
						"Borrowing not found",
						"The borrowing with ID " + getSelectedBorrowingId() + " was not exist in the database"
				);
				borrowingIdInput.clear();
			} else {
				Borrowing newBorrowing = new Borrowing(
						borrowing.getId(),
						borrowing.getReaderId(),
						borrowing.getDocumentId(),
						borrowing.getBorrowTime(),
						borrowing.getDueTime(),
						TimeUtils.getTimestamp()
				);
				if (newBorrowing.getDueTime() < newBorrowing.getReturnTime()) {
					AlertPopup.open("Overdue", "This borrowing is overdue\n" +
							"Expected time: " + TimeUtils.representTimestamp(newBorrowing.getDueTime()) + "\n" +
							"Return time: " + TimeUtils.representTimestamp(newBorrowing.getReturnTime()));
				}
				borrowingStorage.setEntry(newBorrowing);
				filterBorrowingsAction();
			}
		} catch (Exception e) {
			AlertPopup.open("Error", e.getMessage());
		}
	}

}
