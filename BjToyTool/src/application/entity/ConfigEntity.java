package application.entity;

public class ConfigEntity {

	/** 業務名*/
	private String gyomu;
	/** SCE の場所 初期設定ファイルに記載*/
	private String exeSceDir;
	/** confFileFullName  sce実行場所から紐づき*/
	private String confFileName;
	///////////////////////////////////////////////
	/** 業務フォルダ の場所 */
	private String gyomuDir;
	/** INPUTフォルダ場所 */
	private String input;
	/** OUTPUTfフォルダ */
	private String output;
	/** INPUT退避フォルダ名 */
	private String work;
	/** 実行ログ名（場所）*/
	private String log;
	/** Avaterログ名（場所 */
	private String avaterlog;
	/** NOWDATEフォルダ名 */
	private String nowdate;
	/** watchフォルダ*/
	private String watchDir;

	public String getGyomu() {
		return gyomu;
	}
	public void setGyomu(String gyomu) {
		this.gyomu = gyomu;
	}
	public String getExeSceDir() {
		return exeSceDir;
	}
	public void setExeSceDir(String exeSceDir) {
		this.exeSceDir = exeSceDir;
	}
	public String getConfFileName() {
		return confFileName;
	}
	public void setConfFileName(String confFileName) {
		this.confFileName = confFileName;
	}
	public String getGyomuDir() {
		return gyomuDir;
	}
	public void setGyomuDir(String gyomuDir) {
		this.gyomuDir = gyomuDir;
	}
	public String getInput() {
		return input;
	}
	public void setInput(String input) {
		this.input = input;
	}
	public String getOutput() {
		return output;
	}
	public void setOutput(String output) {
		this.output = output;
	}
	public String getWork() {
		return work;
	}
	public void setWork(String work) {
		this.work = work;
	}
	public String getLog() {
		return log;
	}
	public void setLog(String log) {
		this.log = log;
	}
	public String getAvaterlog() {
		return avaterlog;
	}
	public void setAvaterlog(String avaterlog) {
		this.avaterlog = avaterlog;
	}
	public String getNowdate() {
		return nowdate;
	}
	public void setNowdate(String nowdate) {
		this.nowdate = nowdate;
	}
	public String getWatchDir() {
		return watchDir;
	}
	public void setWatchDir(String watchDir) {
		this.watchDir = watchDir;
	}
}
