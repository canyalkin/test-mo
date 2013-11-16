package com.can.summary.exceptions;

public class MissingFileException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8664864863103959216L;

	public MissingFileException(String file) {
		super(file);
	}

}
