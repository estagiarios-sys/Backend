package com.systextil.relatorio.dataBaseData;

import java.util.ArrayList;

record LoadedQueryData(
	ArrayList<String> columnsNickName,
	ArrayList<Object[]> foundObjects
) {}
