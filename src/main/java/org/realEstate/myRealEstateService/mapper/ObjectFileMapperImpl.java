package org.realEstate.myRealEstateService.mapper;

import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObjectFileMapperImpl implements ObjectFileMapper {
    @Override
    public ObjectFilesEntity toEntity(UUID id, ObjecFilesDto uploadInfo) {
        ObjectFilesEntity objectFilesEntity = new ObjectFilesEntity();
        if (uploadInfo != null) {
            objectFilesEntity.setObjectId(id);
            objectFilesEntity.setDescription(uploadInfo.getDescription());
            objectFilesEntity.setFileName(uploadInfo.getFilename());
            if (uploadInfo.getFile().getOriginalFilename() != null) {
                objectFilesEntity.setOriginalFilename(uploadInfo.getFile().getOriginalFilename());
            }
            objectFilesEntity.setUrl(uploadInfo.getUrl());
        }
        return objectFilesEntity;
    }
}
