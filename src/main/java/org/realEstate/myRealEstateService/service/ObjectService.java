package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
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

    private void saveFileInDB(UUID id, String name, String fileName) {
        ObjectFilesEntity fileEntity = mapper.toEntity(id, name, fileName);
        repository.save(fileEntity);
    }

    private void deleteFileFromDbAndFS(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(UPLOAD_DIR, fileName));
            repository.findByFileName(fileName).ifPresent(repository::delete);
        } catch (IOException e) {
            System.err.println("Could not erase the file data.Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getNamee(String[] names, int counter) {
        String name = null;
        if (names != null && counter >= 0 && counter < names.length) {
            name = (names[counter] != null)
                    ? names[counter]
                    : null;
        }
        return name;
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
                    fileName = System.currentTimeMillis() + counter + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(UPLOAD_DIR, fileName);
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    fileNames.add(fileName);
                    // save in
                    String name = getNamee(names, counter);
                    saveFileInDB(id, name, fileName);
                }
                counter++;
            }
        } catch (Exception e) {
            deleteFileFromDbAndFS(fileName);
            throw new CustomException("Could not upload file: " + fileName + ",Error:" + e.getMessage());
        }
    }
}
