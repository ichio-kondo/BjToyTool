package application;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import application.entity.ConfigEntity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * EzAvater.exeを実行します。
 * @author kreis
 *
 */
public class ExecuteAvater implements Runnable {

	@FXML
	public Button exe;
	@FXML
	public TextField statusStr;
	@FXML
	public TextField nowdateDir;
	@FXML
	public TextArea message;
	@FXML
	public ProgressIndicator indicator;

	public File avaterFile;
	public Map<String,ConfigEntity> confMap;
	public ConfigEntity entity;


	public void run() {

		//実行ログ監視を別スレッドで開始。
		LogWatch watch = new LogWatch();
		watch.message = this.message;
		watch.nowdateDir = this.nowdateDir;
		watch.watchPath = this.entity.getWatchDir();
		Thread threadWatch = new Thread(watch);
		threadWatch.start();
		boolean errorFlg = true;
		String command = this.avaterFile.toString() + " /w  " + entity.getExeSceDir(); // 起動コマンドを指定する
        Runtime runtime = Runtime.getRuntime(); // ランタイムオブジェクトを取得する
            try {

            	Process proc = runtime.exec(command); // 指定したコマンドを実行する
            	indicator.setVisible(true);
            	proc.waitFor();

            	//logFileチェックをして正常終了か異常終了の確認をする。
            	if(LogFileOpen.isEnd(watch.logWrite.logfileName)) {
            		statusStr.setText(entity.getGyomu() + " 処理正常終了");
            		errorFlg=false;
            	}else {

            		statusStr.setText(entity.getGyomu() + " 処理異常終了");

            	}

            } catch (IOException e) {

            	statusStr.setText(entity.getGyomu() + " SCE実行エラー");


            } catch (InterruptedException e) {

            	statusStr.setText(entity.getGyomu() + " SCE実行エラー");


            } catch (Exception e) {

				statusStr.setText(entity.getGyomu() + " 実行中にEzAvagerが異常終了しました。 ");

			}finally {
				watch.close();
				System.out.println("EZAvater処理終了。");

				if(errorFlg) {
					statusStr.setStyle("-fx-background-color: #ff69b4;");
				}


				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO 自動生成された catch ブロック
					e.printStackTrace();
				}
				indicator.setVisible(false);


				//実行ボタン活性化
				exe.setDisable(false);
				message.setScrollTop(Double.MAX_VALUE);


			}
	}

}
