package org.realEstate.myRealEstateService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.realEstate.myRealEstateService.Enum.Role;
import org.realEstate.myRealEstateService.Enum.Status;
import org.springframework.data.annotation.ReadOnlyProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    @Id
    @NotNull
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private Role role;
    @NotNull
    private Status status;

}
