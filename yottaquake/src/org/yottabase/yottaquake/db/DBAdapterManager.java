package org.yottabase.yottaquake.db;

public class DBAdapterManager {
	
	private PropertyFile properties;
	
	public DBAdapterManager(PropertyFile properties) {
		super();
		this.properties = properties;
	}

	public PropertyFile getProperties() {
		return properties;
	}

	public void setProperties(PropertyFile properties) {
		this.properties = properties;
	}
	
	public AbstractDBFacade getAdapter(){
		AbstractDBFacade adapter = null;
		
		String adapterFactoryKey =  "db.factory";

		String factoryClassName = properties.get(adapterFactoryKey);

		try {
			DBFacadeFactory adapterFactory = (DBFacadeFactory) Class
					.forName(factoryClassName).newInstance();
			adapter = adapterFactory.createService(properties);

		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return adapter;
	}

}
