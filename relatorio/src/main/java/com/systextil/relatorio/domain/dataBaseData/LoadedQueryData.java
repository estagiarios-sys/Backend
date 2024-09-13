package com.systextil.relatorio.domain.dataBaseData;

import java.util.ArrayList;

record LoadedQueryData(
	ArrayList<String> columnsNickName,
	ArrayList<Object[]> foundObjects,
	ArrayList<String> totalizersResults
) {}
