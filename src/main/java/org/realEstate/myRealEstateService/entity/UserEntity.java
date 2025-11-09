package org.realEstate.myRealEstateService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.realEstate.myRealEstateService.Enum.Role;
import org.realEstate.myRealEstateService.Enum.Status;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {

    @Id
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private Role roles;
    private Status status;

}
