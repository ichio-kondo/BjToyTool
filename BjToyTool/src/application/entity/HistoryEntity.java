package application.entity;

public class HistoryEntity {


	private String status;
	private String startDate;
	private String endDate;
	private String gyomu;
	private String sce;
	private String resultDir;
	private String user;
	private String logfile;


	public String getStatus() {
		return this.status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getGyomu() {
		return gyomu;
	}
	public void setGyomu(String gyomu) {
		this.gyomu = gyomu;
	}
	public String getSce() {
		return sce;
	}
	public void setSce(String sce) {
		this.sce = sce;
	}
	public String getResultDir() {
		return resultDir;
	}
	public void setResultDir(String resultDir) {
		this.resultDir = resultDir;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getLogfile() {
		return logfile;
	}
	public void setLogfile(String logfile) {
		this.logfile = logfile;
	}

}
