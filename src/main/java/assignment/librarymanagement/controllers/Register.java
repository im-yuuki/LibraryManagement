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

public class Register implements ComponentInterface {

	private final WelcomeScreen functions;
	private final Runnable backToLoginAction;
	private Parent root = new Region();

	@FXML
	public TextField usernameField;

	@FXML
	public TextField nameField;

	@FXML
	public TextField emailField;

	@FXML
	public PasswordField passwordField;

	@FXML
	public Button submitButton;

	public Register(WelcomeScreen functions, Runnable backToLoginAction) {
		this.functions = functions;
		this.backToLoginAction = backToLoginAction;
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/register.fxml"));
			loader.setController(this);
			root = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
			new Alert("UI Load Error", e.toString()).show();
		}
	}

	public void submit() {
		submitButton.setDisable(true);
		functions.register(usernameField.getText(), nameField.getText(), emailField.getText(), passwordField.getText(), () -> {
			submitButton.setDisable(false);
			new Alert("Success", "Account created successfully").show();
			backToLogin();
		}, (message) -> {
			submitButton.setDisable(false);
			new Alert("Error", message).show();
		});
	}

	@FXML
	public void backToLogin() {
		usernameField.clear();
		nameField.clear();
		emailField.clear();
		passwordField.clear();
		backToLoginAction.run();
	}

	@Override
	public Parent getRoot() {
		return root;
	}

}
