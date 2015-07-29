package org.yottabase.yottaquake.ui.web.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractAction {
	
	public final String CONTENT_TYPE_JSON = "application/json";
	
	public abstract void run(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
}
