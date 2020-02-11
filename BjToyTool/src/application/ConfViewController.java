package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class ConfViewController implements Initializable {

	@FXML
	private Button close;


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO 自動生成されたメソッド・スタブ

	}
	
	
	
	
	
	
	
	
	
	
	

	@FXML
	public void confViewClose(ActionEvent evt) {

		  close.getScene().getWindow().hide();

	}


}
