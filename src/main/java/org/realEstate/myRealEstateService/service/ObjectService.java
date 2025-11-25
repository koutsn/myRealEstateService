package org.realEstate.myRealEstateService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.dto.ObjectDto;
import org.realEstate.myRealEstateService.entity.ObjectEntity;
import org.realEstate.myRealEstateService.mapper.ObjectMapper;
import org.realEstate.myRealEstateService.repository.ObjectFilesRepository;
import org.realEstate.myRealEstateService.repository.ObjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ObjectService {
    private final ObjectMapper objectMapper;
    private final ObjectRepository objectRepository;
    private final ObjectFilesRepository objectFilesRepository;
    private final ObjectFileService objectFileService;

    @Transactional
    public void createObject(ObjectDto property) {
        ObjectEntity objectEntity = objectMapper.toEntity(property);
        objectRepository.saveAndFlush(objectEntity);
    }

    public ObjectDto getObjectById(UUID id) {
        ObjectEntity objectEntity = objectRepository.findById(id).orElse(null);
        return objectMapper.toDto(objectEntity);

    }

    public List<ObjectDto> getAllObjects() {
        List<ObjectEntity> objectEntity = objectRepository.findAll();
        return objectEntity.stream().map(objectMapper::toDto).collect(Collectors.toList());
    }

    public void deleteObject(UUID id) {
        ObjectEntity objectEntity = objectRepository.findById(id).orElse(null);
        if (objectEntity != null) {

            List<ObjecFilesDto> objectFies = objectFileService.getImagesForObject(objectEntity.getId());

            objectFies.forEach(file -> objectFilesRepository.findById(file.getId()).ifPresent(objectFilesRepository::delete));

            objectRepository.delete(objectEntity);
        }
    }

}
