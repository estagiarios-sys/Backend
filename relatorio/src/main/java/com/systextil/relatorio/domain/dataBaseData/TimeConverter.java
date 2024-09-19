package com.systextil.relatorio.domain.dataBaseData;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class TimeConverter {

	static int convertHHmmssToSeconds(String HHmmss) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime tempo = LocalTime.parse(HHmmss, formatter);
        return tempo.getHour() * 3600 + tempo.getMinute() * 60 + tempo.getSecond();
    }
}
