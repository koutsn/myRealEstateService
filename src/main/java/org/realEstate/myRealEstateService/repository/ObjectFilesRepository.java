package org.realEstate.myRealEstateService.repository;

import org.realEstate.myRealEstateService.entity.ObjectFilesEntity;
import org.realEstate.myRealEstateService.entity.UserEntity;
import org.realEstate.myRealEstateService.mapper.ObjectFileMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ObjectFilesRepository extends JpaRepository<ObjectFilesEntity, UUID> {
}
