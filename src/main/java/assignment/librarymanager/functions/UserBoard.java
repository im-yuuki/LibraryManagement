package assignment.librarymanager.functions;

import assignment.librarymanager.data.Reader;
import assignment.librarymanager.utils.AlertPopup;
import assignment.librarymanager.data.Borrowing;
import assignment.librarymanager.data.Document;
import assignment.librarymanager.managers.BorrowingStorage;
import assignment.librarymanager.managers.Database;
import assignment.librarymanager.managers.DocumentStorage;
import assignment.librarymanager.utils.QRGenerator;
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

public class UserBoard {

	private final DocumentStorage documentStorage;
	private final BorrowingStorage borrowingStorage;
	private final Reader reader;

	private int getSelectedDocumentId() {
		try {
			return Integer.parseInt(documentIdInput.getText());
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
	public Text documentAuthorDisplay;

	@FXML
	public Text documentPublisherDisplay;

	@FXML
	public Text documentCategoryDisplay;

	@FXML
	public TextField borrowingDocumentFilter;

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
	public TableColumn<Borrowing, String> borrowingBorrowTimeColumn;

	@FXML
	public TableColumn<Borrowing, String> borrowingDueTimeColumn;

	@FXML
	public TableColumn<Borrowing, String> borrowingReturnTimeColumn;

	public UserBoard(Stage stage, Reader reader) throws IOException, SQLException {
		this.reader = reader;
		Database database = new Database();
		documentStorage = new DocumentStorage(database);
		borrowingStorage = new BorrowingStorage(database);

		FXMLLoader root = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/user.fxml")));
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

		borrowingIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		borrowingDocumentColumn.setCellValueFactory(new PropertyValueFactory<>("documentId"));
		borrowingBorrowTimeColumn.setCellValueFactory(new PropertyValueFactory<>("borrowTimeFormatted"));
		borrowingDueTimeColumn.setCellValueFactory(new PropertyValueFactory<>("dueTimeFormatted"));
		borrowingReturnTimeColumn.setCellValueFactory(new PropertyValueFactory<>("returnTimeFormatted"));

		filterDocumentsAction();
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

	public void filterBorrowingsAction() {
		int documentId = -1;
		boolean overdueOnly = borrowingOverdueOnlyFilter.isSelected();
		boolean history = borrowingHistoryFilter.isSelected();

		try {
			if (!borrowingDocumentFilter.getText().isBlank()) {
				documentId = Integer.parseInt(borrowingDocumentFilter.getText());
			}
		} catch (NumberFormatException e) {
			AlertPopup.open("Invalid input", "Please enter a valid number for document ID and reader ID");
			return;
		}

		try {
			ArrayList<Borrowing> borrowings = borrowingStorage.findBorrowings(documentId, reader.getId(), overdueOnly, history);
			ObservableList<Borrowing> data = FXCollections.observableArrayList(borrowings);
			borrowingsTable.setItems(data);
		} catch (SQLException e) {
			AlertPopup.open("Error", e.getMessage());
		}
	}

	public void borrowDocumentAction() {
		try {
			new NewBorrowingForm(borrowingStorage, getSelectedDocumentId(), reader.getId(), true);
			filterBorrowingsAction();
		} catch (IOException e) {
			AlertPopup.open("An error occurred while borrowing a document", e.getMessage());
		}
	}

}
