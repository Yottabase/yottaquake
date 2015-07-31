package org.yottabase.yottaquake.ui.web.action.api;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.EventFilter;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;
import org.yottabase.yottaquake.ui.web.utils.ParamsUtils;

public class EventsAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DBFacade facade = DBAdapterManager.getFacade();
		
		// params
		BoundingBox box = ParamsUtils.extractBoundingBox(request);
		EventFilter eventFilter = ParamsUtils.extractEventFilter(request);
		
		//data
		Iterable<Document> events = facade.getEvents(box, eventFilter);
		
		JSONObject result = new JSONObject();
		JSONArray items = new JSONArray();
		result.put("items", items);
		
		for(Document doc : events){
			JSONObject event = new JSONObject(doc);
			items.put(event);
		}
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(result.toString());

		facade.close();
	}

}
