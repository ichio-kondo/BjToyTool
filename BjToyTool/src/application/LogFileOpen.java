package application;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * LogファイルをtextAreaへ表示するクラス。
 * @author kreis
 */
public class LogFileOpen {

	//private String nowdateDirstr = "【実行結果格納フォルダが作成されました】";
	@FXML
	public TextArea message;
	@FXML
	public TextField nowdateDir;
	public int lineNo=0;
	public String logfileName;
	public String watchPath;
	private Pattern p1;
	private Pattern p2;
	private static final String br = System.getProperty("line.separator");

	public LogFileOpen(TextArea message,TextField nowdateDir,String watchPath) {


		this.message = message;
		this.nowdateDir = nowdateDir;
		this.watchPath = watchPath;
		String regex = ".log$";
		p1 = Pattern.compile(regex);

		//自分の実行ログかの確認（先頭がログインUserIdかのチェック）
		String userName = System.getenv("USERNAME");
		String regex2 = "^" + userName + "_";
		p2 = Pattern.compile(regex2);


	}

	public void loadLogFile(String kind, String objStr) {

		if( this.isLog(kind,objStr)) {
			this.logWrite();
		}

	}

	private void logWrite() {

		Path path = Paths.get(logfileName);
	    File file = path.toFile();

		try (BufferedReader reader
				= new BufferedReader(new InputStreamReader
						(new FileInputStream(file), "MS932"));){
		    String str;
		    StringBuffer buf = new StringBuffer("");

		    //List<String> lines = new ArrayList<>();
		    while ((str = reader.readLine()) != null) {
		        //lines.add(str);
		    	this.nowdateDirCheck(str);
		        buf.append(str);
		        buf.append(br);
		    }
		    message.setText(buf.toString());
		    message.setScrollTop(Double.MAX_VALUE);
		    reader.close();
		} catch (IOException e) {
			e.printStackTrace();
			Alert alrt = new Alert(AlertType.ERROR);
			alrt.setTitle("予期せぬエラーが発生");
			alrt.setHeaderText(null);
			alrt.setContentText(e.getMessage());
			alrt.showAndWait();
		}
	}


	private boolean isLog(String kind, String objStr) {

		if(this.logfileName == null || "".equals(this.logfileName)){
			//新規なので、logファイルが作成されたアクションかのチェック
			if("ENTRY_CREATE".equals(kind) && isLogFileCheck(objStr)) {
				this.logfileName = watchPath + objStr;
				System.out.println("logファイル新規true" + this.logfileName);
				return true;
			}
			return false;
		}

		if("ENTRY_MODIFY".equals(kind) && logfileName.equals( this.watchPath + objStr)) {
			System.out.println("logファイル新規true2" + this.logfileName);
			return true;
		}
		return false;
	}

	/**
	 *実行結果格納場所に格納フォルダのフルパスを表示するメソッド
	 *表示させるトリガーは、avaterにてファイル作成後のログメッセージをKEY
	 *に作成。nowdateDirstr の文字列がログに出力されたタイミング
	 * @param val
	 */
	private void nowdateDirCheck(String val) {

		if(val==null || "".equals(val))return;

		if(val.contains(AppProperties.getCreateExeDirMessage())) {
			val = val.replace(AppProperties.getCreateExeDirMessage(), "");
			this.nowdateDir.setText(val);
		}
	}

	/**
	 * 対象ファイル名チェック
	 * 　
	 * １、先頭が環境変数のUserID
	 * ２、末尾が .log
	 *
	 * @param objStr
	 * @return
	 */
	private boolean isLogFileCheck(String objStr) {

		//実行ログかの対象チェック。
		if(objStr==null || objStr.length() < 5) return false;
		//拡張子が.logのチェック
		Matcher m1 = p1.matcher(objStr);
		//先頭UserIdのチェック
		Matcher m2 = p2.matcher(objStr);

		if(m1.find() && m2.find()) return true;

		return false;
	}

	public static boolean isEnd(String logfileName) {

		Path path = Paths.get(logfileName);
		File file = path.toFile();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "MS932"));) {
			String str;
			//List<String> lines = new ArrayList<>();
			while ((str = reader.readLine()) != null) {
				//lines.add(str);
				if(str.contains(AppProperties.getNormalEndMessage())) return true;
			}

		} catch (IOException e) {
			e.printStackTrace();
			Alert alrt = new Alert(AlertType.ERROR);
			alrt.setTitle("予期せぬエラーが発生");
			alrt.setHeaderText(null);
			alrt.setContentText(e.getMessage());
			alrt.showAndWait();
		}
		return false;
	}

	public static void setLogs(String fileName,TextArea textArea) {

		System.out.println("openlog = " + fileName);

		try (BufferedReader reader =
				new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), "MS932"));) {
			String str;
			StringBuffer buf = new StringBuffer("");

			//List<String> lines = new ArrayList<>();
			while ((str = reader.readLine()) != null) {
				//lines.add(str);
				buf.append(str);
				buf.append(br);
			}
			textArea.setText(buf.toString());
			reader.close();

		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
