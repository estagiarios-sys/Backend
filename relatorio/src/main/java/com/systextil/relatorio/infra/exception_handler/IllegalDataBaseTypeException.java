package com.systextil.relatorio.infra.exception_handler;

public class IllegalDataBaseTypeException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;

	public IllegalDataBaseTypeException(String dataBaseType) {
		super("Tipo do banco de dados não reconhecido ou suportado: " + dataBaseType + ". Confira no application.properties");
	}
}