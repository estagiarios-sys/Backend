package com.systextil.relatorio.domain.report_data;

import java.util.ArrayList;
import java.util.Map;

record TreatedReportData(
		ArrayList<String> columnsNameOrNickName,
		ArrayList<Object[]> foundObjects,
		Map<String, String> columnsAndTotalizersResult
) {}