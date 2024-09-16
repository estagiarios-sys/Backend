package com.systextil.relatorio.domain.dataBaseData;

import java.util.ArrayList;
import java.util.Map;

record LoadedQueryData(
	Map<String, String> columnsNameAndNickName,
	ArrayList<Object[]> foundObjects,
	ArrayList<String> totalizersResults
) {}
