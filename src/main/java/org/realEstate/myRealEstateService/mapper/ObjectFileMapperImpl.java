package org.realEstate.myRealEstateService.mapper;

import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObjectFileMapperImpl implements ObjectFileMapper {
    @Override
    public ObjectFilesEntity toEntity(UUID id, String name, String fileName, String originalFilename, String url) {
        ObjectFilesEntity objectFilesEntity = new ObjectFilesEntity();
        objectFilesEntity.setObjectId(id);
        objectFilesEntity.setDescription(name);
        objectFilesEntity.setFileName(fileName);
        objectFilesEntity.setOriginal_filename(originalFilename);
        objectFilesEntity.setUrl(url);
        return objectFilesEntity;
    }
}
