package assignment.librarymanagement;

import assignment.librarymanagement.controllers.WelcomeBoard;
import assignment.librarymanagement.controllers.interfaces.BoardInterface;
import assignment.librarymanagement.data.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {

	public static void main(String[] args) throws SQLException, IOException {
		Application.launch(args);
	}

	private User currentUser = null;
	private BoardInterface currentBoard = null;

	private void reloadUi() throws Exception {
		if (currentUser == null) {
			currentBoard = new WelcomeBoard(user -> {
				this.currentUser = user;
			});
		} else if (currentUser.getRole() == User.Role.ADMINISTRATOR) {

		} else if (currentUser.getRole() == User.Role.USER) {

		} else currentBoard = null;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
		reloadUi();
		if (currentBoard == null) Platform.exit();
		currentBoard.start(primaryStage);
		primaryStage.show();
	}

}
