package org.realEstate.myRealEstateService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.realEstate.myRealEstateService.Enum.ObjectStatus;
import org.realEstate.myRealEstateService.dto.AddressDto;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "objects")
public class ObjectEntity {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private UUID id;

    @NotNull
    @Column(name = "description")
    private String description;

    @NotNull
    @OneToOne
    @JoinColumn(
            name = "address_id",
            referencedColumnName = "id",
            nullable = false,
            unique = true
    )
    private AddressEntity address;
    @NotNull
    @Column(name = "price")
    private float price;
    @NotNull
    @Column(name = "number_rooms")
    private int numberRooms;
    @NotNull
    @Column(name = "number_bathrooms")
    private int numberBathrooms;
    @Null
    @Column(name = "has_garage")
    private boolean hasGarage;
    @Null
    @Column(name = "garage_size")
    private int garageSize;
    @NotNull
    @Column(name = "status")
    private ObjectStatus status;
    @NotNull
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @NotNull
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

}

