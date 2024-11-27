package assignment.librarymanager.functions;

import assignment.librarymanager.data.Document;
import assignment.librarymanager.managers.DocumentStorage;
import assignment.librarymanager.utils.AlertPopup;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Objects;

public class EditDocumentForm implements FormInterface {

	private final DocumentStorage documentStorage;
	private final Stage popup;

	@Nullable
	private final Document document;

	@FXML
	public TextField nameField;

	@FXML
	public TextField qtyField;

	@FXML
	public TextField authorField;

	@FXML
	public TextField publisherField;

	@FXML
	public TextField categoryField;

	public EditDocumentForm(DocumentStorage documentStorage, @Nullable Document document) throws IOException {
		this.document = document;
		this.documentStorage = documentStorage;

		FXMLLoader root = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/edit_document.fxml")));
		root.setController(this);
		popup = new Stage();
		popup.setScene(new Scene(root.load()));
		popup.initModality(Modality.APPLICATION_MODAL);
		popup.initStyle(StageStyle.UTILITY);
		popup.setResizable(false);
		if (document == null) {
			popup.setTitle("Create document");
		} else {
			popup.setTitle("Edit document " + document.getId());
			nameField.setText(document.getName());
			qtyField.setText(String.valueOf(document.getQuantity()));
			authorField.setText(document.getAuthor());
			publisherField.setText(document.getPublisher());
			categoryField.setText(document.getCategory());
		}
		qtyField.setTextFormatter(new TextFormatter<String>(change -> {
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
		int quantity;
		try {
			quantity = Integer.parseInt(qtyField.getText());
		} catch (NumberFormatException e) {
			AlertPopup.open("Error", "Quantity must be a number");
			return;
		}
		String author = authorField.getText();
		String publisher = publisherField.getText();
		String category = categoryField.getText();
		Document newDocument;
		if (document == null) newDocument = new Document(name, quantity, author, publisher, category);
		else newDocument = new Document(document.getId(), name, quantity, author, publisher, category);
		try {
			documentStorage.setEntry(newDocument);
		} catch (Exception e) {
			e.printStackTrace();
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
