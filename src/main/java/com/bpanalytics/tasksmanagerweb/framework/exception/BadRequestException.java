package com.bpanalytics.tasksmanagerweb.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.BAD_REQUEST )
public class BadRequestException extends RuntimeException {

	private static final long serialVersionUID = 7068310310467892258L;
	
	public BadRequestException(String message) {
		super(message);
	}

}
