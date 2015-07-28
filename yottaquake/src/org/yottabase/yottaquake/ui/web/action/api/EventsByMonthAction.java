package org.yottabase.yottaquake.ui.web.action.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.yottabase.yottaquake.db.DBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;

public class EventsByMonthAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DBFacade facade = DBAdapterManager.getFacade();
		
		Iterable<Document> result;
		String year =  this.cleanParam(request.getParameter("year"));
		
		if(year == null ){
			result = facade.countByYearMonth();
		}else{
			result = facade.countByMonthInYear(Integer.parseInt(year));
		}
		
		JSONArray json = new JSONArray();
		
		for(Document doc : result ){
			JSONObject mounth = new JSONObject();
			if(year == null ){
				mounth.put("year", doc.get("year"));
			}else{
				mounth.put("year", year);
			}
			mounth.put("month", doc.get("month"));
			mounth.put("count", doc.get("count"));
			json.put(mounth);
		}
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(json.toString());
		
//		facade.close();
	}

}
