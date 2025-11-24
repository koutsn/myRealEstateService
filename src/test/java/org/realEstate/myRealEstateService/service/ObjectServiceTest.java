package org.realEstate.myRealEstateService.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.realEstate.myRealEstateService.dto.AddressDto;
import org.realEstate.myRealEstateService.dto.ObjectDto;
import org.realEstate.myRealEstateService.entity.ObjectEntity;
import org.realEstate.myRealEstateService.repository.ObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.realEstate.myRealEstateService.Enum.ObjectStatus.SOLD;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ObjectServiceTest {

    @Autowired
    private ObjectService objectService;

    @Autowired
    private ObjectRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void createObject() {

        OffsetDateTime creationDate = OffsetDateTime.now();

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet("street");
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


    }

}