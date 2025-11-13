package org.realEstate.myRealEstateService.dto;

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
    private String[] name;
    private MultipartFile[] file;
}
