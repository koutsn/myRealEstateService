package org.realEstate.myRealEstateService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "addresses")
public class AddressEntity {
    @Id
    @NotNull
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private UUID id;
    @NotNull
    @Column(name = "street")
    private String street;
    @NotNull
    @Column(name = "zip_code")
    private String zipCode;
    @NotNull
    @Column(name = "city")
    private String city;
    @NotNull
    @Column(name = "country")
    private String country;
}