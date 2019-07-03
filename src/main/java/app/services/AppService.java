package app.services;

import app.models.Application;

public interface AppService extends ModelService<Application>{

	public Application findByAppCode(String code) throws Exception;
}
