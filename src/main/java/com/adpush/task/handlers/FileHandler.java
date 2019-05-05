package com.adpush.task.handlers;

import com.couchbase.client.java.document.JsonDocument;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileHandler {
    
    private static final String FILE_BASE_PATH = "../reports/";
    
    public void initializeParentDirectory() throws IOException {
        createParentDirectoryIfNotExists();
        cleanParentDirectory();
    }
    
    public URL createMonthlyReport(String key, List<JsonDocument> documents) {
        try {
            return createTarGzip(key, documents);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private URL createTarGzip(String fileName, List<JsonDocument> documents) throws IOException {
        
        String outputFilePath = FILE_BASE_PATH + fileName + ".tar.gz";
        File outputFile = new File(outputFilePath);
        
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
             GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(bufferedOutputStream);
             TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(gzipOutputStream)) {
            
            
            for (JsonDocument document : documents) {
                TarArchiveEntry archiveEntry = new TarArchiveEntry(document.id());
                byte[] documentBytes = document.content().toString().getBytes();
                
                archiveEntry.setSize(documentBytes.length);
                
                tarArchiveOutputStream.putArchiveEntry(archiveEntry);
                tarArchiveOutputStream.write(documentBytes);
                tarArchiveOutputStream.closeArchiveEntry();
            }
            
            tarArchiveOutputStream.close();
            return outputFile.toURI().toURL();
        }
    }
    
    private void createParentDirectoryIfNotExists() throws IOException {
        try {
            Files.createDirectories(Paths.get(FILE_BASE_PATH));
        } catch (FileAlreadyExistsException ex) {
        }
    }
    
    private void cleanParentDirectory() {
        File[] files = new File(FILE_BASE_PATH).listFiles();
        
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            file.delete();
        }
    }
}
