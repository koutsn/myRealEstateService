package org.realEstate.myRealEstateService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}

