package assignment.librarymanager.functions;

import assignment.librarymanager.data.Reader;
import assignment.librarymanager.utils.AlertPopup;
import javafx.stage.Stage;

public class UserBoard {

	public UserBoard(Stage stage, Reader reader) {
		stage.setScene(null);
		AlertPopup.open("Welcome!", "You are logged in as reader " + reader.getId() + "\n" +
				"But this feature is not implemented yet.\n" +
				"Please wait for the next update.");
		// TODO
	}

}
