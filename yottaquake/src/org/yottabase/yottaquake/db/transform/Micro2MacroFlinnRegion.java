package org.yottabase.yottaquake.db.transform;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;

public class Micro2MacroFlinnRegion {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		DBFacade facade = DBAdapterManager.getFacade();
		Set<String> regionsAggregates = facade.getDistinctRegionsAggregates();;
		
		for (String ra : regionsAggregates) {
			System.out.println(ra);
			Iterable<Document> innerRegions = facade.getRegionsByAggregate(ra);
			
			List<Point2D> points = new LinkedList<Point2D>();
			for (Document ir : innerRegions) {
				// polygons=[ [[p1x1,p1y1],[p1x2,p1y2],...], [[p2x1,p2y1],[p2x2,p2y2],...], ... ]
				ArrayList<ArrayList<ArrayList<Double>>> polygons = new ArrayList<ArrayList<ArrayList<Double>>>();
				Document geometry = (Document) ir.get("geometry");
				
				String geometryType = geometry.getString("type");
				if (geometryType.equals("Polygon")) {
					// add single polygon to polygons list
					ArrayList<ArrayList<Double>> poly = ((ArrayList<ArrayList<ArrayList<Double>>>) geometry.get("coordinates")).get(0);
					polygons.add(poly);
					
				} else {
					// add each sub-polygon to polygons list
					ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> polys = (ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>) geometry.get("coordinates");
					for (ArrayList<ArrayList<ArrayList<Double>>> p : polys) {
						ArrayList<ArrayList<Double>> poly = p.get(0);
						polygons.add(poly);
					}
					
				}
				
				// convert vertices in each polygon in polygons list from array to Point2D
				for (ArrayList<ArrayList<Double>> polygon : polygons) {
					for (ArrayList<Double> vertex : polygon) {
						double x = vertex.get(0).doubleValue();
						double y = vertex.get(1).doubleValue();
						Point2D point = new Point2D(x, y);
						
						points.add(point);
					}
				}
			}
			
			// convert list of Point2D to array
			Point2D[] pts = points.toArray( new Point2D[points.size()] );	
			GrahamScan gs = new GrahamScan(pts);
			
			// convert Iterable<Point2D> hull to ArrayList<ArrayList<Double>>>
			ArrayList<ArrayList<Double>> mr_coordinates = new ArrayList<ArrayList<Double>>();
			for (Point2D p : gs.hull()) {
				ArrayList<Double> pair = new ArrayList<Double>();
				pair.add(p.x());
				pair.add(p.y());
				
				mr_coordinates.add(pair);
			}
			
			// create JSON 
			Document mr_properties = new Document();
			mr_properties.put("name", ra);
			
			Document mr_geometry = new Document();
			mr_geometry.put("type", "Polygon");
			mr_geometry.put("coordinates", mr_coordinates);
			
			Document macroRegion = new Document();
			macroRegion.put("type", "Feature");
			macroRegion.put("properties", mr_properties);
			macroRegion.put("geometry", mr_geometry);
			
			System.out.println(macroRegion.toJson());
		}

	}

}
