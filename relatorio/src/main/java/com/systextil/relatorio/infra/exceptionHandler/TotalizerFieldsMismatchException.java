package com.systextil.relatorio.infra.exceptionHandler;

/** 
 * Excessão lançada quando os campos totalizersQuery e totalizers da SavedQuerySaving vêm separados.
 * 
 * Classe pai: {@link IllegalArgumentException} 
 */
public class TotalizerFieldsMismatchException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1L;

	public TotalizerFieldsMismatchException(String message) {
		super(message);
	}
}