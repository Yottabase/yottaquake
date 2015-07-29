package org.yottabase.yottaquake.core;

import java.util.ArrayList;

public class LatLng {

	public double lat, lng;

	public LatLng(double lat, double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public ArrayList<Double> asArray() {
		ArrayList<Double> arrayPoint = new ArrayList<Double>();
		arrayPoint.add(lng);
		arrayPoint.add(lat);
		
		return arrayPoint;
	}
	
}
