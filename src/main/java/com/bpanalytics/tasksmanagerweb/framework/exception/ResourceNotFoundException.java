package com.bpanalytics.tasksmanagerweb.framework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus( value = HttpStatus.NOT_FOUND )
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -5591090876502762652L;
}
