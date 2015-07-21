package org.yottabase.yottaquake.ui.web.action.api;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.yottabase.yottaquake.ui.web.core.AbstractAction;

public class EventByMonthAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JSONObject json = new JSONObject();
		
		JSONArray items = new JSONArray();
		json.put("counts", items);
		
		JSONObject mounth = new JSONObject();
		mounth.put("month", "gen");
		mounth.put("count", "10");
		items.put(mounth);
		
		JSONObject mounth2 = new JSONObject();
		mounth2.put("month", "feb");
		mounth2.put("count", "20");
		items.put(mounth2);
		
		
		response.setContentType(this.CONTENT_TYPE_JSON);
		response.getWriter().write(json.toString());
	}

}
