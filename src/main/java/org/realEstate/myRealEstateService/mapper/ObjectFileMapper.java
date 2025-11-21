package org.realEstate.myRealEstateService.mapper;

import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Component
public interface ObjectFileMapper {
    ObjectFilesEntity toEntity(ObjecFilesDto objecFilesDto);

    ObjecFilesDto toDto(ObjectFilesEntity objectFilesEntity);
}
