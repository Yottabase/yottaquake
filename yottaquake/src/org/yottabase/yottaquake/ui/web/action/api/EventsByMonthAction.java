package org.yottabase.yottaquake.ui.web.action.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;

public class EventsByMonthAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AbstractDBFacade facade = this.getFacade();
		
		long count = facade.countEvents();
		
		
		JSONObject json = new JSONObject();
		
		JSONArray items = new JSONArray();
		json.put("countByMonth", items);
		json.put("count", count);
		
		JSONObject mounth;
		
		mounth = new JSONObject();
		mounth.put("month", "gen");
		mounth.put("year", "2012");
		mounth.put("count", "200");
		items.put(mounth);
		
		mounth = new JSONObject();
		mounth.put("month", "feb");
		mounth.put("year", "2012");
		mounth.put("count", "220");
		items.put(mounth);
		
		mounth = new JSONObject();
		mounth.put("month", "mar");
		mounth.put("year", "2012");
		mounth.put("count", "310");
		items.put(mounth);
		
		mounth = new JSONObject();
		mounth.put("month", "apr");
		mounth.put("year", "2012");
		mounth.put("count", "120");
		items.put(mounth);
		
		mounth = new JSONObject();
		mounth.put("month", "mag");
		mounth.put("year", "2012");
		mounth.put("count", "150");
		items.put(mounth);
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(json.toString());
		
		facade.close();
	}

}
