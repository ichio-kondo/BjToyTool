package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import application.entity.HistoryEntity;
import javafx.collections.ObservableList;

public class HistoryExcel {

	public static ObservableList<HistoryEntity> getHistoryEntityLIst(ObservableList<HistoryEntity> list,String fileName) {
		if (!(new File(fileName)).exists())
			return null;

		XSSFWorkbook workbook1=null;
		XSSFSheet sheet1;
		try {
			workbook1 = new XSSFWorkbook(new FileInputStream(fileName));
			sheet1= workbook1.getSheetAt(0);
			/* シートの定義の最終行 */
			int lastRow = sheet1.getLastRowNum();

			/* 行を順に上から見ていく。 */
			for (int i = 1; i <= lastRow; i++) {
				HistoryEntity entity = new HistoryEntity();
				/* 行情報を取得 */
				XSSFRow row = sheet1.getRow(i);
				//ステータス
				try {
					entity.setStatus(((XSSFCell) row.getCell(0)).getStringCellValue());
					//業務
					entity.setGyomu(((XSSFCell) row.getCell(1)).getStringCellValue());
					//USER
					entity.setUser(((XSSFCell) row.getCell(2)).getStringCellValue());
					//開始日時
					entity.setStartDate(((XSSFCell) row.getCell(3)).getStringCellValue());
					//終了日時
					entity.setEndDate(((XSSFCell) row.getCell(4)).getStringCellValue());
					//SCE
					entity.setSce(((XSSFCell) row.getCell(5)).getStringCellValue());
					//結果フォルダ
					entity.setResultDir(((XSSFCell) row.getCell(6)).getStringCellValue());
					//実行LOG
					entity.setLogfile(((XSSFCell) row.getCell(7)).getStringCellValue());

				} catch (Exception e) {

					//SCE
					entity.setSce(((XSSFCell) row.getCell(5)).getStringCellValue());
					//結果フォルダ
					entity.setResultDir(((XSSFCell) row.getCell(6)).getStringCellValue());
					//実行LOG
					entity.setLogfile(((XSSFCell) row.getCell(7)).getStringCellValue());
				}
				list.add(entity);
			}
			/* List<String> gyoumuList = list.stream()
			            .map(s -> s.getGyomu())
			            //.distinct()
			            .collect(Collectors.toList());

			    System.out.println(gyoumuList);*/

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

		return list;

	}
}
