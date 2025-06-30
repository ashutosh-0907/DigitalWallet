package org.gfg.OnboardingService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gfg.enums.UserStatus;
import org.gfg.enums.UserIdentifier;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userId;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true, length = 13)
    private String mobNo;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    private String dob;

    @Enumerated(EnumType.STRING)
    private UserIdentifier userIdentifier;

    @Column(unique = true)
    private String userIdentifierValue;

    @CreationTimestamp
    private Date createdUpon;

    @UpdateTimestamp
    private Date updatedUpon;
}
