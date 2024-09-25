package com.systextil.relatorio.domain.data_base_data;

enum Totalizer {
	
	AVG, COUNT, MAX, MIN, SUM;
	
	String toPortuguese() {
		switch (this) {
			case AVG:
				return "Média";
			case COUNT:
				return "Contador";
			case MAX:
				return "Máximo";
			case MIN:
				return "Mínimo";
			case SUM:
				return "Soma";
			default:
				return "Desconhecido";
		}
	}

}