package org.realEstate.myRealEstateService.dto;

import jakarta.persistence.Column;
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
    private UUID objectId;
    private String description;
    private MultipartFile file;
    private String url;
}
