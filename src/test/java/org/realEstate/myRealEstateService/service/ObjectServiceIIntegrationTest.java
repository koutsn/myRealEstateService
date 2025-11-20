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

    private static final String UPLOAD_DIR = "images";

    private final String FILENAME = "file1.jpeg";

    private final String DESCRIPTION = "living room";

    private final InputStream stream1 = new ByteArrayInputStream("testFile".getBytes());

    MultipartFile file = new MockMultipartFile(FILENAME,  // name parameter (form field name)
            FILENAME,             // original filename
            "image",            // content type
            "test date".getBytes()); // file content as byte[];

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
        UUID id = UUID.randomUUID();
        filesDto.setObjectId(id);
        filesDto.setFile(file);
        filesDto.setDescription(DESCRIPTION);

        // RUN
        objectService.uploadImages(id, filesDto);

        Optional<List<ObjectFilesEntity>> fileEntity = repository.findByObjectId(id);
        assertNotNull(fileEntity);
        assertEquals(1, fileEntity.get().size());
        assertNotNull(fileEntity.get().getFirst().getId());
        assertEquals(id, fileEntity.get().getFirst().getObjectId());
        assertEquals(FILENAME, fileEntity.get().getFirst().getFileName());
    }

    @Test
    void uploadImages_no_objectId() {
        UUID id = UUID.randomUUID();
        filesDto.setObjectId(id);
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
        UUID id = UUID.randomUUID();
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    objectService.uploadImages(id, null);
                }
        );
        assertEquals("Files object is null", exception.getMessage());
    }

    @Test
    void uploadImages_no_names() {
        UUID id = UUID.randomUUID();
        filesDto.setObjectId(id);
        filesDto.setFile(file);
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    objectService.uploadImages(id, filesDto);
                }
        );
        assertEquals("No file description is given in names", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void uploadImages_error_handling() {
        UUID id = UUID.randomUUID();
        filesDto.setObjectId(id);
        filesDto.setFile(file);
        filesDto.setDescription(DESCRIPTION);

        InputStream stream1 = new ByteArrayInputStream("testFile".getBytes());
        doReturn(FILENAME).when(fileMock).getFilename(anyString());

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {

            // Mock createDirectories
            mockedFiles.when(() -> Files.createDirectories(Paths.get(UPLOAD_DIR)))
                    .thenReturn(Paths.get(UPLOAD_DIR));

            // Mock file copies
            mockedFiles.when(() ->
                    Files.copy(eq(stream1),
                            eq(Paths.get(UPLOAD_DIR, "0_test1.jpg")),
                            eq(REPLACE_EXISTING))
            ).thenReturn(100L);

            doThrow(new RuntimeException("Validation failed"))
                    .when(fileMock)
                    .validateFile(any(MultipartFile.class));

            // RUN
            Exception exception = assertThrows(
                    CustomException.class,
                    () -> {
                        objectService.uploadImages(id, filesDto);
                    }
            );
            assertEquals("Could not upload file: " + FILENAME + " ,Error: Validation failed", exception.getMessage());

            Optional<List<ObjectFilesEntity>> fileEntity = repository.findByObjectId(id);
            assertNotNull(fileEntity);
            assertEquals(0, fileEntity.get().size());
        }
    }
}