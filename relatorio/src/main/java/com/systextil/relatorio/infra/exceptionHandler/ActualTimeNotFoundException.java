package com.systextil.relatorio.infra.exceptionHandler;

import java.sql.SQLException;

public class ActualTimeNotFoundException extends SQLException {
	
	private static final long serialVersionUID = 1L;

	public ActualTimeNotFoundException(String message) {
		super(message);
	}
}