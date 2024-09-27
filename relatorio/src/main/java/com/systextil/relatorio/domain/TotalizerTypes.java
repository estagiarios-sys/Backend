package com.systextil.relatorio.domain;

public enum TotalizerTypes {
	
	AVG, COUNT, MAX, MIN, SUM;
	
	public String toPortuguese() {
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