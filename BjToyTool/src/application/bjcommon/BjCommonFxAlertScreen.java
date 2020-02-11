package application.bjcommon;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * 共通ダイアログボックス用クラス
 * @author kreis
 *
 */
public class BjCommonFxAlertScreen {

	/**
	 * FX用汎用エラーダイアログ
	 */
	public static void errorMsgBox(String title, String message) {
		Alert alrt = new Alert(AlertType.ERROR);
		alrt.setTitle(title);
		alrt.setHeaderText(null);
		alrt.setContentText(message);
		alrt.showAndWait();
	}

	/**
	 * FX用汎用インフォメーションダイヤログ
	 * @param title
	 * @param message
	 */
	public static void infoMsgBox(String title,String message) {
		Alert alrt = new Alert(AlertType.INFORMATION);
		alrt.setTitle(title);
		alrt.setHeaderText(null);
		alrt.setContentText(message);
		alrt.showAndWait();
	}



}
