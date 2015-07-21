package org.yottabase.yottaquake.ui.web.action.page;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yottabase.yottaquake.ui.web.core.AbstractAction;

public class HomeAction extends AbstractAction{

	@Override
	public void run(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("view/home.jsp").forward(request, response);
		
	}

}
