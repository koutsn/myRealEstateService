package org.realEstate.myRealEstateService.mapper;

import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObjectFileMapperImpl implements ObjectFileMapper {
    @Override
    public ObjectFilesEntity toEntity(ObjecFilesDto objecFilesDto) {
        ObjectFilesEntity objectFilesEntity = new ObjectFilesEntity();
        if (objecFilesDto != null) {
            objectFilesEntity.setObjectId(objecFilesDto.getObjectId() != null ? objecFilesDto.getObjectId() : null);
            objectFilesEntity.setDescription(objecFilesDto.getDescription() != null ? objecFilesDto.getDescription() : null);
            objectFilesEntity.setFileName(objecFilesDto.getFilename() != null ? objecFilesDto.getFilename() : null);
            objectFilesEntity.setOriginalFilename(objecFilesDto.getFile() != null && objecFilesDto.getFile().getOriginalFilename() != null ? objecFilesDto.getFile().getOriginalFilename() : null);
            if (objecFilesDto.getUrl() != null && objecFilesDto.getFilename() != null) {
                objectFilesEntity.setUrl(objecFilesDto.getUrl() + objecFilesDto.getFilename());
            }
        }
        return objectFilesEntity;
    }

    @Override
    public ObjecFilesDto toDto(ObjectFilesEntity objectFilesEntity) {
        ObjecFilesDto objecFilesDto = new ObjecFilesDto();

        if (objectFilesEntity != null) {
            objecFilesDto.setId(objectFilesEntity.getId() != null ? objectFilesEntity.getId() : null);
            objecFilesDto.setObjectId(objectFilesEntity.getObjectId() != null ? objectFilesEntity.getObjectId() : null);
            objecFilesDto.setFilename(objectFilesEntity.getFileName() != null ? objectFilesEntity.getFileName() : null);
            objecFilesDto.setOriginalFilename(objectFilesEntity.getOriginalFilename() != null ? objectFilesEntity.getOriginalFilename() : null);
            objecFilesDto.setDescription(objectFilesEntity.getDescription() != null ? objectFilesEntity.getDescription() : null);
            objecFilesDto.setUrl(objectFilesEntity.getUrl() != null ? objectFilesEntity.getUrl() : null);
        }
        return objecFilesDto;

    }
}
