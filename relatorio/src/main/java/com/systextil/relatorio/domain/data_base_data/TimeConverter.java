package com.systextil.relatorio.domain.data_base_data;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class TimeConverter {
	
	private TimeConverter() {
		throw new IllegalStateException("Utility class");
	}

	static int convertHHmmssToSeconds(String hhmmss) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime tempo = LocalTime.parse(hhmmss, formatter);
        return tempo.getHour() * 3600 + tempo.getMinute() * 60 + tempo.getSecond();
    }
}