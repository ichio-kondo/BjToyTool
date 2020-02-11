package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import application.entity.ConfigEntity;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * mainとなる画面（EzAvaterConsole)のコントローラークラス。
 * 主な実装は、シナリオ実行の際の実行ログのWatchとEzAvaterのコマンド実行をスレッド管理。
 * 又、実行ログから実行結果フォルダの抽出やステータス表示、プログレスバーの表示管理も行う。
 * @author kreis
 *
 */
public class EzAvaterConsoleController implements Initializable{

	public static final String GYOMU_NULL    ="------実行する業務名を選択して下さい------";
	public Map<String,ConfigEntity> confMap;
	//public ConfigEntity selectEntity;
	public List<ConfigEntity> entityList;

	@FXML
	private TextField statusStr;
	@FXML
	private TextField sectext;
	@FXML
	private TextField nowdateDir;
	@FXML
	private Button openButton;
	@FXML
	private Button exedirButton;
	@FXML
	private Button clear;
	@FXML
	private Button exe;
	@FXML
	private Label lGyomu;
	@FXML
	private ComboBox<String> gCombo;
	@FXML
	private TextArea message;
	@FXML
	private ProgressIndicator indicator;

	/**
	 * コンボ内の業務が選択されていない場合のInfomationダイアログ
	 */
	public void showGyomuNotInfo() {
		Alert alrt = new Alert(AlertType.ERROR);
		alrt.setTitle("コンボ選択エラー");
		alrt.setHeaderText(null);
		alrt.setContentText("シナリオ名（業務名）を選択してください。");
		alrt.showAndWait();
	}

	/**
	 * EzAvater.exeInfomationダイアログ
	 */
	public void showsceNotInfo() {
		Alert alrt = new Alert(AlertType.ERROR);
		alrt.setTitle("ezAvater.exe設定エラー");
		alrt.setHeaderText(null);
		alrt.setContentText("ezavaterが設定されていません。	メニューの設定よりezavater.exeの設定をして下さい。");
		alrt.showAndWait();
	}

	/**
	 * 実行ボタン押下Event
	 * @param evt
	 */
	@FXML
	protected void execute(ActionEvent evt) {

		this.clearText(evt);
		//コンボ選択名称を取得。
		String val = gCombo.getValue();
		if(val==null || GYOMU_NULL.equals(val)) {
			//コンボ内の業務を選択してくださいのメッセージを出して終了。
			this.showGyomuNotInfo();
			return;
		}

		if(!AppProperties.isAvater()) {
			this.showsceNotInfo();
			return;
		}

		//実行ボタンをDisable
		exe.setDisable(true);
		this.comboAction(evt);

		//ポップアップメッセージ出力
		Alert alrt = new Alert(AlertType.CONFIRMATION);
		alrt.setTitle("実行確認");
		alrt.setHeaderText(null);
		alrt.setContentText(val + " を実行してもよろしいですか？");
		Optional<ButtonType> result = alrt.showAndWait();

		if (result.get() == ButtonType.OK) { //OKボタンがクリックされたら

			//Ez Avaterを別スレッドで実行開始。
			message.setText("----------------------" + val + " シナリオを実行開始しました。共通初期処理実施中 ----------------------");

			ExecuteAvater the = new ExecuteAvater();
			the.exe = this.exe;
			the.statusStr = this.statusStr;
			the.nowdateDir = this.nowdateDir;
			the.message = this.message;
			the.avaterFile = AppProperties.getAvaterFileName();
			the.confMap=this.confMap;
			the.entity=ConfigExcel.getConfigEntity(this.confMap.get(val));
			the.indicator=this.indicator;
			//the.alrt = new Alert(AlertType.ERROR);

			Thread thread = new Thread(the);
			thread.start();
			//ステータスボックスに実行開始のステータス表示
			statusStr.setText(val + "実行中");
			System.out.println(val);

		} else {
			//ポップアップメッセージで'取消し'が押下されたので、
			//実行ボタンをEnableにして処理終了。
			exe.setDisable(false);
		}
System.out.println(confMap.get( ""));
	}

	@FXML
	protected void exedir(ActionEvent evt) {
		if( nowdateDir.getText() == null || "".equals(nowdateDir.getText())) return ;
		//エクスプローラーを立ち上げる。
		try {
			Runtime.getRuntime().exec("explorer.exe /root ," + nowdateDir.getText());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	@FXML
	protected void confOpen(ActionEvent evt) {

	}

	@FXML
	protected void inputdir(ActionEvent evt) {

	}
	@FXML
	protected void showedit(ActionEvent evt) {
	String val = gCombo.getValue();
	if(val==null || GYOMU_NULL.contentEquals(val)) {
		//コンボ内の業務を選択してくださいのメッセージを出して終了。
		this.showGyomuNotInfo();
		return;
	}
		Runtime runtime = Runtime.getRuntime();
		ConfigEntity entity = ConfigExcel.getConfigEntity(this.confMap.get(val));
		System.out.println(entity.getExeSceDir());
		try {
			runtime.exec("rundll32 url.dll,FileProtocolHandler " + entity.getExeSceDir());
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}


	}
	/*
	 * 業務フォルダボタン押下Event
	 * @param evt
	 */
	@FXML
	protected void openDir(ActionEvent evt) {

		//コンボ選択名称を取得。
		String val = gCombo.getValue();
		if (GYOMU_NULL.contentEquals(val)) {
			//コンボ内の業務を選択してくださいのメッセージを出して終了。
			this.showGyomuNotInfo();
			return;
		}

		//エクスプローラーを立ち上げる。
		try {
			Runtime.getRuntime().exec("explorer.exe /root ," + "C:\\TK\\EZ_業務\\資格取得\\");
		} catch (IOException ex) {

		}
	}

	@FXML
	protected void clearText(ActionEvent evt) {
		message.clear();
		statusStr.clear();
		statusStr.setStyle("-fx-background-color: #D3D3D3");
		//statusStr.setStyle("-fx-background-color: #D3D3D3; -fx-text-fill: black;");
		sectext.clear();
		nowdateDir.clear();
	}


	/*
     * (非 Javadoc)
     * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	//ProgressIndicatorを非表示に。
    	//indicator.getAccessibleText();
    	indicator.setProgress(-1.0f);
    	indicator.setVisible(false);
    	//System.out.println("%LOCALAPPDATA%=" + System.getenv("LOCALAPPDATA"));
    	this.confSet();
        message.setText( "");
        //ステータス表示ボックスを変更不可属性へ
        statusStr.setEditable(false);
        nowdateDir.setEditable(false);
    }

    private void confSet() {

    	if (AppProperties.isSceDir()) {
    		//読込み処理
    		this.entityList = AppProperties.getConfList();
    		this.confMap = AppProperties.getSceMap();
    		gCombo.getItems().add(GYOMU_NULL);
    		for(ConfigEntity entity : this.entityList){
    			gCombo.getItems().add(entity.getGyomu());
            }
    		// 初期選択状態を設定
            gCombo.getSelectionModel().select(0);
    	}
    }

    /**
     * メニュー「File->Close」押下時
     * @param event
     */
    @FXML
    protected void onMenuClose(ActionEvent event){
      Platform.exit(); // アプリ終了
    }

    /**
     * メニュー設定押下時 設定場所
     */
    @FXML
	protected void allConfigOpen(ActionEvent event) {

		FileChooser fc = new FileChooser();
		fc.setTitle("シナリオ設定ファイルSET");
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("SEC設定ファイル", "*.proj"));
				//new FileChooser.ExtensionFilter("すべてのファイル", "*.*"));
		// 初期フォルダをホームに
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		// ファイルダイアログ表示
		//File file = fc.showSaveDialog(null);
		File file = fc.showOpenDialog(null);
		AppProperties.setProperties("sce", file.toString());
		this.confSet();
	}

    @FXML
    protected void historyDirSet_M(ActionEvent event) {
    	DirectoryChooser fc = new DirectoryChooser();
    	fc.setTitle("実行履歴の格納場所設定");
    	// 初期で開くディレクトリパスを指定
        fc.setInitialDirectory(new File(System.getProperty("user.home")));

        // ダイアログを表示 ※引数にメインのstageを指定する
        File importFile = fc.showDialog(null);

        String selectDirPath = null;
        if (importFile == null) return;
        selectDirPath = importFile.getPath().toString();
        AppProperties.setProperties("historyDir", selectDirPath);

    }

    /**
     * メニュー設定押下時 avater場所
     */
    @FXML
	protected void ezavater(ActionEvent event) {

		FileChooser fc = new FileChooser();
		fc.setTitle("EZAVATERのPATH設定");
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("ezavater.exe", "ezavater.exe"));
				//new FileChooser.ExtensionFilter("すべてのファイル", "*.*"));
		// 初期フォルダをホームに
		fc.setInitialDirectory(new File(System.getProperty("user.home")));
		// ファイルダイアログ表示
		//File file = fc.showSaveDialog(null);
		File file = fc.showOpenDialog(null);
		if (file==null)return ;
		AppProperties.setProperties("ezavater", file.toString());
	}

    /**
     * シナリオ一覧
     * @param event
     * @throws Throwable
     */
    @FXML
    protected void scenarioOpen(ActionEvent event) throws Throwable {
    	this.scenarioOpen();
    }

    public void scenarioOpen() throws Throwable {//フォルダ内履歴エクセルチェック。

    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ScenarioEditer.fxml"));
    	AnchorPane root = (AnchorPane) loader.load();
    	Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("conf.css").toExternalForm());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("シナリオ一覧");
		stage.initModality(Modality.APPLICATION_MODAL);
		ConfigEntity conEntity = confMap.get(gCombo.getValue());
    	((ScenarioController)loader.getController()).setConfFileName(conEntity.getConfFileName());
    	((ScenarioController)loader.getController()).getList();
		stage.showAndWait();
	}
    /**
     * メニュー設定押下時 実行履歴
     * @throws IOException
     */
    @FXML
	protected void historyOpen(ActionEvent event) throws IOException {
    	this.historyOpen();
    }
    @FXML
    protected void historyOpenB(ActionEvent event) throws IOException {
    	this.historyOpen();
    }
    protected void historyOpen() throws IOException {
    	//フォルダ内履歴エクセルチェック。
    	List<String> historyFileList = HistoryController.getHistoryFileList();
    	if(historyFileList == null ) return;
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("History.fxml"));
    	AnchorPane root = (AnchorPane) loader.load();
    	Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("conf.css").toExternalForm());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("実行履歴");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();

    }


    @FXML
    protected void comboAction(ActionEvent event) {
    	//コンボ選択名称を取得。
    	String val = gCombo.getValue();
    	if(val==null || GYOMU_NULL.contentEquals(val)) {
    		sectext.clear();
			return;
		}

    	sectext.setText((ConfigExcel.getConfigEntity(this.confMap.get(val))).getExeSceDir());
    }

    @FXML
    protected void showSecondWindow(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfView.fxml"));
    	AnchorPane root = (AnchorPane) loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("conf.css").toExternalForm());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("設定情報");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.showAndWait();
	}
}
