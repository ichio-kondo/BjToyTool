
package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import application.entity.ConfigEntity;

public class AppProperties {

	private static Properties prop = new Properties();
	private static final String path = "BJTOYTOOL\\app.properties";
	private static final String NORMALEND_MESSAGE = "実施ステータス】正常終了";
	private static final String CREATE_DIR_MESSAGE = "【実行結果格納フォルダが作成されました】";
	private static final String HISTORY_FILE_STARTWITH = "EZAvater_";
	private static final String HISTORY_FILE_ENDWITH = "_実行履歴.xlsx";
	private static final String EZLOG_FILE_STARTWITH = "EZAvaterLog_";

	static {
		try {
			String strpass = System.getenv("LOCALAPPDATA") + "\\" + path;

			if (!(new File(strpass).exists())) {
				//app.properties作成
				try {
					FileWriter fw = new FileWriter(strpass);
					fw.write("");
					fw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			InputStream istream = new FileInputStream(strpass);
			InputStreamReader isr = new InputStreamReader(istream, "UTF-8");
			prop.load(isr);
			istream.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public static String getEzLogFileStartWith() {
		try {

			String msg = prop.getProperty("ezlogFileStart");
			System.out.println(msg);
			if (msg == null || "".equals(msg)) {
				//SETしてデフォルト文字列を返す。
				AppProperties.setProperties("ezlogFileStart", AppProperties.EZLOG_FILE_STARTWITH);
				return AppProperties.EZLOG_FILE_STARTWITH;
			}
			return msg;
		} catch (Exception e) {
			AppProperties.setProperties("ezlogFileStart", AppProperties.EZLOG_FILE_STARTWITH);
			return AppProperties.EZLOG_FILE_STARTWITH;
		}
	}




	public static String getHistoryFileEndWith() {
		try {

			String msg = prop.getProperty("historyFileEnd");
			System.out.println(msg);
			if (msg == null || "".equals(msg)) {
				//SETしてデフォルト文字列を返す。
				AppProperties.setProperties("historyFileEnd", AppProperties.HISTORY_FILE_ENDWITH);
				return AppProperties.HISTORY_FILE_ENDWITH;
			}
			return msg;
		} catch (Exception e) {
			AppProperties.setProperties("historyFileEnd", AppProperties.HISTORY_FILE_ENDWITH);
			return AppProperties.HISTORY_FILE_ENDWITH;
		}
	}

	public static String getHistoryFileStartWith() {
		try {

			String msg = prop.getProperty("historyFileStart");
			System.out.println(msg);
			if (msg == null || "".equals(msg)) {
				//SETしてデフォルト文字列を返す。
				AppProperties.setProperties("historyFileStart", AppProperties.HISTORY_FILE_STARTWITH);
				return AppProperties.HISTORY_FILE_STARTWITH;
			}
			return msg;
		} catch (Exception e) {
			AppProperties.setProperties("historyFileStart", AppProperties.HISTORY_FILE_STARTWITH);
			return AppProperties.HISTORY_FILE_STARTWITH;
		}
	}

	public static String getNormalEndMessage() {
		try {

			String normarlEndMessage = prop.getProperty("normalend");
			System.out.println(normarlEndMessage);
			if (normarlEndMessage == null || "".equals(normarlEndMessage)) {
				//SETしてデフォルト文字列を返す。
				AppProperties.setProperties("normalend", AppProperties.NORMALEND_MESSAGE);
				return AppProperties.NORMALEND_MESSAGE;
			}
			return normarlEndMessage;
		} catch (Exception e) {
			AppProperties.setProperties("normalend", AppProperties.NORMALEND_MESSAGE);
			return AppProperties.NORMALEND_MESSAGE;
		}
	}

	public static String getCreateExeDirMessage() {
		try {

			String message = prop.getProperty("createExeDir");

			if (prop.getProperty("createExeDir") == null || "".equals(prop.getProperty("createExeDir"))) {
				//SETしてデフォルト文字列を返す。
				AppProperties.setProperties("createExeDir", AppProperties.CREATE_DIR_MESSAGE);
				return AppProperties.CREATE_DIR_MESSAGE;
			}
			return message;
		} catch (Exception e) {
			AppProperties.setProperties("createExeDir", AppProperties.CREATE_DIR_MESSAGE);
			return AppProperties.CREATE_DIR_MESSAGE;
		}
	}

	public static boolean isSceDir() {
		String sce = prop.getProperty("sce");
		if (sce == null || "".equals(sce))
			return false;
		File filename = new File(sce);
		if (filename.exists())
			return true;
		return false;
	}

	public static List<ConfigEntity> getConfList() {
		String fileName = prop.getProperty("sce");
		List<ConfigEntity> list = new ArrayList<ConfigEntity>();

		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(fileName)), "UTF8"));) {
			String str;
			while ((str = reader.readLine()) != null) {
				String[] sce = str.split("=");
				ConfigEntity entity = new ConfigEntity();
				if (sce.length == 2) {
					entity.setGyomu(sce[0]);
					entity.setExeSceDir(sce[1]);
					entity.setConfFileName(entity.getExeSceDir().replace(".sce", "_CONFIG.xlsx"));
					list.add(entity);
				}
			}
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
		return list;
	}

	public static Map<String, ConfigEntity> getSceMap() {
		Map<String, ConfigEntity> map = new HashMap<String, ConfigEntity>();
		List<ConfigEntity> list = getConfList();
		for (ConfigEntity entity : list) {
			map.put(entity.getGyomu(), entity);
		}
		return map;
	}

	public static boolean isAvater() {
		String sce = prop.getProperty("ezavater");
		if (sce == null || "".equals(sce))
			return false;
		File filename = new File(sce);
		if (filename.exists())
			return true;
		return false;
	}

	public static File getAvaterFileName() {
		return new File(prop.getProperty("ezavater"));

	}

	public static File getHistoryDirName() {
		try {
			return new File(prop.getProperty("historyDir"));
		} catch (Exception e) {
			return null;
		}
	}

	public static void setProperties(String key, String filename) {
		AppProperties.prop.setProperty(key, filename);
		String strpass = System.getenv("LOCALAPPDATA") + "\\" + path;
		OutputStream ostream;
		try {
			ostream = new FileOutputStream(strpass);

			OutputStreamWriter osw = new OutputStreamWriter(ostream, "UTF-8");
			prop.store(osw, "Comments");
			osw.close();

			ostream.close();

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
