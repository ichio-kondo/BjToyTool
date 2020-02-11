package application;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.beanutils.BeanUtils;

import application.bjcommon.BjCommonExcelReader;
import application.bjcommon.BjExcelEntity;
import application.entity.GyomuCofingEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author i0903
 *
 */
public class ScenarioController  implements Initializable {

	@FXML
	private TableView<GyomuCofingEntity> SceTable;
	@FXML
	private TextField sceconfig;

	private String confFileName;

	public String getConfFileName() {
		return confFileName;
	}

	public void setConfFileName(String confFileName) {
		this.confFileName = confFileName;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO 自動生成されたメソッド・スタブ

	}

	/**
	 * configファイル読み込み。
	 * @return
	 */
	public List<GyomuCofingEntity> getList(){
		sceconfig.setText(confFileName);
		File bookFile = new File(confFileName);
		int startRow = 2;
		int startCol = 0;
		GyomuCofingEntity entity = new GyomuCofingEntity();
		int sheetIndex = 0;
		BjCommonExcelReader exel = new BjCommonExcelReader(bookFile,startRow,startCol,sheetIndex,((BjExcelEntity)entity) );
		//エクセルからの読み込み
		List<BjExcelEntity> list =  exel.getList();
		//エクセルから、変数記載＜％％＞を変数値にリプレイスし、GyomuConfigEntityListへPopulate。

		List<GyomuCofingEntity> gyoumuConfigList = new ArrayList<GyomuCofingEntity>();
		for( BjExcelEntity eEntity: list) {
			GyomuCofingEntity gEntity = new GyomuCofingEntity();
			try {
				BeanUtils.copyProperties(gEntity, eEntity);
			} catch (IllegalAccessException | InvocationTargetException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return null;
			}
			gyoumuConfigList.add(gEntity);
		}


    	for(GyomuCofingEntity e: gyoumuConfigList) {
    		System.out.println(e.getVariableName() + "=" + e.getVariableValue());
    	}
		return gyoumuConfigList;
	}

	@FXML
	public void add(ActionEvent evt) {

	}
	@FXML
	public void delete(ActionEvent evt) {

	}
	@FXML
	public void submit(ActionEvent evt) {

	}
	@FXML
	public void close(ActionEvent evt) {

	}

}
