package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.bjcommon.BjCommonDirectoryUtil;
import application.bjcommon.BjCommonFileWriterUtil;
import application.entity.HistoryEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * 履歴表示画面のControllerクラス。
 * 履歴ファイル（エクセルファイル）の読み込みを行いViewTableに表示します。
 * 又、検索内容により表示制御も行う。
 * 尚、表示トリガーはEzAvaterConsoleのメニューからと、画面のButtonから呼び出しがされます。
 * @author kreis
 *
 */
public class HistoryController implements Initializable {

	public static boolean tabFlg = true;
	@FXML
	private TextArea logTextArea;
	@FXML
	private TextArea ezlog;
	@FXML
	private ComboBox<String> gyomuCombo;
	@FXML
	private ComboBox<String> userCombo;
	@FXML
	private ComboBox<String> dateCombo;
	@FXML
	private TableView<HistoryEntity> historyTable;
	@FXML
	private TableColumn<HistoryEntity, String> status;
	@FXML
	private TableColumn<HistoryEntity, String> gyomu;
	@FXML
	private TableColumn<HistoryEntity, String> user;
	@FXML
	private TableColumn<HistoryEntity, String> startDate;
	@FXML
	private TableColumn<HistoryEntity, String> endDate;
	@FXML
	private TableColumn<HistoryEntity, String> gyoumuDir;
	@FXML
	private TableColumn<HistoryEntity, String> logfile;
	@FXML
	public TabPane tabs;
	public ObservableList<HistoryEntity> historyList;
	public List<String> userList;
	public List<String> dateList;
	public List<String> gyomuList;
	private static final String ALLUSER = "--ALLUSER--";
	private static final String ALLDATE = "";
	private static String selectionEzlogFile = "";

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		//テキストエリアReadOnly設定。
		logTextArea.setEditable(false);
		ezlog.setEditable(false);

		//historyTable用のObserverList作成。
		historyList = FXCollections.observableArrayList();
		//実行履歴フォルダからhistoryListを作成。
		this.setList();

		//TabaleView とObserverListのbind
		historyTable.itemsProperty().setValue(historyList);
		status.setCellValueFactory(new PropertyValueFactory<HistoryEntity, String>("status"));
		gyomu.setCellValueFactory(new PropertyValueFactory<HistoryEntity, String>("gyomu"));
		user.setCellValueFactory(new PropertyValueFactory<HistoryEntity, String>("user"));
		startDate.setCellValueFactory(new PropertyValueFactory<HistoryEntity, String>("startDate"));
		endDate.setCellValueFactory(new PropertyValueFactory<HistoryEntity, String>("endDate"));
		gyoumuDir.setCellValueFactory(new PropertyValueFactory<HistoryEntity, String>("resultDir"));
		logfile.setCellValueFactory(new PropertyValueFactory<HistoryEntity, String>("logfile"));

		addButtonToTable();
		//Userコンボ設定。
		for (String user : this.userList) {
			userCombo.getItems().add(user);
		}
		// Userコンボ初期選択状態を設定
		userCombo.getSelectionModel().select(0);

		//年月コンボ設定。
		for (String yearAndMonth : this.dateList) {
			dateCombo.getItems().add(yearAndMonth);
		}
		// Dateコンボ初期選択状態を設定
		dateCombo.getSelectionModel().select(0);

		for (String gyomu : this.gyomuList) {
			gyomuCombo.getItems().add(gyomu);
		}
		gyomuCombo.getSelectionModel().select(0);

		//Row選択時のアクション（タブ内のテキストエリアにLogを表示させるアクション設定）
		historyTable.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
			if (historyTable.getSelectionModel().getSelectedItem() != null) {
				//テキストエリア内クリアー処理
				logTextArea.clear();
				ezlog.clear();
				selectionEzlogFile = "";
				//選択行の実行ログファイル名を取得。
				String fileName = historyTable.getSelectionModel().getSelectedItem().getLogfile();

				if (fileName == null || "".equals(fileName))
					return;
				if (!new File(fileName).exists())
					return;
				//実行ログをテキストエリアに表示。
				LogFileOpen.setLogs(fileName, logTextArea);
				//EZAveterログが格納しているフォルダ名取得。
				String dir = historyTable.getSelectionModel().getSelectedItem().getResultDir();
				//ezlogが生成済みか確認（強制終了の場合は作成されない事がある）
				File[] files = BjCommonDirectoryUtil.getDirListForStartWithFilter(dir, "EZAvaterLog_");
				if (files == null || files.length < 1)
					return;

				if(files.length > 1){
					 //日をまたがった際は２つのlogが作成されているので
					 //マージを行う。
					StringBuffer buf=new StringBuffer("");
					for(File item : files) {
						StringBuffer result = null;
						result=BjCommonFileWriterUtil.fileToStringBufferForMs932(item);
					   if( result != null) {

						   buf.append(result);
					   }
					}
					if(!"".equals(buf.toString())) {
						BjCommonFileWriterUtil.FileWriteForMs932(files[0],buf.toString());
					}
				}
				selectionEzlogFile = files[0].toString();
				LogFileOpen.setLogs(files[0].toString(), ezlog);
			}
		});

		//TAB ダブルクリック
		tabs.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				boolean doubleClicked = event.getButton().equals(MouseButton.PRIMARY)
						&& event.getClickCount() == 2;
				if (doubleClicked) {
					Desktop desktop = Desktop.getDesktop();
					File file = null;
					String logfilename = null;
					if (tabs.getSelectionModel().getSelectedIndex() == 0) {
						//実行ログオープン
						logfilename = historyTable.getSelectionModel().getSelectedItem().getLogfile();
					} else {
						//Ezlog
						logfilename = selectionEzlogFile;
					}
					if (logfilename == null || "".equals(logfilename))
						return;
					file = new File(logfilename);
					try {
						desktop.open(file);
					} catch (IOException e) {
						// TODO 自動生成された catch ブロック
						e.printStackTrace();
					}
				}
			}
		});
		//ステータスを見てエラー時の色を変える
		historyTable.setRowFactory(row -> new TableRow<HistoryEntity>() {
			@Override
			protected void updateItem(HistoryEntity item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null)
					setStyle("");
				else if ("強制終了".equals(item.getStatus()))
					setStyle("-fx-background-color: #ff69b4;");
				else if ("異常終了".equals(item.getStatus()))
					setStyle("-fx-background-color: #ffc0cb;");
				else
					setStyle("");
			}
		});

		//先頭行にフォーカス
				historyTable.requestFocus();
				historyTable.getSelectionModel().select(0);
	}

	/**
	 * tableView (historyTable) のListを作成する。
	 */
	public void setList() {
		List<String> flist = getHistoryFileList();
		historyList = FXCollections.observableArrayList();
		for (String fileName : flist) {
			historyList = HistoryExcel.getHistoryEntityLIst(historyList, fileName);
		}
		this.historyListSort();

		//gyomuListを作成
		Map<String, String> map = new HashMap<String, String>();
		map.put(ALLDATE, ALLDATE);
		for (HistoryEntity entity : historyList) {
			map.put(entity.getGyomu(), entity.getGyomu());
		}
		gyomuList = new ArrayList<String>(map.keySet());
		//userListを作成
		map.clear();
		map.put(ALLUSER, ALLUSER);
		for (HistoryEntity entity : historyList) {
			map.put(entity.getUser(), entity.getUser());
		}
		//検索画面の実行Userコンボ内のリスト作成
		userList = new ArrayList<String>(map.keySet());

		//dateListを作成
		map.clear();
		map.put(ALLDATE, ALLDATE);
		for (HistoryEntity entity : historyList) {
			String str = entity.getStartDate();
			int index = str.indexOf("月");
			String yearAndMonth = str.substring(0, index);
			map.put(yearAndMonth + "月", yearAndMonth);
			System.out.println(yearAndMonth);
		}
		dateList = new ArrayList<String>(map.keySet());

	}

	public long chengeDate(String val) {
		Pattern pattern = Pattern.compile("[^0-9]");
		Matcher matcher = pattern.matcher(val);
		String str = matcher.replaceAll("");
		return Long.parseLong(str);
	}

	private void searchList() {
		//検索条件にてListの再作成。
		//業務で絞る
		String gyomu = gyomuCombo.getValue();
		//USERで絞る
		String user = userCombo.getValue();
		//DATEで絞る
		String str = dateCombo.getValue();
		int index;
		String yearAndMonth = "";
		if (!ALLDATE.equals(str)) {
			index = str.indexOf("月");
			yearAndMonth = str.substring(0, index);
		}
		Iterator<HistoryEntity> ite = historyList.iterator();
		while (ite.hasNext()) {
			HistoryEntity entity = ite.next();
			//業務
			if (!ALLDATE.equals(gyomu)) {
				if (!gyomu.equals(entity.getGyomu())) {
					ite.remove();
					continue;
				}
			}
			//User
			if (!ALLUSER.equals(user)) {
				if (!user.equals(entity.getUser())) {
					ite.remove();
					continue;
				}
			}
			//DATE
			if (!ALLDATE.equals(str)) {
				String startdate = entity.getStartDate();
				index = startdate.indexOf("月");
				if (!yearAndMonth.equals(startdate.substring(0, index))) {
					ite.remove();
				}
			}

		}
	}

	@FXML
	public void refreshOpen(ActionEvent evt) {
		this.refreshOpen();
	}
	public void refreshOpen() {
		this.setList();
		this.searchList();
		this.historyTable.getItems().clear();
		this.historyTable.getItems().addAll(historyList);
		this.historyTable.layout();
		this.historyTable.refresh();
		//先頭行にフォーカス
		historyTable.requestFocus();
		historyTable.getSelectionModel().select(0);
	}

	@FXML
	public void clear(ActionEvent evt) {
		userCombo.getSelectionModel().select(0);
		dateCombo.getSelectionModel().select(0);
		gyomuCombo.getSelectionModel().select(0);
	}

	public static List<String> getHistoryFileList() {
		File fileDir = AppProperties.getHistoryDirName();
		//設定されているか？
		if (fileDir == null) {
			showsceNotInfo("履歴フォルダ設定エラー", "履歴ファイルの場所が設定されていません。履歴メニュー内の設定より履歴ファイルが格納されている場所を設定して下さい。");
			//messageを出し処理終了
			return null;
		}
		//履歴エクセルフォルダの存在チェック？
		if (!fileDir.exists()) {
			showsceNotInfo("履歴フォルダ設定エラー", "履歴ファイルの場所が設定されていません。履歴メニュー内の設定より履歴ファイルが格納されている場所を設定して下さい。");
			//messageを出し処理終了
			return null;
		}

		//履歴フォルダ内に該当履歴ファイルが存在するか？

		File[] files = BjCommonDirectoryUtil.getDirListForEndWithFilter(fileDir.toString(),
				AppProperties.getHistoryFileEndWith());
		if (files == null || files.length < 1) {
			showsceNotInfo("履歴ファイルエラー", "履歴ファイルの場所に履歴ファイルがありません。");
			return null;
		}
		//先頭チェック
		List<String> list = new ArrayList<String>();
		String str = AppProperties.getHistoryFileStartWith();
		for (int i = 0; i < files.length; ++i) {
			if (files[i].getName().startsWith(str)) {

				System.out.println(files[i].getName());
				list.add(files[i].toString());
			}
		}
		if (list.size() < 1) {
			showsceNotInfo("履歴ファイルエラー", "履歴ファイルの場所に履歴ファイルがありません。");
			return null;
		}
		return list;
	}

	/**
	 * 汎用エラーダイアログ
	 */
	public static void showsceNotInfo(String title, String message) {
		Alert alrt = new Alert(AlertType.ERROR);
		alrt.setTitle(title);
		alrt.setHeaderText(null);
		alrt.setContentText(message);
		alrt.showAndWait();
	}

	/**
	 * 降順ソート
	 */
	public void historyListSort() {

		Collections.sort(historyList,(p1, p2) -> {
			int ret = ((HistoryEntity) p2).getStartDate().compareTo(((HistoryEntity) p1).getStartDate());
			if (ret == 0) {
				ret = ((HistoryEntity) p2).getEndDate().compareTo(((HistoryEntity) p1).getEndDate());
			}
			if (ret == 0) {
				ret = ((HistoryEntity) p2).getUser().compareTo(((HistoryEntity) p1).getUser());
			}
		return ret;
		});
	}


	/**
	 * historyTable内の列にボタンを追加（結果フォルダを開く）
	 */
	private void addButtonToTable() {

		TableColumn<HistoryEntity, Void> colBtn = new TableColumn<HistoryEntity, Void>("実行結果フォルダ");

		Callback<TableColumn<HistoryEntity, Void>, TableCell<HistoryEntity, Void>> cellFactory
		= new Callback<TableColumn<HistoryEntity, Void>, TableCell<HistoryEntity, Void>>() {
			@Override
			public TableCell<HistoryEntity, Void> call(final TableColumn<HistoryEntity, Void> param) {
				final TableCell<HistoryEntity, Void> cell = new TableCell<HistoryEntity, Void>() {

					private final Button btn = new Button("結果フォルダを開く");

					{
						btn.setOnAction((ActionEvent event) -> {
							HistoryEntity entity = getTableView().getItems().get(getIndex());
							entity.getResultDir();
							try {
								Runtime.getRuntime().exec("explorer.exe /root ," + entity.getResultDir() );
							} catch (IOException ex) {

							}
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		};
		colBtn.setCellFactory(cellFactory);
		historyTable.getColumns().add(colBtn);

	}
}
