package com.systextil.relatorio.domain.savedQuery;

public record TotalizerListing(
		
		String totalizer
		
) {
	
	public TotalizerListing(Totalizer totalizer) {
		this(
				totalizer.getTotalizer()
		);
	}
	
}
