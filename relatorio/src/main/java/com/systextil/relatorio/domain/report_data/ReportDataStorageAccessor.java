package com.systextil.relatorio.domain.report_data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.systextil.relatorio.domain.RelationshipData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
class ReportDataStorageAccessor {

    private final String relationshipsWithJoinsJsonFilePath;

    ReportDataStorageAccessor(@Value("${relationships_with_joins.json.file.path}") String relationshipsWithJoinsJsonFilePath) {
        this.relationshipsWithJoinsJsonFilePath = relationshipsWithJoinsJsonFilePath;
    }

    List<RelationshipData> findRelationshipData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        assert relationshipsWithJoinsJsonFilePath != null;
        Path fileOfJoinsPath = Paths.get(relationshipsWithJoinsJsonFilePath);
        String json = Files.readString(fileOfJoinsPath);

        return objectMapper.readValue(
                json, objectMapper
                        .getTypeFactory()
                        .constructCollectionType(List.class, RelationshipData.class)
        );
    }
}