package application.bjcommon;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Fileを扱う共通Utilityクラス
 * @author kreis
 *
 */
public class BjCommonDirectoryUtil {


	/**
	 * 指定ディレクトリ内にて任意文字列（後方一致）のファイル一覧を抽出するメソッド。
	 * @param dirPath　String　検索フォルダ
	 * @param fileterString　String 検索文字列（後方一致）
	 * @return　File配列　File[]
	 */

	public static File[] getDirListForEndWithFilter(String dirPath,String fileterString) {

		//フィルタを作成する
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File file, String str) {
				if (str.endsWith(fileterString)) {
					return true;
				} else {
					return false;
				}
			}
		};
		File[] files = new File(dirPath).listFiles(filter);

		//結果を出力する
		for (int i = 0; i < files.length; ++i) {
			System.out.println(files[i]);
		}
		return files;
	}

	/**
	 * 指定ディレクトリ内にて任意文字列（前方一致）のファイル一覧を抽出するメソッド。
	 * @param dirPath　String　検索フォルダ
	 * @param fileterString　String 検索文字列（後方一致）
	 * @return　File配列　File[]
	 */
	public static File[] getDirListForStartWithFilter(String dirPath,String fileterString) {

		//フィルタを作成する
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File file, String str) {
				if (str.startsWith(fileterString)) {
					return true;
				} else {
					return false;
				}
			}
		};
		File[] files = new File(dirPath).listFiles(filter);

		//結果を出力する
		for (int i = 0; i < files.length; ++i) {
			System.out.println(files[i]);
		}
		return files;
	}

}
