package org.realEstate.myRealEstateService.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "object_files")
public class ObjectFilesEntity {

    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR) // forces CHAR(36)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private UUID id;
    @JdbcTypeCode(SqlTypes.CHAR) // forces CHAR(36)
    @Column(name = "object_id", updatable = true, nullable = false)
    private UUID objectId;
    @Column(name = "name", updatable = true, nullable = false)
    private String name;
    @Column(name = "file_name", updatable = true, nullable = false)
    private String fileName;
}
