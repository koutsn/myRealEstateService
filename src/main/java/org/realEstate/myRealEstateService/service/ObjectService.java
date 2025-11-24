package org.realEstate.myRealEstateService.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.ObjectDto;
import org.realEstate.myRealEstateService.entity.ObjectEntity;
import org.realEstate.myRealEstateService.mapper.ObjectMapper;
import org.realEstate.myRealEstateService.repository.ObjectRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObjectService {
    private final ObjectMapper objectMapper;
    private final ObjectRepository objectRepository;

    @Transactional
    public void createObject(ObjectDto property)
    {
        ObjectEntity objectEntity = objectMapper.toEntity(property);
        objectRepository.saveAndFlush(objectEntity);
    }
}
