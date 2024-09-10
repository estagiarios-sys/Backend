package com.systextil.relatorio.dataBaseData;

import java.util.ArrayList;

public record QueryWithTotalizers(
		
	String query,
	ArrayList<Totalizer> listOfTotalizers

) {}
