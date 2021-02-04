package com.shopJpa.exception;

public class NotEnoughStockException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotEnoughStockException() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public NotEnoughStockException(String message) {
		// TODO Auto-generated constructor stub
		super(message);
	}
	
	public NotEnoughStockException(String message, Throwable cause) {
		// TODO Auto-generated constructor stub
		super(message, cause);
	}
	
	public NotEnoughStockException(Throwable cause) {
		// TODO Auto-generated constructor stub
		super(cause);
	}
	
}
