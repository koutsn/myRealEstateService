package org.realEstate.myRealEstateService.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private final String status;
    private final String message;
}
