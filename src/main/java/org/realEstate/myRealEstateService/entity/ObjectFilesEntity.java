package org.realEstate.myRealEstateService.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class ObjectFilesEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private UUID objectId;
    private String name;
    private String file;
}
