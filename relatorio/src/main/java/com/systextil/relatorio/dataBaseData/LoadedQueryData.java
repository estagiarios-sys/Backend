package com.systextil.relatorio.dataBaseData;

import java.util.ArrayList;

record LoadedQueryData(
	ArrayList<String> columnsBanco,
	ArrayList<Object[]> foundObjects
) {}
