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
	
	public static TotalizerTypes toTotalizerType(String totalizerString) {
		switch (totalizerString) {
			case "AVG": {
				return TotalizerTypes.AVG;
			}
			case "COUNT": {
				return TotalizerTypes.COUNT;
			}
			case "MAX": {
				return TotalizerTypes.MAX;
			}
			case "MIN": {
				return TotalizerTypes.MIN;
			}
			case "SUM": {
				return TotalizerTypes.SUM;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + totalizerString);
		}
	}

}