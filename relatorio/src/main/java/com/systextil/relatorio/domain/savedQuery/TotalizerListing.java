package com.systextil.relatorio.domain.savedQuery;

import java.util.HashMap;
import java.util.Map;

record TotalizerListing(
	Map<String, String> totalizers
) {
	
	TotalizerListing(Totalizer totalizer) {
		this(
			new HashMap<String, String>() {
				private static final long serialVersionUID = 1L;

			{
				put(totalizer.getColumn(), totalizer.getName());
			}}
		);
	}
}