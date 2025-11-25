package org.realEstate.myRealEstateService.service;

import jakarta.transaction.Transactional;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObjectFileService {

    @Value("${image.path}")
    private String UPLOAD_DIR;

    @Value("${image.url}")
    private String URL;

    private final ObjectFileMapper objectFileMapper;

    private final ObjectFilesRepository objectFilesRepository;

    //private final ObjectService objectService;

    private final File file;

    @Transactional
    private void saveFileInDB(ObjecFilesDto uploadInfo) {
        ObjectFilesEntity fileEntity = objectFileMapper.toEntity(uploadInfo);
        objectFilesRepository.saveAndFlush(fileEntity);
    }

    @Transactional
    private void deleteFileFromDbAndFS(String fileName) {
        try {
            if (fileName != null) {
                file.deleteFile(Paths.get(UPLOAD_DIR, fileName));
                objectFilesRepository.findByFileName(fileName).ifPresent(objectFilesRepository::delete);
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

    @Transactional
    public void uploadImages(UUID objId, ObjecFilesDto uploadInfo) throws CustomException {
        uploadChecks(objId, uploadInfo);

        try {
            uploadInfo.setObjectId(objId);
            String fileName = this.file.getFilename(this.file.getFileExt(uploadInfo.getFile()));
            uploadInfo.setFilename(fileName);
            uploadInfo.setUrl(URL + fileName);
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

    public List<ObjecFilesDto> getImagesForObject(UUID objId) {
        List<ObjecFilesDto> imagesDto = new ArrayList<>();
        List<ObjectFilesEntity> images = objectFilesRepository.findByObjectId(objId).orElse(null);
        if (images != null) {
            imagesDto = images.stream().map(objectFileMapper::toDto).collect(Collectors.toList());
        }
        return imagesDto;
    }

    public ObjecFilesDto getImageById(UUID imgID) {
        ObjectFilesEntity image = objectFilesRepository.findById(imgID).orElse(null);
        return objectFileMapper.toDto(image);
    }

    public void deleteImageById(UUID id) {
        ObjecFilesDto image = getImageById(id);
        if (image != null) {
            deleteFileFromDbAndFS(image.getFilename());
        }
    }

    public void deleteImageByObjectId(UUID objId) {
        List<ObjectFilesEntity> images = objectFilesRepository.findByObjectId(objId).orElse(null);
        if (images != null && !images.isEmpty()) {
            images.forEach(image -> deleteFileFromDbAndFS(image.getFileName()));
        }
    }
}
