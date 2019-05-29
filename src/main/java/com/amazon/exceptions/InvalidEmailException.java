package com.amazon.exceptions;

public class InvalidEmailException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidEmailException() {
		super();
	}

	public InvalidEmailException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

	public InvalidEmailException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public InvalidEmailException(String arg0) {
		super(arg0);
	}

	public InvalidEmailException(Throwable arg0) {
		super(arg0);
	}
	
	

}
