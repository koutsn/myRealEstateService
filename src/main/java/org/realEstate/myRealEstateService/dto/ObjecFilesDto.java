package org.realEstate.myRealEstateService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjecFilesDto {
    @Id
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID Id;
    @NotNull
    private UUID objectId;
    @NotNull
    private String filename;
    @NotNull
    private String originalFilename;
    private String description;
    private MultipartFile file;
    @NotNull
    private String url;
}
