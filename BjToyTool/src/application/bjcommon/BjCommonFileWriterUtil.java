package application.bjcommon;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class BjCommonFileWriterUtil {

	private static final String br = System.getProperty("line.separator");

	/**
	 *文字コードMS923でパラメータファイルを読み込んでStringBufferで返すメソッド
	 *
	 * @param file
	 * @param String 文字コード characterCode
	 */
	public static StringBuffer fileToStringBufferForMs932(File file) {
		return BjCommonFileWriterUtil.fileToStringBuffer(file, "MS932");
	}
	/**
	 * パラメータファイルを読み込んでStringBufferで返すメソッド
	 * @param file
	 * @param String 文字コード characterCode
	 */
	public static StringBuffer fileToStringBuffer(File file,String characterCode) {
		StringBuffer buf = null;
		//BufferedReader reader = null;
		try (BufferedReader reader
				= new BufferedReader(new InputStreamReader
						(new FileInputStream(file), characterCode));){
		    String str;
		    buf = new StringBuffer("");
		    while ((str = reader.readLine()) != null) {
		        buf.append(str);
		        buf.append(br);
		    }
		    if(reader != null) {
				reader.close();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();

		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();

		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();

		}
		return buf;
	}

	/**
	 * パラメータファイルに書き込みを行う。
	 * @param file
	 * @param str
	 * @return boolean true 書き込み成功 false 書き込みNG
	 */
	public static boolean FileWriteForMs932(File file, String str) {
		if(!BjCommonFileWriterUtil.checkBeforeWritefile(file)) return false;
		try {

            PrintWriter p_writer = new PrintWriter(new BufferedWriter
                (new OutputStreamWriter(new FileOutputStream(file),"MS932")));
            p_writer.println(str);
            p_writer.close();
            return true;
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
	}

	/**
	 * 書き込み可能ファイルかのチェックを行う。
	 * @param file　File
	 * @return　true 可
	 */
	private static boolean checkBeforeWritefile(File file) {
		if (file.exists()) {
			if (file.isFile() && file.canWrite()) {
				return true;
			}
		}

		return false;
	}

}
