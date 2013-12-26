package com.versacomllc.emailer;

public class Result {

	private final int code;
	private final String message;
	
	public Result(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}


}
