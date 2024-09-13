package com.systextil.relatorio.infra.exceptionHandler;

public class TotalizerFieldsMismatchException extends IllegalArgumentException {
	
	public TotalizerFieldsMismatchException(String message) {
		super(message);
	}
}
