package assignment.librarymanager.utils;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class AlertPopup {

	public static void open(String label, String message) {
		Stage alertPopup = new Stage();
		alertPopup.initModality(Modality.APPLICATION_MODAL);
		alertPopup.initStyle(StageStyle.UTILITY);
		alertPopup.setTitle(label);

		Image infoIcon = new Image(Objects.requireNonNull(AlertPopup.class.getResourceAsStream("/info.png")), 40, 40, true, true);
		Text text = new Text(message);
		text.setWrappingWidth(360);
		HBox hBox = new HBox(10, new ImageView(infoIcon), text);
		hBox.setAlignment(Pos.CENTER);
		Button closeButton = new Button("OK");
		closeButton.setOnAction(e -> alertPopup.close());
		VBox layout = new VBox(10, hBox, closeButton);
		layout.setPadding(new Insets(20));
		layout.setAlignment(Pos.CENTER);

		alertPopup.setScene(new Scene(layout));
		alertPopup.setResizable(false);
		alertPopup.showAndWait();
	}

}
