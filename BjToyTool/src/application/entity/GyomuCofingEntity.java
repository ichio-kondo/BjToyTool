package application.entity;

import application.bjcommon.BjExcelEntity;

public class GyomuCofingEntity implements BjExcelEntity {
	//SEQ	項目名称	変数名	変数値
	private String seq;
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	public String getItemnName() {
		return itemnName;
	}
	public void setItemnName(String itemnName) {
		this.itemnName = itemnName;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getVariableValue() {
		return variableValue;
	}
	public void setVariableValue(String variableValue) {
		this.variableValue = variableValue;
	}
	private String itemnName;
	private String variableName;
	private String variableValue;
	@Override
	public String[] getColIdArray() {
		// TODO 自動生成されたメソッド・スタブ
		String n[] = {"seq","itemnName","variableName","variableValue"};
		return n;
	}

}
