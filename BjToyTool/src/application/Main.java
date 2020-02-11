package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			System.out.println(getClass().toString());
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("EzAvaterConsole.fxml"));
			Scene scene = new Scene(root);
			//アイコンの設定
			Image img = new Image(getClass().getResourceAsStream( "bj.ico" ));
			primaryStage.getIcons().add(img);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle( "BJ TOYTOOL - Ez Avater Controller");

			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		launch(args);

	}
}
