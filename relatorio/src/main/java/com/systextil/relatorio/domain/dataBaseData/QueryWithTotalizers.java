package com.systextil.relatorio.domain.dataBaseData;

import java.util.ArrayList;

/** Classe necessária no método DataBaseRepository.getTotalizersResults() */
record QueryWithTotalizers(
		
	String query,
	ArrayList<Totalizer> totalizers

) {}
