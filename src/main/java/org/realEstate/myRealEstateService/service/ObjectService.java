package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.mapper.ObjectFileMapper;
import org.realEstate.myRealEstateService.repository.ObjectFilesRepository;
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

    private final ObjectFileMapper mapper;

    private final ObjectFilesRepository repository;

    public void saveFileInDB(UUID id, String name, String fileName) {
        ObjectFilesEntity fileEntity = mapper.toEntity(id, name, fileName);
        repository.save(fileEntity);
    }

    public void uploadImages(UUID id, ObjecFilesDto files) throws CustomException {
        List<String> fileNames = new ArrayList<>();
        String fileName = "";
        try {
            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String[] names = files.getName();
            int counter = 0;

            for (MultipartFile file : files.getFile()) {
                if (!file.isEmpty()) {
                    // write in file-system
                    fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR, fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    fileNames.add(fileName);
                    // save in DB
                    if (names != null && names.length >= counter) {
                        saveFileInDB(id, names[counter], fileName);
                    }
                    counter++;
                }
            }
        } catch (IOException e) {
            throw new CustomException("Could not upload file: " + fileName + ",Error:" + e.getMessage());
        }
    }
}
