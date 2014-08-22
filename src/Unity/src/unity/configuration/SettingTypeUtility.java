package unity.configuration;


public class SettingTypeUtility {

	public static String settingTypeToString(SettingType type) {
		return type.toString();
	}
	
	public static SettingType stringToSettingType(String type) {
		
		SettingType settingType = null;

		switch (type) {
		
	    case "stString" :
	    	settingType = SettingType.stString;
	    	break;
	    	
	    case "stBoolean" :
	    	settingType = SettingType.stBoolean;
	    	break;
	    	
	    case "stEncryptedString" :
	    	settingType = SettingType.stEncryptedString;
	    	break;
	    	
	    case "stInteger" :
	    	settingType = SettingType.stInteger;
	    	break;
	    	
	    default :
	    	settingType = SettingType.stUnknown;
	    	break;
		}
		return settingType;
	}
}
