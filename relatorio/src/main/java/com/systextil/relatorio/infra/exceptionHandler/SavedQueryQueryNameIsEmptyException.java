package com.systextil.relatorio.infra.exceptionHandler;

public class SavedQueryQueryNameIsEmptyException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public SavedQueryQueryNameIsEmptyException() {
		super("Consulta salva não encontrada com o nome passado");
	}
}