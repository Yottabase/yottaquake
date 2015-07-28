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
		
		boxArray.add(topLeft);
		boxArray.add(bottomRight);
		return boxArray;
	}
	
	public ArrayList<ArrayList<ArrayList<Double>>> getPolygon(){

		ArrayList<Double> topLeftPoint = getCoordinate().get(1);
		ArrayList<Double> bottomRighttPoint = getCoordinate().get(0);

		ArrayList<Double> topRight = new ArrayList<Double>();
		LatLng p2= new LatLng(topLeftPoint.get(1), bottomRighttPoint.get(0));
		topRight.add(p2.getLng());
		topRight.add(p2.getLat());

		ArrayList<Double> bottomeLeft = new ArrayList<Double>();
		LatLng p4= new LatLng(topLeftPoint.get(0), bottomRighttPoint.get(1));
		bottomeLeft.add(p4.getLng());
		bottomeLeft.add(p4.getLat());
		
		ArrayList<ArrayList<Double> > boxArray = new ArrayList<ArrayList<Double>>();
		boxArray.add(bottomRighttPoint);
		boxArray.add(topRight);
		boxArray.add(topLeftPoint);
		boxArray.add(bottomeLeft);
		boxArray.add(bottomRighttPoint);
		
		ArrayList<ArrayList<ArrayList<Double>>> polygonArray = new ArrayList<ArrayList<ArrayList<Double>>>();
		polygonArray.add(boxArray);	
		
		return polygonArray;
	}
}
