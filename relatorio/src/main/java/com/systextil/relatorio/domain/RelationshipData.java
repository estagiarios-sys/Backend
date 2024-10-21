package com.systextil.relatorio.domain;

/** Record que carrega as relações das tabelas no banco */
public record RelationshipData(
		String tablesPair,
		String join
) {}