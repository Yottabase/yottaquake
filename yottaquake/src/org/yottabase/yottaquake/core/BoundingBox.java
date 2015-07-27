package org.yottabase.yottaquake.core;

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
}
