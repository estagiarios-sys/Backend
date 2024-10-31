package com.systextil.relatorio.domain.report_data;

import java.util.List;
import java.util.Map;

record TreatedReportData(
		List<String> columnsNameOrNickName,
		List<Object[]> foundObjects,
		Map<String, String> columnsAndTotalizersResult
) {}