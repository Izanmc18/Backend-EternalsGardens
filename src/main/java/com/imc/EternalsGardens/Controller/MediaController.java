package com.imc.EternalsGardens.Controller;

import com.imc.EternalsGardens.Service.Interfaces.IStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
@RequiredArgsConstructor
public class MediaController {

    private final IStorageService storageService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        // Initialize storage (creates directory if not exists)
        storageService.init();

        String fileUrl = storageService.store(file);

        return ResponseEntity.ok(Map.of("url", fileUrl));
    }
}
