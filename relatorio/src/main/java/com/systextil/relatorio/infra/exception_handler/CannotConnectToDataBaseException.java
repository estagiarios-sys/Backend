package com.systextil.relatorio.infra.exception_handler;

import java.sql.SQLException;

public class CannotConnectToDataBaseException extends SQLException {
	
	private static final long serialVersionUID = 1L;

	public CannotConnectToDataBaseException(String message) {
		super(message);
	}
}