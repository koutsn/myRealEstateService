package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObjectService {

    private static final String UPLOAD_DIR = "uploads/";

    public void uploadImages(String id, ObjecFilesDto files) throws CustomException {
        List<String> fileNames = new ArrayList<>();
        String fileName = "";
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            for (MultipartFile file : files.getFile()) {
                if (!file.isEmpty()) {
                    fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR, fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    fileNames.add(fileName);
                }
            }
        } catch (IOException e) {
           throw new CustomException("Could not upload file: " + fileName +  ",Error:" + e.getMessage());
        }
    }
}
