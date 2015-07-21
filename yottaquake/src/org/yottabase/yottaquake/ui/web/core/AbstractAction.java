package org.yottabase.yottaquake.ui.web.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yottabase.yottaquake.db.AbstractDBFacade;
import org.yottabase.yottaquake.db.DBAdapterManager;
import org.yottabase.yottaquake.db.PropertyFile;

public abstract class AbstractAction {
	
	private static final String CONFIG_FILE_PATH = "db.properties";
	
	public final String CONTENT_TYPE_JSON = "application/json";
	
	public abstract void run(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
	
	public AbstractDBFacade getFacade(){
		PropertyFile propertyFile = new PropertyFile(DBAdapterManager.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH));
		DBAdapterManager adapterManager = new DBAdapterManager(propertyFile);
		return adapterManager.getAdapter();
	}
}
