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
	
	
	public ArrayList<ArrayList<Double>> getCoordinatesPair(){
		ArrayList<ArrayList<Double> > boxArray = new ArrayList<ArrayList<Double> >();	
		boxArray.add(this.topLeft.asArray());
		boxArray.add(this.bottomRight.asArray());
		
		return boxArray;
	}
	
	
	public ArrayList<ArrayList<ArrayList<Double>>> toPolygon() {
		ArrayList<ArrayList<Double>> points = null;
		ArrayList<ArrayList<ArrayList<Double>>> polygon = null;

		LatLng topRightPoint = new LatLng(this.topLeft.getLat(), this.bottomRight.getLng());
		LatLng bottomLeftPoint = new LatLng(this.bottomRight.getLat(), this.topLeft.getLng());
		
		ArrayList<Double> topRight = topRightPoint.asArray();
		ArrayList<Double> bottomLeft = bottomLeftPoint.asArray();
		ArrayList<Double> topLeft = this.topLeft.asArray();
		ArrayList<Double> bottomRight = this.bottomRight.asArray();
		
		points = new ArrayList<ArrayList<Double>>();
		points.add(bottomRight);
		points.add(topRight);
		points.add(topLeft);
		points.add(bottomLeft);
		points.add(bottomRight);

		polygon = new ArrayList<ArrayList<ArrayList<Double>>>();
		polygon.add(points);	
		
		return polygon;
	}
}
