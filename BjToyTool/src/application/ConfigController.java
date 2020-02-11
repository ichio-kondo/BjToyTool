package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;

public class ConfigController implements Initializable {

	@FXML
	private Button closebutton;


	@FXML
	public void close(ActionEvent evt) {
		closebutton.getScene().getWindow().hide();
	}

	@FXML
	public void ok(ActionEvent evt) {

	}

	@FXML
	public void setSCE(ActionEvent evt) {
		FileChooser fc = new FileChooser();
	    fc.setTitle("ファイル選択");
	    fc.getExtensionFilters().addAll(
	      new FileChooser.ExtensionFilter("SEC設定場所", "*.proj"),
	      new FileChooser.ExtensionFilter("すべてのファイル", "*.*")
	    );
	    // 初期フォルダをホームに
	    fc.setInitialDirectory(new File(System.getProperty("user.home")));
	    // ファイルダイアログ表示
	    //File file = fc.showSaveDialog(null);
	    File file = fc.showOpenDialog(null);
	    if(file != null) {
	      System.out.println(file.getPath());
	    }
	}




	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO 自動生成されたメソッド・スタブ

	}

}
