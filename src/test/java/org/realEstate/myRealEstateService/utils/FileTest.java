package org.realEstate.myRealEstateService.utils;

import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.realEstate.myRealEstateService.exception.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class FileTest {

    @InjectMocks
    private File fileValidator;

    @Mock
    private MultipartFile multipartFile;

    @Mock
    private InputStream inputStream;

    @Test
    void validateFile_Success() throws Exception {
        when(multipartFile.getOriginalFilename()).thenReturn("test.png");
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));
        when(multipartFile.getSize()).thenReturn(1024L);

        // Tika should detect image mime
        Tika tika = Mockito.mock(Tika.class);
        try (MockedConstruction<Tika> mocked = mockConstruction(Tika.class,
                (mock, context) -> when(mock.detect(any(InputStream.class))).thenReturn("image/png"))) {

            assertDoesNotThrow(() -> fileValidator.validateFile(multipartFile));
        }
    }

    // ------- UNSUPPORTED EXTENSION -------
    @Test
    void validateFile_UnsupportedExtension() throws Exception {
        when(multipartFile.getOriginalFilename()).thenReturn("test.exe");

        CustomException e = assertThrows(CustomException.class,
                () -> fileValidator.validateFile(multipartFile));

        assertEquals("Unsupported file extension", e.getMessage());
    }

    // ------- UNSUPPORTED MIME TYPE -------
    @Test
    void validateFile_UnsupportedMime() throws Exception {
        when(multipartFile.getOriginalFilename()).thenReturn("test.png");
        when(multipartFile.getContentType()).thenReturn("application/x-msdownload");

        CustomException e = assertThrows(CustomException.class,
                () -> fileValidator.validateFile(multipartFile));

        assertEquals("Unsupported MIME type", e.getMessage());
    }

    // ------- INVALID FILE SIGNATURE -------
    @Test
    void validateFile_InvalidSignature() throws Exception {
        when(multipartFile.getOriginalFilename()).thenReturn("test.png");
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));

        try (MockedConstruction<Tika> mocked = mockConstruction(Tika.class,
                (mock, context) -> when(mock.detect(any(InputStream.class))).thenReturn("text/plain"))) {

            CustomException e = assertThrows(CustomException.class,
                    () -> fileValidator.validateFile(multipartFile));

            assertEquals("File content does not match an image", e.getMessage());
        }
    }

    // ------- FILE TOO LARGE -------
    @Test
    void validateFile_FileTooLarge() throws Exception {
        when(multipartFile.getOriginalFilename()).thenReturn("test.png");
        when(multipartFile.getContentType()).thenReturn("image/png");
        when(multipartFile.getSize()).thenReturn(6L * 1024L * 1024L); // 6MB
        when(multipartFile.getInputStream()).thenReturn(new ByteArrayInputStream("data".getBytes()));

        try (MockedConstruction<Tika> mocked = mockConstruction(Tika.class,
                (mock, context) -> when(mock.detect(any(InputStream.class))).thenReturn("image/jpeg"))) {

            CustomException e = assertThrows(CustomException.class,
                    () -> fileValidator.validateFile(multipartFile));

            assertEquals("File too large", e.getMessage());
        }

    }
}
