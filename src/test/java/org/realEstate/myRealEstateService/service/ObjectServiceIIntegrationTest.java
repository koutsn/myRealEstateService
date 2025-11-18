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

    private final String filename = "file1";

    private final InputStream stream1 = new ByteArrayInputStream("testFile".getBytes());

    MultipartFile file = new MockMultipartFile(filename,  // name parameter (form field name)
            filename,             // original filename
            "image",            // content type
            "test date".getBytes()); // file content as byte[];

    MultipartFile file2 = new MockMultipartFile(filename + "2",  // name parameter (form field name)
            filename + "2",             // original filename
            "image",            // content type
            "test2 date".getBytes());

    MultipartFile[] files = new MultipartFile[]{file};
    String[] names = {"Living room",};

    @BeforeEach
    @SneakyThrows
    void Setup() {
        doReturn(filename).when(fileMock).getFilename(anyString());
        doNothing().when(fileMock).validateFile(any(MultipartFile.class));
        doNothing().when(fileMock).createDir(anyString());
        doNothing().when(fileMock).copyFile(any(),any());
        doNothing().when(fileMock).deleteFile(any());
    }

    @Test
    @SneakyThrows
    void uploadImages_success() {
        UUID id = UUID.randomUUID();
        filesDto.setObjectId(id);
        filesDto.setFile(files);
        filesDto.setName(names);

        // RUN
        objectService.uploadImages(id, filesDto);

        Optional<List<ObjectFilesEntity>> fileEntity = repository.findByObjectId(id);
        assertNotNull(fileEntity);
        assertEquals(1, fileEntity.get().size());
        assertNotNull(fileEntity.get().getFirst().getId());
        assertEquals(id, fileEntity.get().getFirst().getObjectId());
        assertEquals(filename, fileEntity.get().getFirst().getFileName());
        assertEquals(names[0], fileEntity.get().getFirst().getName());
    }

    @Test
    void uploadImages_no_objectId() {
        UUID id = UUID.randomUUID();
        filesDto.setObjectId(id);
        filesDto.setFile(files);
        filesDto.setName(names);
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
        filesDto.setFile(files);
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    objectService.uploadImages(id, filesDto);
                }
        );
        assertEquals("No file description is given in names", exception.getMessage());
    }

    @Test
    void uploadImages_files_names_inconsistency() {
        MultipartFile[] filesWith2Files = new MultipartFile[]{file, file2};
        UUID id = UUID.randomUUID();
        filesDto.setObjectId(id);
        filesDto.setFile(filesWith2Files);
        filesDto.setName(names);
        Exception exception = assertThrows(
                CustomException.class,
                () -> {
                    objectService.uploadImages(id, filesDto);
                }
        );
        assertEquals("Inconsistency in files and name parameters", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void uploadImages_error_handling() {
        UUID id = UUID.randomUUID();
        filesDto.setObjectId(id);
        filesDto.setFile(files);
        filesDto.setName(names);

        InputStream stream1 = new ByteArrayInputStream("testFile".getBytes());
        doReturn(filename).when(fileMock).getFilename(anyString());

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
            assertEquals("Could not upload file: ,Error:Validation failed", exception.getMessage());

            Optional<List<ObjectFilesEntity>> fileEntity = repository.findByObjectId(id);
            assertNotNull(fileEntity);
            assertEquals(0, fileEntity.get().size());
        }
    }
}