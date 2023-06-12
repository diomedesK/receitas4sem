package com.receitas.app.service;

import com.fasterxml.jackson.annotation.*;

/**
 * Service layer response for creading, updating and deleting operations
 **/
public class ServiceAPIResponse {
	public String message;

	@JsonIgnore
	public int status;

	public ServiceAPIResponse( String message, int status ){
		this.message = message;
		this.status = status;
	}

}
