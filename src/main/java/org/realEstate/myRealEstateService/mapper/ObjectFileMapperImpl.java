package org.realEstate.myRealEstateService.mapper;

import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObjectFileMapperImpl implements ObjectFileMapper {
    @Override
    public ObjectFilesEntity toEntity(UUID id, String name, String fileName) {
        ObjectFilesEntity objectFilesEntity = new ObjectFilesEntity();
        objectFilesEntity.setObjectId(id);
        objectFilesEntity.setName(name);
        objectFilesEntity.setFileName(fileName);
        return objectFilesEntity;
    }
}
