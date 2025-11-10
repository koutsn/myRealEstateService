package org.realEstate.myRealEstateService.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    private String username;
    private String password;
}

