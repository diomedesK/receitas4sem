package com.receitas.app.service;

/**
 * Service layer response for creading, updating and deleting operations
 **/
public class ServiceAPIResponse {
	public String message;
	public int status;

	public ServiceAPIResponse( String message, int status ){
		this.message = message;
		this.status = status;
	}

}
