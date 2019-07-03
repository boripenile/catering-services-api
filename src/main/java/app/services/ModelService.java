package app.services;

import org.javalite.activejdbc.LazyList;

public interface ModelService<T> {

	public LazyList<?> findAll() throws Exception;
	
	public T findById(String id) throws Exception;
	
	public T update(T model) throws Exception;
	
	public T create(T model) throws Exception;
	
	public boolean delete(String id) throws Exception;
	
	public int count() throws Exception;
	
	public boolean exist(String id) throws Exception;
	
}
