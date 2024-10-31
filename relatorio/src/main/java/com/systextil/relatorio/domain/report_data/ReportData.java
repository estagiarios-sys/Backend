package com.systextil.relatorio.domain.report_data;

import java.util.List;
import java.util.Map;

/** Record que retorna os dados coletados no banco */
record ReportData(
	Map<String, String> columnsNameAndNickName,
	List<Object[]> foundObjects,
	List<String> totalizersResult
) {}