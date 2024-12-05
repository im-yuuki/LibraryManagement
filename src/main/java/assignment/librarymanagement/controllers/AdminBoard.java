package assignment.librarymanagement.controllers;

import assignment.librarymanagement.controllers.interfaces.BoardInterface;
import assignment.librarymanagement.data.User;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class AdminBoard implements BoardInterface {

	private final User currentUser;
	private final Consumer<User> loggedInUserSetter;
	private final Runnable switcher;
	private final Parent root;
	private Stage stage;

	@FXML
	public AnchorPane displayPane;

	@FXML
	public Text nameDisplay;

	@FXML
	public ImageView background;

	public AdminBoard(User currentUser, Consumer<User> loggedInUserSetter, Runnable switcher) throws IOException {
		if (currentUser.getRole() != User.Role.ADMINISTRATOR) Platform.exit();
		this.currentUser = currentUser;
		this.loggedInUserSetter = loggedInUserSetter;
		this.switcher = switcher;
		FXMLLoader loader =  new FXMLLoader(getClass().getResource("/fxml/admin.fxml"));
		loader.setController(this);
		root = loader.load();
		nameDisplay.setText(currentUser.getName());
	}

	@Override
	public void start(Stage stage) {
		this.stage = stage;
		Scene scene = new Scene(root);
		stage.setScene(scene);
		background.fitWidthProperty().bind(scene.widthProperty());
		background.fitHeightProperty().bind(scene.heightProperty());
		stage.setTitle(currentUser.getName() + " [Administrator] - Library Management");
		stage.setResizable(true);
	}

	@Override
	public void nextUi() {
		loggedInUserSetter.accept(null);
		switcher.run();
	}

	@FXML
	public void logout() {
		new Confirm("Are you sure you want to logout?", this::nextUi).show();
	}

	@FXML
	public void showAbout() {
		new Alert("About", "Library Management System\nVersion 2.0").show();
	}

	@FXML
	public void exit() {
		Platform.exit();
	}

}
