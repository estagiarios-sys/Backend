package com.systextil.relatorio.domain.pdf;

import java.util.Map;

record PdfTotalizerSaving(
	Map<String, String> totalizer
) {}