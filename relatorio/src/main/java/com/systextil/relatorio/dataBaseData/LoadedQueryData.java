package com.systextil.relatorio.dataBaseData;

import java.util.ArrayList;

record LoadedQueryData(
	ArrayList<String> columns,
	ArrayList<Object[]> foundObjects
) {}
