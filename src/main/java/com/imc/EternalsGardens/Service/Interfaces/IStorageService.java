package com.imc.EternalsGardens.Service.Interfaces;

import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;

public interface IStorageService {
    void init();

    String store(MultipartFile file);

    String store(MultipartFile file, String subFolder);

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
