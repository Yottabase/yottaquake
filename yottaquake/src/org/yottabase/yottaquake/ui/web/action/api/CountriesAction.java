package org.yottabase.yottaquake.ui.web.action.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.CountryDetailLevel;
import org.yottabase.yottaquake.core.LatLng;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;

public class CountriesAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// init bbox
		BoundingBox box = null;
		
		String paramTopLeftLat  =  this.cleanParam(request.getParameter("topLeftLat"));
		String paramTopLeftLng  =  this.cleanParam(request.getParameter("topLeftLng"));
		String paramBottomRightLat  =  this.cleanParam(request.getParameter("bottomRightLat"));
		String paramBottomRightLng  =  this.cleanParam(request.getParameter("bottomRightLng"));
		
		if(paramTopLeftLat != null && paramTopLeftLng != null & paramBottomRightLat != null & paramBottomRightLng != null){
			
			LatLng topLeft = new LatLng(Double.parseDouble(paramTopLeftLat), Double.parseDouble(paramTopLeftLng));
			LatLng bottomRight = new LatLng(Double.parseDouble(paramBottomRightLat), Double.parseDouble(paramBottomRightLng));
			
			box = new BoundingBox(topLeft, bottomRight);
		}
		
		//init levelQuality		
		String mapDetailParam =  this.cleanParam(request.getParameter("levelQuality"));
		if(mapDetailParam == null){
			response.getWriter().write("Parametro levelQuality richiesto");
			return;
		}
		CountryDetailLevel mapDetailLevel = CountryDetailLevel.valueOf(mapDetailParam);
		
		
		DBFacade facade = DBAdapterManager.getFacade();
		
		JSONArray items = new JSONArray();
		for(Document doc : facade.getCountriesWithEventsCount(mapDetailLevel, box)){
			
			JSONObject obj = new JSONObject(doc.toJson());
			items.put(obj);
			
		}
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(items.toString());

	}

}
