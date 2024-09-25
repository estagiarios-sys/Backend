package com.systextil.relatorio.domain.saved_query;

import java.util.Map;

record TotalizerListing(
	Map<String, String> totalizers
) {
	TotalizerListing(Totalizer totalizer) {
		this(
			Map.of(totalizer.getColumn(), totalizer.getName())
		);
	}
}