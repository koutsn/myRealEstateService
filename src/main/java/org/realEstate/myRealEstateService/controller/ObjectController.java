package org.realEstate.myRealEstateService.controller;

import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.ObjectDto;
import org.realEstate.myRealEstateService.response.ErrorResponse;
import org.realEstate.myRealEstateService.service.ObjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${spring.application.name}")
public class ObjectController {
    private final ObjectService objectService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(
            value = "/object",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> createObject(@ModelAttribute ObjectDto object) {
        try {
            objectService.createObject(object);
            return ResponseEntity.created(null).build();
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
