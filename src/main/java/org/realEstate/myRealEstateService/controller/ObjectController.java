package org.realEstate.myRealEstateService.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.ObjecFilesDto;
import org.realEstate.myRealEstateService.response.ErrorResponse;
import org.realEstate.myRealEstateService.service.ObjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.name}")
public class ObjectController {

    private final ObjectService objectService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(
            value = "/object/{id}/images",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> createImage(@PathVariable UUID id, @ModelAttribute ObjecFilesDto file) {
        try {
            objectService.uploadImages(id, file);
            return ResponseEntity.created(null).build();
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @GetMapping("/object/{id}/images")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> getObjectImages(@PathVariable UUID id) {
        try {
            List<ObjecFilesDto> objectImages = objectService.getImagesForObject(id);
            return ResponseEntity.ok(objectImages);
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @GetMapping("/image/{id}")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> getImage(@PathVariable UUID id) {
        try {
            ObjecFilesDto objectImage = objectService.getImageById(id);
            return ResponseEntity.ok(objectImage);
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
