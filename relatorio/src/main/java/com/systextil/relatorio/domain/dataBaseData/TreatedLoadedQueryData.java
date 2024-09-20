package com.systextil.relatorio.domain.dataBaseData;

import java.util.ArrayList;
import java.util.Map;

public record TreatedLoadedQueryData(
		ArrayList<String> columnsNameOrNickName,
		ArrayList<Object[]> foundObjects,
		Map<String, String> columnsAndTotalizersResult
) {}