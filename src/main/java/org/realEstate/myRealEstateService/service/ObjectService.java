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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private void saveFileInDB(ObjecFilesDto uploadInfo) {
        ObjectFilesEntity fileEntity = mapper.toEntity(uploadInfo);
        repository.save(fileEntity);
    }

    private void deleteFileFromDbAndFS(String fileName) {
        try {
            if (fileName != null) {
                file.deleteFile(Paths.get(UPLOAD_DIR, fileName));
                repository.findByFileName(fileName).ifPresent(repository::delete);
            }
        } catch (IOException e) {
            System.err.println("Could not erase the file data.Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void uploadChecks(UUID objId, ObjecFilesDto file) throws CustomException {
        if (objId == null)
            throw new CustomException("Object id is null");

        if (file == null)
            throw new CustomException("Files object is null");


        if (file.getDescription() == null)
            throw new CustomException("No file description is given in names");

    }

    public void uploadImages(UUID objId, ObjecFilesDto uploadInfo) throws CustomException {
        uploadChecks(objId, uploadInfo);

        try {
            uploadInfo.setObjectId(objId);
            uploadInfo.setFilename(this.file.getFilename(this.file.getFileExt(uploadInfo.getFile())));
            uploadInfo.setUrl(URL);
            this.file.validateFile(uploadInfo.getFile());
            Files.createDirectories(Path.of(UPLOAD_DIR));
            Path filePath = Paths.get(UPLOAD_DIR, uploadInfo.getFilename());
            InputStream inputstream = uploadInfo.getFile().getInputStream();
            this.file.copyFile(inputstream, filePath);
            saveFileInDB(uploadInfo);
        } catch (Exception e) {
            deleteFileFromDbAndFS(uploadInfo.getFilename());
            throw new CustomException("Could not upload file: " + uploadInfo.getFile().getOriginalFilename() + " ,Error: " + e.getMessage());
        }
    }

}
