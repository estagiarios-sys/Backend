package com.systextil.relatorio.domain.pdf;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

class StorageAccessor {
	
	@Value("${pdf.storage.location}")
    private static String storageLocation;
	
	private StorageAccessor() {
		throw new IllegalStateException("Classe utilitária");
	}

	static void deleteFile(String filePath) throws IOException {
		Files.delete(Paths.get(filePath));
	}
	
	static String savePdf(byte[] fileBytes, String pdfTitle) throws IOException {
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