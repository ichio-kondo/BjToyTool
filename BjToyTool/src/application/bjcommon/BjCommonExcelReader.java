package application.bjcommon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * ExcelSheetを読み込むUtilクラス。
 * 読み込みを行うSheet毎にインスタンス生成を行う。
 *
 * コンストラクタ
 * 　１、読み込みを行うエクセルブックのFileクラス
 * 　２、読み込み開始行番号
 * 　３、読み込み開始列番号
 * 　４、読み込みを行うシートのインデックス番号（0開始）
 * 　５、詰込みを行うEntityクラス（BjExcelEntityInterfaceを継承したEntityクラス）
 *
 * 尚、継承先でAbstractメソッドのgetHeadFiledArrayId()の実装を行う。
 * 実装内容は、Entityのフィールド名のString配列を作成。
 * Excelの読み込み開始列番号からの列順に合わせてBindさせたいフィールド名の記載を行う。
 * 作成した配列分読み込みを行い、entityへPopulateします。
 *
 * @author i0903
 *
 */

public class BjCommonExcelReader {

	private File bookFile;
	private int startRow;
	private int startCol;
	private BjExcelEntity entity;
	private int sheetIndex;

	private List<BjExcelEntity> list;

	public BjCommonExcelReader(File bookFile, int startRow, int startCol, int sheetIndex, BjExcelEntity entity) {

		this.bookFile = bookFile;
		this.startRow = startRow;
		this.startCol = startCol;
		this.entity = entity;
		this.sheetIndex = sheetIndex;
	}

	public List<BjExcelEntity> getList() {

		this.read();
		return list;
	}

	public String[] getHeadFiledArrayId() {
		return entity.getColIdArray();
	}

	private void read() {
		list = new ArrayList<BjExcelEntity>();

		XSSFWorkbook workbook1 = null;
		XSSFSheet sheet1;
		try {
			workbook1 = new XSSFWorkbook(new FileInputStream(this.bookFile));
			sheet1 = workbook1.getSheetAt(this.sheetIndex);
			/* シートの定義の最終行 */
			int lastRow = sheet1.getLastRowNum();

			/* 行を順に上から見ていく。 */
			System.out.println("LAST_ROW" + lastRow);
			for (int i = this.startRow; i < lastRow-1; i++) {
				BjExcelEntity eEntity;
				try {
					eEntity = entity.getClass().newInstance();
					/* 行情報を取得 */
					XSSFRow row = sheet1.getRow(i);
					//ステータス
					String[] colId = this.getHeadFiledArrayId();
					for (int ii = startCol; ii < startCol + colId.length; ii++) {
						System.out.println(ii);
						Cell cell = (XSSFCell) row.getCell(ii);
						String val = this.cellToStringValue(cell);
						String id = colId[ii - startCol];
						eEntity = BjReflectUtil.bjFormFiledSet(eEntity, id, val);

					}
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				list.add(eEntity);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook1.close();
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

	}

	/**
	 * Cellの値をすべて文字にして返却します。
	 * @param cell
	 * @return
	 */
	private String cellToStringValue(Cell cell) {

		CellType cellType = cell.getCellType();
		if (cellType == CellType.BLANK) {
			return null;
		} else if (cellType == CellType.BOOLEAN) {
			return Boolean.toString(cell.getBooleanCellValue());
		} else if (cellType == CellType.ERROR) {
			throw new RuntimeException("Error cell is unsupported");
		} else if (cellType == CellType.FORMULA) {
			throw new RuntimeException("Formula cell is unsupported");
		} else if (cellType == CellType.NUMERIC) {
			if (DateUtil.isCellDateFormatted(cell)) {
				return (cell.getDateCellValue()).toString();
			} else {
				return Double.toString(cell.getNumericCellValue());
			}
		} else if (cellType == CellType.STRING) {
			return cell.getStringCellValue();
		} else {
			throw new RuntimeException("Unknow type cell");
		}
	}

}
