package org.yottabase.yottaquake.core;

import java.util.ArrayList;

public class BoundingBox {
	
	public LatLng topLeft;
	
	public LatLng bottomRight;

	public BoundingBox(LatLng topLeft, LatLng bottomRight) {
		super();
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
	}
	
	public LatLng getTopLeft() {
		return topLeft;
	}

	public void setTopLeft(LatLng topLeft) {
		this.topLeft = topLeft;
	}

	public LatLng getBottomRight() {
		return bottomRight;
	}

	public void setBottomRight(LatLng bottomRight) {
		this.bottomRight = bottomRight;
	}
	
	public ArrayList<ArrayList<Double>> getCoordinate(){
		ArrayList<ArrayList<Double> > boxArray = new ArrayList<ArrayList<Double> >();
		ArrayList<Double> bottomRight = new ArrayList<Double>();
		bottomRight.add(this.getBottomRight().getLng());
		bottomRight.add(this.getBottomRight().getLat());
		
		ArrayList<Double> topLeft = new ArrayList<Double>();
		topLeft.add(this.getTopLeft().getLng());
		topLeft.add(this.getTopLeft().getLat());
		
		boxArray.add(bottomRight);
		boxArray.add(topLeft);
		return boxArray;
	}
}
