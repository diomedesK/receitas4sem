package com.receitas.app.service;

import com.receitas.app.model.UserModel;

import com.fasterxml.jackson.annotation.*;

@JsonRootName("sessionData")
public class SessionData {
	private UserModel user;
	private String sessionToken;

	public SessionData( UserModel user, String sessionToken ){
		this.user = user;
		this.sessionToken = sessionToken;
	}

	public UserModel getUser(){
		return this.user;
	}

	public boolean setUser( UserModel user ){
		this.user = user;
		return true;
	}

	public String getSessionToken(){
		return this.sessionToken;
	}

	public boolean setSessionToken( String sessionToken ){
		this.sessionToken = sessionToken;
		return true;
	}

}
