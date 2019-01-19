package com.haeungun.index4j.exceptions;

public class UnsupportedRelevanceException extends Exception {
	
	private static final String errMsg = "Unsupported relevance [%s]";
	
	public UnsupportedRelevanceException(String input) {
		super(String.format(errMsg, input));
	}

}
