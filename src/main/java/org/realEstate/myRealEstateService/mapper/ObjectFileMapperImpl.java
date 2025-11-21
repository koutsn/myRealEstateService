package org.realEstate.myRealEstateService.mapper;

import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObjectFileMapperImpl implements ObjectFileMapper {
    @Override
    public ObjectFilesEntity toEntity(ObjecFilesDto uploadInfo) {
        ObjectFilesEntity objectFilesEntity = new ObjectFilesEntity();
        if (uploadInfo != null) {
            objectFilesEntity.setObjectId(uploadInfo.getObjectId());
            objectFilesEntity.setDescription(uploadInfo.getDescription());
            objectFilesEntity.setFileName(uploadInfo.getFilename());
            objectFilesEntity.setOriginalFilename(uploadInfo.getFile().getOriginalFilename());
            objectFilesEntity.setUrl(uploadInfo.getUrl());
        }
        return objectFilesEntity;
    }
}
