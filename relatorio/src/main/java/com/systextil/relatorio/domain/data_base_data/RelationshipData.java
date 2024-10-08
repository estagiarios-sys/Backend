package com.systextil.relatorio.domain.data_base_data;

/** Record que carrega as relações das tabelas no banco */
record RelationshipData(
		String tables,
		String join
) {}