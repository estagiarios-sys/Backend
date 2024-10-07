package com.systextil.relatorio.domain;

import java.util.Map;

public record ColumnAndTotalizer(
	Map<String, TotalizerTypes> totalizer
) {
//	public ColumnAndTotalizer(Map<String, String> columnAndTotalizer) {
//        this(
//            columnAndTotalizer.entrySet().stream()
//                .collect(
//                    java.util.stream.Collectors.toMap(
//                        Map.Entry::getKey,
//                        entry -> TotalizerTypes.toTotalizerType(entry.getValue())
//                    )
//                )
//        );
//    }
}