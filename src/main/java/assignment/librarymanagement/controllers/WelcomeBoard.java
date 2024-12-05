package assignment.librarymanagement.controllers;

import assignment.librarymanagement.controllers.interfaces.BoardInterface;
import assignment.librarymanagement.data.User;
import assignment.librarymanagement.functions.WelcomeScreen;
import assignment.librarymanagement.utils.DatabaseWrapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.function.Consumer;

public class WelcomeBoard implements BoardInterface {

	private final Parent root;
	private final WelcomeScreen functions;
	private final Consumer<User> loggedInUserSetter;
	private final Runnable switcher;
	private final Login loginUi;
	private final Register registerUi;
	private final DatabaseWrapper database;
	private Stage stage = null;

	@FXML
	public AnchorPane componentDisplay;

	public WelcomeBoard(Consumer<User> loggedInUserSetter, Runnable switcher) throws SQLException, IOException {
		this.switcher = switcher;
		this.loggedInUserSetter = loggedInUserSetter;
		this.database = new DatabaseWrapper();
		this.functions = new WelcomeScreen(database);
		this.loginUi = new Login(functions, this::nextUi, this::toRegister);
		this.registerUi = new Register(functions, this::toLogin);
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("/fxml/welcome.fxml"));
		loader.setController(this);
		root = loader.load();
		toLogin();
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		stage.setScene(new Scene(root));
		stage.setTitle("Welcome - Library Management");
		stage.setResizable(false);
	}

	@Override
	public void nextUi() {
		User user = functions.getLoggedInUser();
		if (user != null) loggedInUserSetter.accept(user);
		if (stage != null) stage.close();
		try {
			database.close();
		} catch (SQLException ignored) {}
		switcher.run();

	}

	private void toLogin() {
		Parent loginRoot = loginUi.getRoot();
		componentDisplay.setTopAnchor(loginRoot, 0.0);
		componentDisplay.setBottomAnchor(loginRoot, 0.0);
		componentDisplay.setLeftAnchor(loginRoot, 0.0);
		componentDisplay.setRightAnchor(loginRoot, 0.0);
		componentDisplay.getChildren().setAll(loginRoot);
	}

	private void toRegister() {
		Parent registerRoot = registerUi.getRoot();
		componentDisplay.setTopAnchor(registerRoot, 0.0);
		componentDisplay.setBottomAnchor(registerRoot, 0.0);
		componentDisplay.setLeftAnchor(registerRoot, 0.0);
		componentDisplay.setRightAnchor(registerRoot, 0.0);
		componentDisplay.getChildren().setAll(registerRoot);
	}

}
