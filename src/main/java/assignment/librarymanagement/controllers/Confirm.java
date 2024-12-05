package assignment.librarymanagement.controllers;

import assignment.librarymanagement.controllers.interfaces.PopupInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class Confirm implements PopupInterface {

	private final Stage stage;
	private final Runnable callback;

	@FXML
	public ImageView imageView;

	@FXML
	public Text text;

	public Confirm(@NotNull String message, @NotNull Runnable callback) {
		this.callback = callback;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/confirm.fxml"));
		loader.setController(this);
		Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/confirm.png")));
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Confirm");
		stage.setResizable(false);
		stage.getIcons().add(image);
		try {
			stage.setScene(new javafx.scene.Scene(loader.load()));
			imageView.setImage(image);
			text.setText(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void confirm() {
		stage.close();
		callback.run();
	}

	@Override
	public void show() {
		stage.showAndWait();
	}

}
