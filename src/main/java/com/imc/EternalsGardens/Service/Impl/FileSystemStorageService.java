package com.imc.EternalsGardens.Service.Impl;

import com.imc.EternalsGardens.Service.Interfaces.IStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileSystemStorageService implements IStorageService {

    // Base path pointing to Frontend Assets
    private final Path rootLocation = Paths
            .get("c:/Users/Izan/DAW/2DAW/EternalsGardens/eternals-gardens-front/src/assets");

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        return store(file, ""); // Default to root or common folder
    }

    @Override
    public String store(MultipartFile file, String subFolder) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file.");
            }

            // Generate unique filename
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String newFilename = UUID.randomUUID().toString() + extension;

            // Determine destination folder
            Path destinationFolder = this.rootLocation;
            if (subFolder != null && !subFolder.trim().isEmpty()) {
                destinationFolder = this.rootLocation.resolve(subFolder);
                if (!Files.exists(destinationFolder)) {
                    Files.createDirectories(destinationFolder);
                }
            }

            Path destinationFile = destinationFolder.resolve(Paths.get(newFilename))
                    .normalize().toAbsolutePath();

            // Security check
            if (!destinationFile.startsWith(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            // Return absolute backend URL (avoids Angular dev-server race conditions)
            String folderPath = (subFolder != null && !subFolder.isEmpty()) ? subFolder + "/" : "";
            // Changed from /images/ to /assets/ to match new MvcConfig and root location
            return "http://localhost:8080/assets/" + folderPath.replace("\\", "/") + newFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        // Not implemented for safety
    }
}
