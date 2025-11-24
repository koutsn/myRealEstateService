package org.realEstate.myRealEstateService.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ObjectFileServiceIIntegrationTest {
    @Autowired
    ObjectFileService objectFileService;

    @Autowired
    private ObjectFilesRepository repository;

    @MockitoBean
    private File fileMock;

    private final ObjecFilesDto filesDto = new ObjecFilesDto();

    private final UUID objectId = UUID.randomUUID();

    private final String UPLOAD_DIR = "images";

    private final String ORIGINAL_FILENAME = "file1.jpeg";    private final String ORIGINAL_FILENAME2 = "file2.jpeg";


    private final String FILENAME = UUID.randomUUID().toString() + ".jpg";

    private final String DESCRIPTION = "living room";

    private final InputStream stream1 = new ByteArrayInputStream("testFile".getBytes());

    MultipartFile file = new MockMultipartFile(FILENAME,  // name parameter (form field name)
            ORIGINAL_FILENAME,             // original filename
            "image",            // content type
            "test data".getBytes()); // file content as byte[];

    MultipartFile file2 = new MockMultipartFile(FILENAME,  // name parameter (form field name)
            ORIGINAL_FILENAME2,             // original filename
            "image",            // content type
            "test data2".getBytes());

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

        objectFileService.uploadImages(objectId, filesDto);

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
                    objectFileService.uploadImages(null, filesDto);
                }
        );
        assertEquals("Object id is null", exception.getMessage());
    }

    @Test
    void uploadImages_files_is_null() {
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    objectFileService.uploadImages(objectId, null);
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

        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    objectFileService.uploadImages(objectId, filesDto);
                }
        );
        assertEquals("Could not upload file: " + ORIGINAL_FILENAME + " ,Error: Validation failed", exception.getMessage());

        Optional<List<ObjectFilesEntity>> fileEntity = repository.findByObjectId(objectId);
        assertNotNull(fileEntity);
        assertEquals(0, fileEntity.get().size());

    }

    @Test
    @SneakyThrows
    void getObjectImages() {
        filesDto.setObjectId(objectId);
        filesDto.setFile(file);
        filesDto.setDescription("First file");
        objectFileService.uploadImages(objectId, filesDto);

        filesDto.setObjectId(objectId);
        filesDto.setFile(file2);
        filesDto.setDescription("Second File");
        objectFileService.uploadImages(objectId, filesDto);

        List<ObjecFilesDto> objectImages = objectFileService.getImagesForObject(objectId);

        assertEquals(2, objectImages.size());
        assertNotNull(objectImages.get(0).getId());
        assertEquals(objectId, objectImages.get(0).getObjectId());
        assertNull(objectImages.get(0).getFile());
        assertThat(objectImages.get(0).getUrl()).matches("http://localhost:8080/images/\\S+\\.(jpg|jpeg|png|gif|bmp|svg|webp|tiff)");
        assertEquals("First file", objectImages.get(0).getDescription());
        assertNotNull(objectImages.get(0).getFilename());
        assertEquals("file1.jpeg", objectImages.get(0).getOriginalFilename());

        assertNotNull(objectImages.get(1).getId());
        assertEquals(objectId, objectImages.get(1).getObjectId());
        assertNull(objectImages.get(1).getFile());
        assertThat(objectImages.get(1).getUrl()).matches("http://localhost:8080/images/\\S+\\.(jpg|jpeg|png|gif|bmp|svg|webp|tiff)");
        assertEquals("Second File", objectImages.get(1).getDescription());
        assertNotNull(objectImages.get(1).getFilename());
        assertEquals("file2.jpeg", objectImages.get(1).getOriginalFilename());
    }

    @Test
    @SneakyThrows
    void getImage() {
        filesDto.setObjectId(objectId);
        filesDto.setFile(file);
        filesDto.setDescription("First file");
        objectFileService.uploadImages(objectId, filesDto);

        List<ObjecFilesDto> objectImages = objectFileService.getImagesForObject(objectId);
        assertEquals(1, objectImages.size());

        ObjecFilesDto image = objectFileService.getImageById(objectImages.getFirst().getId());

        assertNotNull(image);
        assertNotNull(image.getId());
        assertEquals(objectId, image.getObjectId());
        assertNull(image.getFile());
        assertThat(image.getUrl()).matches("http://localhost:8080/images/\\S+\\.(jpg|jpeg|png|gif|bmp|svg|webp|tiff)");
        assertEquals("First file", image.getDescription());
        assertNotNull(image.getFilename());
        assertEquals("file1.jpeg", image.getOriginalFilename());
    }
}