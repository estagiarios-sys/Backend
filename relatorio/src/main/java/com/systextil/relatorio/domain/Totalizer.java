package com.systextil.relatorio.domain;

public enum Totalizer {
	
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
	
	public static Totalizer toTotalizerType(String totalizerString) {
		switch (totalizerString) {
			case "AVG": {
				return Totalizer.AVG;
			}
			case "COUNT": {
				return Totalizer.COUNT;
			}
			case "MAX": {
				return Totalizer.MAX;
			}
			case "MIN": {
				return Totalizer.MIN;
			}
			case "SUM": {
				return Totalizer.SUM;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + totalizerString);
		}
	}

}