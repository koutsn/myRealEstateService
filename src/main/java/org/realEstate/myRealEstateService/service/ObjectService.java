package org.realEstate.myRealEstateService.service;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.mapper.ObjectFileMapper;
import org.realEstate.myRealEstateService.repository.ObjectFilesRepository;
import org.realEstate.myRealEstateService.utils.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ObjectService {

    @Value("${image.path}")
    private String UPLOAD_DIR;

    @Value("${image.url}")
    private String URL;

    private final ObjectFileMapper mapper;

    private final ObjectFilesRepository repository;

    private final File file;

    private void saveFileInDB(UUID id, String name, String fileName, String originalFilename, String url) {
        ObjectFilesEntity fileEntity = mapper.toEntity(id, name, fileName, originalFilename, url);
        repository.save(fileEntity);
    }

    private void deleteFileFromDbAndFS(String fileName) {
        try {
            if (fileName != null) {
                file.deleteFile(Paths.get(UPLOAD_DIR, fileName));
                ;
                repository.findByFileName(fileName).ifPresent(repository::delete);
            }
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

    private void uploadChecks(UUID id, ObjecFilesDto files) throws CustomException {
        if (id == null)
            throw new CustomException("Object id is null");

        if (files == null)
            throw new CustomException("Files object is null");

        if (files.getName() == null)
            throw new CustomException("No file description is given in names");

        if (files.getName().length != files.getFile().length)
            throw new CustomException("Inconsistency in files and name parameters");
    }

    public void uploadImages(UUID id, ObjecFilesDto files) throws CustomException {

        uploadChecks(id, files);

        List<String> fileNames = new ArrayList<>();
        String fileName = "";
        try {
            file.createDir(UPLOAD_DIR);
            String[] names = files.getName();
            int counter = 0;
            for (MultipartFile file : files.getFile()) {
                if (!file.isEmpty()) {
                    // Validate File
                    this.file.validateFile(file);
                    // write in file-system
                    fileName = this.file.getFilename(this.file.getFileExt(file));
                    Path filePath = Paths.get(UPLOAD_DIR, fileName);
                    InputStream dd = file.getInputStream();
                    this.file.copyFile(file.getInputStream(), filePath);
                    fileNames.add(fileName);
                    // save in
                    String name = getNamee(names, counter);
                    saveFileInDB(id, name, fileName, file.getOriginalFilename(), URL);
                }
                counter++;
            }
        } catch (Exception e) {
            deleteFileFromDbAndFS(fileName);
            throw new CustomException("Could not upload file: " + fileName + ",Error:" + e.getMessage());
        }
    }
}
