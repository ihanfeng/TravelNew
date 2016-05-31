package com.travelogue.resources.exception;

public class ResourceException extends RuntimeException {

	private static final long serialVersionUID = 4226205525581298812L;
	
	public ResourceException(){
		super();
	}
	
	public ResourceException(String s){
		super(s);
	}
	
	public ResourceException(Exception e){
		super(e);
	}

}
