package org.realEstate.myRealEstateService.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.realEstate.myRealEstateService.dto.ObjectDto;
import org.realEstate.myRealEstateService.response.ErrorResponse;
import org.realEstate.myRealEstateService.service.ObjectService;
import org.springframework.http.HttpStatus;
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
            value = "/object"
    )
    public ResponseEntity<?> createObject(@Valid @ModelAttribute ObjectDto object) {
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

    @GetMapping(
            value = "/objects"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> getAllObjects() {
        try {
            List<ObjectDto> objects = objectService.getAllObjects();
            return ResponseEntity.ok(objects);
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @GetMapping(
            value = "/object/|{id}"
    )
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ResponseEntity<?> getAllObjects(@PathVariable UUID id) {
        try {
            ObjectDto objects = objectService.getObjectById(id);
            return ResponseEntity.ok(objects);
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(
            value = "/object/|{id}"
    )
    public ResponseEntity<?> deleteObject(@PathVariable UUID id) {
        try {
            objectService.deleteObject(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.toString(), e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(response);
        }
    }
}
