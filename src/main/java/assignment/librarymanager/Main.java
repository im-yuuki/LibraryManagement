package assignment.librarymanager;

import assignment.librarymanager.data.Reader;
import assignment.librarymanager.functions.AdminBoard;
import assignment.librarymanager.functions.FormInterface;
import assignment.librarymanager.functions.UserBoard;
import assignment.librarymanager.managers.Database;
import assignment.librarymanager.managers.ReaderStorage;
import assignment.librarymanager.utils.AlertPopup;
import io.github.cdimascio.dotenv.Dotenv;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application implements FormInterface {

	private final Database database;
	private final ReaderStorage readerStorage;
	private final Dotenv dotenv;
	private Stage stage;

	@FXML
	public TextField idField;

	@FXML
	public PasswordField passwordField;

	public Main() throws SQLException, IOException {
		database = new Database();
		readerStorage = new ReaderStorage(database);
		dotenv = Dotenv.configure().ignoreIfMissing().load();
	}

	@Override
	public void start(Stage stage) throws IOException {
		this.stage = stage;
		stage.setTitle("Library Manager");
		stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
		FXMLLoader root = new FXMLLoader(Objects.requireNonNull(getClass().getResource("/fxml/login.fxml")));
		root.setController(this);
		Scene scene = new Scene(root.load());
		stage.setScene(scene);
		stage.setResizable(false);

		idField.setTextFormatter(new TextFormatter<String>(change -> {
			String text = change.getControlNewText();
			if (text.matches("\\d*")) {
				return change;
			}
			return null;
		}));
		stage.show();
	}

	@Override
	public void onSubmit() {
		int id;
		try {
			String idText = idField.getText();
			if (idText.isBlank()) {
				AlertPopup.open("Invalid ID", "Please enter your ID");
				return;
			}
			id = Integer.parseInt(idText);
		} catch (NumberFormatException e) {
			AlertPopup.open("Invalid ID", "ID must be a number");
			return;
		}
		String password = passwordField.getText();
		if (password.isBlank()) {
			AlertPopup.open("Invalid Password", "Please enter your password");
			return;
		}
		try {
			if (id == 0) {
				if (dotenv.get("ADMIN_PASSWORD", "admin").equals(password)) {
					database.close();
					stage.close();
					stage.setResizable(true);
					new AdminBoard(stage);
					stage.show();
				} else {
					throw new LoginException("Invalid admin password");
				}
			} else {
				Reader reader = readerStorage.getEntry(id);
				if (reader == null) {
					throw new LoginException("Invalid ID");
				}
				if (reader.verifyPassword(password)) {
					database.close();
					stage.close();
					stage.setResizable(true);
					new UserBoard(stage, reader);
					stage.show();
				} else {
					throw new LoginException("Invalid password");
				}
			}
		} catch (SQLException | LoginException | NullPointerException e) {
			AlertPopup.open("Error", "Invalid ID or password");
		} catch (Exception e) {
			e.printStackTrace();
			AlertPopup.open("An unexpected error occured", e.getMessage());
			Platform.exit();
		}
	}

	@Override
	public void onCancel() {
		Platform.exit();
	}

	public static void main(String[] args) {
		Application.launch(Main.class, args);
	}

}