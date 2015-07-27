package org.yottabase.yottaquake.db.mongodb;

public enum CountryDetailLevel {
	
	LOW,
	MEDIUM,
	HIGH;
	
	public CountryDetailLevel getValueFor(String s) {
		switch (s) {
		case "low":
			return LOW;
		case "medium":
			return MEDIUM;
		case "high":
			return HIGH;
		default:
			return null;
		}
	}

}
