package com.systextil.relatorio.infra.exception_handler;

import java.sql.SQLException;

public class DataBaseConnectionException extends SQLException {
	
	private static final long serialVersionUID = 1L;

	public DataBaseConnectionException(String message) {
		super(message);
	}
}