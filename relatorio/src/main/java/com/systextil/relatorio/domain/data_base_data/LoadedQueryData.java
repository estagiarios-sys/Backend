package com.systextil.relatorio.domain.data_base_data;

import java.util.ArrayList;
import java.util.Map;

/** Record que retorna os dados coletados no banco */
record LoadedQueryData(
	Map<String, String> columnsNameAndNickName,
	ArrayList<Object[]> foundObjects,
	ArrayList<String> totalizersResult
) {}