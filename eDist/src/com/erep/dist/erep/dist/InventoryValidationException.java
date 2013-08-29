package com.erep.dist.erep.dist;

public class InventoryValidationException extends Exception {

	private static final long	serialVersionUID	= 1L;

	public InventoryValidationException(){
		super();
	}
	
	public InventoryValidationException(String message) {
		super(message);
	}
}
