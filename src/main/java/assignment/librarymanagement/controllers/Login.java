package assignment.librarymanagement.controllers;

import assignment.librarymanagement.controllers.interfaces.ComponentInterface;
import assignment.librarymanagement.functions.WelcomeScreen;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;

import java.io.IOException;

public class Login implements ComponentInterface {

	private final WelcomeScreen functions;
	private final Runnable loginSuccessAction;
	private final Runnable registerAction;
	private Parent root = new Region();

	@FXML
	public TextField usernameField;

	@FXML
	public PasswordField passwordField;

	@FXML
	public Button submitButton;

	public Login(WelcomeScreen functions, Runnable loginSuccessAction, Runnable registerAction) {
		this.functions = functions;
		this.loginSuccessAction = loginSuccessAction;
		this.registerAction = registerAction;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
			loader.setController(this);
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			new Alert("UI Load Error", e.toString()).show();
		}
	}

	@FXML
	public void submit() {
		submitButton.setDisable(true);
		functions.login(usernameField.getText(), passwordField.getText(), loginSuccessAction, (message) -> {
			submitButton.setDisable(false);
			new Alert("Error", message).show();
		});
	}

	@FXML
	public void registerAction() {
		usernameField.clear();
		passwordField.clear();
		registerAction.run();
	}

	@Override
	public Parent getRoot() {
		return root;
	}
}
