package com.systextil.relatorio.domain.dataBaseData;

/** Record que carrega as relações das tabelas no banco */
record RelationshipData(
		String tabelas,
		String join
) {}
