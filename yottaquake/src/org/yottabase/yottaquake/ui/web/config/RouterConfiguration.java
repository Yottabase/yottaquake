package org.yottabase.yottaquake.ui.web.config;

import java.util.ArrayList;
import java.util.List;

import org.yottabase.yottaquake.ui.web.core.Route;

public class RouterConfiguration {
	
	public List<Route> getRoutes(){
		
		List<Route> routes = new ArrayList<Route>();
		
		//api
		routes.add(new Route("api-events-by-month", "org.yottabase.yottaquake.ui.web.action.api.EventsByMonthAction"));
		routes.add(new Route("api-events-by-year", "org.yottabase.yottaquake.ui.web.action.api.EventsByYearAction"));
		
		routes.add(new Route("api-events", "org.yottabase.yottaquake.ui.web.action.api.EventsAction"));
		
		routes.add(new Route("api-countries", "org.yottabase.yottaquake.ui.web.action.api.CountriesAction"));
		routes.add(new Route("api-flinn-regions", "org.yottabase.yottaquake.ui.web.action.api.FlinnRegionsAction"));
		
		
		//pages
		routes.add(new Route("home", "org.yottabase.yottaquake.ui.web.action.page.HomeAction"));
		
		return routes;
	}

}
