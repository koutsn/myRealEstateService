package org.realEstate.myRealEstateService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.realEstate.myRealEstateService.Enum.Role;
import org.realEstate.myRealEstateService.Enum.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Role roles;
    private Status status;

}
