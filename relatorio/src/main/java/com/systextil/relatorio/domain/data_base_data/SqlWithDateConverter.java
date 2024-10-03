package com.systextil.relatorio.domain.data_base_data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class SqlWithDateConverter {
	
	private SqlWithDateConverter() {
		throw new IllegalStateException("Utility class");
	}
	
	static String toSqlWithDdMMMyyyy(String sqlWithYyyyMMdd) throws ParseException {
		
		if (sqlHasYyyyMMdd(sqlWithYyyyMMdd)) {
			List<String> yyyyMMddDates = new ArrayList<>();
			List<String> ddMMMyyyyDates = new ArrayList<>();
			String sqlWithDdMMMyyyy = null;
			Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
			Matcher matcher = pattern.matcher(sqlWithYyyyMMdd);
			
			while (matcher.find()) {
				yyyyMMddDates.add(matcher.group());
			}
			
			for (String yyyyMMddDate : yyyyMMddDates) {
				ddMMMyyyyDates.add(toDdMMMyyyy(yyyyMMddDate));
			}
			sqlWithDdMMMyyyy = sqlWithYyyyMMdd;
			
			for (int i = 0; i < yyyyMMddDates.size(); i++) {
				 sqlWithDdMMMyyyy = sqlWithDdMMMyyyy.replace(yyyyMMddDates.get(i), ddMMMyyyyDates.get(i));
			}
			
			return sqlWithDdMMMyyyy;
		} else {
			return sqlWithYyyyMMdd;
		}
	}
	
	private static boolean sqlHasYyyyMMdd(String sql) {
		Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
		Matcher matcher = pattern.matcher(sql);
		
		return matcher.find();
	}
	
	private static String toDdMMMyyyy(String yyyyMMdd) throws ParseException {
		SimpleDateFormat yyyyMMddFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat ddMMMyyyyFormat = new SimpleDateFormat("dd-MM-yyyy");
		Date yyyyMMddDate = yyyyMMddFormat.parse(yyyyMMdd);
		
		return ddMMMyyyyFormat.format(yyyyMMddDate);
	}
}