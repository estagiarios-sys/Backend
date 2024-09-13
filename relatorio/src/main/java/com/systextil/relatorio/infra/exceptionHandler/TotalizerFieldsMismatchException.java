package com.systextil.relatorio.infra.exceptionHandler;

/** Excessão lançada quando os campos totalizersQuery e totalizers do SavedQuerySaving vêm separados */
public class TotalizerFieldsMismatchException extends IllegalArgumentException {
	
	public TotalizerFieldsMismatchException(String message) {
		super(message);
	}
}
