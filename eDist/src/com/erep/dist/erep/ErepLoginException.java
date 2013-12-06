package com.erep.dist.erep;

public class ErepLoginException extends Exception {

	private static final long	serialVersionUID	= 1L;

	public ErepLoginException(String username) {
		super("Could not login " + username);
	}

}
