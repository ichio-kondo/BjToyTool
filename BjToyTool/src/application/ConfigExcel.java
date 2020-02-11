package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.entity.ConfigEntity;

public class ConfigExcel {


	/**
	 * ConfigExcelファイルを読み込みます。
	 */

	public static ConfigEntity getConfigEntity(ConfigEntity entity) {

		String configFile = entity.getConfFileName();

		if (!(new File(configFile)).exists())
			return entity;

		XSSFWorkbook workbook1=null;
		try {
			workbook1 = new XSSFWorkbook(new FileInputStream(configFile));

			XSSFSheet sheet1 = workbook1.getSheet("CONFIG");

			Map<String,String> map = ConfigExcel.getCell(sheet1);
			entity.setWatchDir(map.get("$COMMON_DIR$") + map.get("$業務$") + "\\実行結果\\");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				workbook1.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		return entity;
	}


	private static Map<String,String> getCell(XSSFSheet sheet) {

		Map<String,String> map = new HashMap<String,String>();
		for (int i=2; i<19; i++) {


			XSSFRow row1 = sheet.getRow(i);
			XSSFCell hensu = row1.getCell(2);
			XSSFCell val = row1.getCell(3);
			//repulace str
			map.put(hensu.getStringCellValue(),val.getStringCellValue());

		}


		//4,2 4,3 4,4 $NOW_DATE$

		//5,2 5,3,5,4 $業務フォルダCommon$

		//6,2 6,3 6,4 $業務フォルダ$

		//7,2 7,3 7,4 $NOW_DATE_DIR$

		//9,2 9,3 9,4 $OUTPUT_DIR$


		//12,2 12,3 12,4 $実行場所$

		return map;
	}
}
