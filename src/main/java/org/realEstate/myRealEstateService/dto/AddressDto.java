package org.realEstate.myRealEstateService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    @Id
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;
    @NotNull
    private String street;
    @NotNull
    private String zipCode;
    @NotNull
    private String city;
    @NotNull
    private String country;

}
