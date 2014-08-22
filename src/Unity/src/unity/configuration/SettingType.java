package unity.configuration;

public enum SettingType {

	stString(0),
	stBoolean(1),
	stInteger(2),
	stEncryptedString(3),
	stUnknown(99);
	
	private int _value;
	
	private SettingType(int value){
		this._value = value;
	}
	
	public void setSettingType(int value) {
		this._value = value;
	}

	public Integer getSettingType() {
		return this._value;
	}
}
