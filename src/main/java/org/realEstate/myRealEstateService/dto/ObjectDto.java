package org.realEstate.myRealEstateService.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.realEstate.myRealEstateService.Enum.ObjectStatus;
import org.springframework.web.multipart.MultipartFile;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectDto {
    @NotNull
    @Id
    private UUID id;
    @NotNull
    private String description;
    @NotNull
    private AddressDto address;
    @NotNull
    private float price;
    @NotNull
    private int numberRooms;
    @NotNull
    private int numberBathrooms;
    @NotNull
    private int garageSize;
    @NotNull
    private ObjectStatus status;
    @NotNull
    private OffsetDateTime createdAt;
    @NotNull
    private OffsetDateTime updatedAt;
    @NotNull
    private String thumbnail;

}
