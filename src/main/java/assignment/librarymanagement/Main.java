package assignment.librarymanagement;

import assignment.librarymanagement.controllers.AdminBoard;
import assignment.librarymanagement.controllers.UserBoard;
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

	public Main() throws SQLException, IOException {
	}

	public static void main(String[] args) throws SQLException, IOException {
		Application.launch(args);
	}

	private User currentUser = null;
	private BoardInterface currentBoard = new WelcomeBoard(user -> { this.currentUser = user; }, this::reloadUi);
	private Stage primaryStage = null;

	private void reloadUi() {
		try {
			if (currentUser == null) {
				currentBoard = new WelcomeBoard(user -> { this.currentUser = user; }, this::reloadUi);
				currentBoard.start(primaryStage);
				primaryStage.show();
			} else if (currentUser.getRole() == User.Role.ADMINISTRATOR) {
				currentBoard = new AdminBoard(currentUser, user -> { this.currentUser = user; }, this::reloadUi);
				currentBoard.start(primaryStage);
				primaryStage.show();
			} else if (currentUser.getRole() == User.Role.USER) {
				currentBoard = new UserBoard(currentUser, user -> { this.currentUser = user; }, this::reloadUi);
				currentBoard.start(primaryStage);
				primaryStage.show();
			} else Platform.exit();
		} catch (Exception e) {
			e.printStackTrace();
			Platform.exit();
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));
		currentBoard.start(primaryStage);
		primaryStage.show();
	}

}
