package com.systextil.relatorio.domain.data_base_data;

import java.util.ArrayList;
import java.util.Map;

record TreatedLoadedQueryData(
		ArrayList<String> columnsNameOrNickName,
		ArrayList<Object[]> foundObjects,
		Map<String, String> columnsAndTotalizersResult
) {}