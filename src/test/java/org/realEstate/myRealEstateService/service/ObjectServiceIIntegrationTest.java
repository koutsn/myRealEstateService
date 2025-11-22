package org.realEstate.myRealEstateService.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.realEstate.myRealEstateService.repository.ObjectFilesRepository;
import org.realEstate.myRealEstateService.utils.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ObjectServiceIIntegrationTest {
    @Autowired
    ObjectService objectService;

    @Autowired
    private ObjectFilesRepository repository;

    @MockitoBean
    private File fileMock;

    private final ObjecFilesDto filesDto = new ObjecFilesDto();

    private final UUID objectId = UUID.randomUUID();

    private final String UPLOAD_DIR = "images";

    private final String ORIGINAL_FILENAME = "file1.jpeg";

    private final String FILENAME = UUID.randomUUID().toString();

    private final String DESCRIPTION = "living room";

    private final InputStream stream1 = new ByteArrayInputStream("testFile".getBytes());

    MultipartFile file = new MockMultipartFile(FILENAME,  // name parameter (form field name)
            ORIGINAL_FILENAME,             // original filename
            "image",            // content type
            "test date".getBytes()); // file content as byte[];
    private MultipartFile MultipartFile;

    @BeforeEach
    @SneakyThrows
    void Setup() {
        doReturn("jpeg").when(fileMock).getFileExt(any());
        doReturn(FILENAME).when(fileMock).getFilename(anyString());
        doNothing().when(fileMock).validateFile(any(MultipartFile.class));
        doNothing().when(fileMock).createDir(anyString());
        doNothing().when(fileMock).copyFile(any(), any());
        doNothing().when(fileMock).deleteFile(any());
    }

    @Test
    @SneakyThrows
    void uploadImages_success() {
        filesDto.setObjectId(objectId);
        filesDto.setFile(file);
        filesDto.setDescription(DESCRIPTION);

        // RUN
        objectService.uploadImages(objectId, filesDto);

        Optional<List<ObjectFilesEntity>> fileEntity = repository.findByObjectId(objectId);
        assertNotNull(fileEntity);
        assertEquals(1, fileEntity.get().size());
        assertNotNull(fileEntity.get().getFirst().getId());
        assertEquals(objectId, fileEntity.get().getFirst().getObjectId());
        assertEquals(FILENAME, fileEntity.get().getFirst().getFileName());
        assertEquals(ORIGINAL_FILENAME, fileEntity.get().getFirst().getOriginalFilename());
    }

    @Test
    void uploadImages_no_objectId() {
        filesDto.setObjectId(objectId);
        filesDto.setFile(file);
        filesDto.setDescription(DESCRIPTION);
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    objectService.uploadImages(null, filesDto);
                }
        );
        assertEquals("Object id is null", exception.getMessage());
    }

    @Test
    void uploadImages_files_is_null() {
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    objectService.uploadImages(objectId, null);
                }
        );
        assertEquals("Files object is null", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void uploadImages_error_handling() {
        filesDto.setObjectId(objectId);
        filesDto.setFile(file);
        filesDto.setDescription(DESCRIPTION);

        InputStream stream1 = new ByteArrayInputStream("testFile".getBytes());
        doReturn(FILENAME).when(fileMock).getFilename(anyString());
        
        doThrow(new RuntimeException("Validation failed"))
                .when(fileMock)
                .validateFile(any(MultipartFile.class));

        // RUN
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    objectService.uploadImages(objectId, filesDto);
                }
        );
        assertEquals("Could not upload file: " + ORIGINAL_FILENAME + " ,Error: Validation failed", exception.getMessage());

        Optional<List<ObjectFilesEntity>> fileEntity = repository.findByObjectId(objectId);
        assertNotNull(fileEntity);
        assertEquals(0, fileEntity.get().size());

    }
}