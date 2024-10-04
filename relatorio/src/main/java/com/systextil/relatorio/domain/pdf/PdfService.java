package com.systextil.relatorio.domain.pdf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
class PdfService {

    @Value("${pdf.storage.location}")
    private String storageLocation;

    String savePdf(byte[] fileBytes, String pdfTitle) throws IOException {
        Path storagePath = Paths.get(storageLocation);
        
        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }
        String fileName = UUID.randomUUID() + "_" + pdfTitle + ".pdf";
        Path filePath = storagePath.resolve(fileName);
        Files.write(filePath, fileBytes);

        return filePath.toString();
    }
}