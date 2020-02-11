package application.bjcommon;

/**
 * 正規表現を利用した便利クラス
 * @author kreis
 *
 */
public class BjRegexUtil {

	/**
	 * 文字列から数値のみ抽出しLong値として求めるメソッド
	 * @param str 抽出文字列
	 * @return　数値のみ抽出したLong値
	 */
	public static long getStrForNumber(String str) {
		// 正規表現のパターンを作成。
		if (str == null)
			return 0;
		long n = Long.parseLong(str.replaceAll("[^0-9]", ""));
		System.out.println("dateNumber" + n);
		return n;
	}

}
