package com.systextil.relatorio.domain.report_data;

import java.util.ArrayList;
import java.util.Map;

/** Record que retorna os dados coletados no banco */
record ReportData(
	Map<String, String> columnsNameAndNickName,
	ArrayList<Object[]> foundObjects,
	ArrayList<String> totalizersResult
) {}