package app.services.impl;
import org.javalite.activejdbc.LazyList;

import app.models.Application;
import app.services.AppService;

public class AppServiceImpl implements AppService {

	@Override
	public LazyList<?> findAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Application findById(String id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Application update(Application model) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Application create(Application model) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int count() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean exist(String id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Application findByAppCode(String code) throws Exception {
		try {
			//Base.open();
			LazyList<Application> apps = Application.find("app_code = ?", code);
			int size = apps.size();
			if (size > 0) {
				return apps.get(0);
			}
			throw new Exception("No application found with app_code: " + code);
		} finally {
			//Base.close();
		}
	}

	@Override
	public boolean delete(String id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
