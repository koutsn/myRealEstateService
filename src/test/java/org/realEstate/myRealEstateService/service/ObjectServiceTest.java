package org.realEstate.myRealEstateService.service;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.realEstate.myRealEstateService.dto.AddressDto;
import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.dto.ObjectDto;
import org.realEstate.myRealEstateService.entity.ObjectEntity;
import org.realEstate.myRealEstateService.repository.ObjectRepository;
import org.realEstate.myRealEstateService.utils.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.realEstate.myRealEstateService.Enum.ObjectStatus.SOLD;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ObjectServiceTest {

    @Autowired
    private ObjectService objectService;

    @Autowired
    ObjectFileService objectFileService;

    @MockitoBean
    private File fileMock;

    @Autowired
    private ObjectRepository repository;

    private final ObjecFilesDto filesDto = new ObjecFilesDto();

    private OffsetDateTime creationDate = OffsetDateTime.now();

    private String filename;

    MultipartFile file = new MockMultipartFile("filename1-jpg",  // name parameter (form field name)
            "originalFilename",             // original filename
            "image",            // content type
            "test data2".getBytes());

    @SneakyThrows
    public void uploadFile(UUID objectId) {
        filesDto.setObjectId(objectId);
        filesDto.setFile(file);
        filesDto.setDescription("Test description");
        objectFileService.uploadImages(objectId, filesDto);
    }

    @BeforeEach
    @SneakyThrows
    void setup() {
        repository.deleteAll();
        filename = UUID.randomUUID().toString() + ".jpg";
        doReturn("jpeg").when(fileMock).getFileExt(any());
        doReturn(filename).when(fileMock).getFilename(anyString());
        doNothing().when(fileMock).validateFile(any(MultipartFile.class));
        doNothing().when(fileMock).createDir(anyString());
        doNothing().when(fileMock).copyFile(any(), any());
        doNothing().when(fileMock).deleteFile(any());
    }

    void createObjectIntern() {
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("street 2A");
        addressDto.setZipCode("1234");
        addressDto.setCity("city");
        addressDto.setCountry("country");

        ObjectDto objectDto = new ObjectDto();
        objectDto.setDescription("description");
        objectDto.setAddress(addressDto);
        objectDto.setCreatedAt(creationDate);
        objectDto.setPrice(120.50F);
        objectDto.setStatus(SOLD);
        objectDto.setHasGarage(false);
        objectDto.setNumberRooms(3);
        objectDto.setNumberBathrooms(1);
        objectDto.setUpdatedAt(creationDate);
        objectService.createObject(objectDto);
    }

    @Test
    void createObject() {

        createObjectIntern();

        List<ObjectEntity> properties = repository.findAll();
        assertNotNull(properties);
        assertEquals(1, properties.size());
        assertEquals("description", properties.getFirst().getDescription());
        assertNotNull(properties.getFirst().getId());
        assertEquals(creationDate.toInstant().truncatedTo(ChronoUnit.SECONDS), properties.getFirst().getCreatedAt().truncatedTo(ChronoUnit.SECONDS).toInstant());
        assertEquals(creationDate.toInstant().truncatedTo(ChronoUnit.SECONDS), properties.getFirst().getUpdatedAt().truncatedTo(ChronoUnit.SECONDS).toInstant());
        assertEquals(120.50F, properties.getFirst().getPrice());
        assertEquals(SOLD, properties.getFirst().getStatus());
        assertFalse(properties.getFirst().isHasGarage());
        assertEquals(3, properties.getFirst().getNumberRooms());
        assertEquals(1, properties.getFirst().getNumberBathrooms());
        assertNotNull(properties.getFirst().getAddress().getId());
        assertEquals("street 2A", properties.getFirst().getAddress().getStreet());
        assertEquals("1234", properties.getFirst().getAddress().getZipCode());
        assertEquals("city", properties.getFirst().getAddress().getCity());
        assertEquals("country", properties.getFirst().getAddress().getCountry());
    }

    @Test
    void getObjectById() {
        createObjectIntern();
        List<ObjectEntity> propertiesEntity = repository.findAll();
        assertNotNull(propertiesEntity);
        assertEquals(1, propertiesEntity.size());

        ObjectDto properties = objectService.getObjectById(propertiesEntity.getFirst().getId());
        assertNotNull(propertiesEntity);
        assertEquals(1, propertiesEntity.size());
        assertEquals("description", properties.getDescription());
        assertNotNull(properties.getId());
        assertEquals(creationDate.toInstant().truncatedTo(ChronoUnit.SECONDS), properties.getCreatedAt().truncatedTo(ChronoUnit.SECONDS).toInstant());
        assertEquals(creationDate.toInstant().truncatedTo(ChronoUnit.SECONDS), properties.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS).toInstant());
        assertEquals(120.50F, properties.getPrice());
        assertEquals(SOLD, properties.getStatus());
        assertFalse(properties.isHasGarage());
        assertEquals(3, properties.getNumberRooms());
        assertEquals(1, properties.getNumberBathrooms());
        assertNotNull(properties.getAddress().getId());
        assertEquals("street 2A", properties.getAddress().getStreet());
        assertEquals("1234", properties.getAddress().getZipCode());
        assertEquals("city", properties.getAddress().getCity());
        assertEquals("country", properties.getAddress().getCountry());
    }

    @Test
    void deleteObject() {
        createObjectIntern();
        List<ObjectEntity> propertiesEntity = repository.findAll();
        assertNotNull(propertiesEntity);
        assertEquals(1, propertiesEntity.size());

        uploadFile(propertiesEntity.getFirst().getId());

        List<ObjecFilesDto> files = objectFileService.getImagesForObject(propertiesEntity.getFirst().getId());
        assertEquals(1, files.size());
        
        objectService.deleteObject(propertiesEntity.getFirst().getId());
        List<ObjectEntity> propertiesEntityAfterDelete = repository.findAll();
        List<ObjecFilesDto> filesAfterDelete = objectFileService.getImagesForObject(propertiesEntity.getFirst().getId());

        assertEquals(0, propertiesEntityAfterDelete.size());
        assertEquals(0, filesAfterDelete.size());
    }

    @Test
    void getAllObjects() {
        createObjectIntern();
        createObjectIntern();
        createObjectIntern();
        List<ObjectDto> properties = objectService.getAllObjects();
        assertEquals(3, properties.size());
    }
}