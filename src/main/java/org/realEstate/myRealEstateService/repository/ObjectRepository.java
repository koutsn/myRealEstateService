package org.realEstate.myRealEstateService.repository;

import org.realEstate.myRealEstateService.entity.ObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ObjectRepository extends JpaRepository<ObjectEntity, UUID> {

}
