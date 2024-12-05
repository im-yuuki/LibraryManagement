package assignment.librarymanagement.controllers;

import assignment.librarymanagement.controllers.interfaces.PopupInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

public class Alert implements PopupInterface {

	private final Stage stage;

	@FXML
	public ImageView imageView;

	@FXML
	public Text text;

	public Alert(@NotNull String title, @NotNull String message) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/alert.fxml"));
		loader.setController(this);
		Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/alert.png")));
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(title);
		stage.setResizable(false);
		stage.getIcons().add(image);
		try {
			stage.setScene(new Scene(loader.load()));
			imageView.setImage(image);
			text.setText(message);
		} catch (IOException e) {
			System.err.println("FALLBACK: " + title + ": " + message);
			e.printStackTrace();
		}
	}

	@Override
	public void show() {
		stage.showAndWait();
	}

	@FXML
	public void close() {
		stage.close();
	}

}
