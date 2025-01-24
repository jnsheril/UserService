package com.scaler.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import java.util.List;

@Getter
@Setter
@Entity

public class User extends BaseModel{
    private String name;
    private String password;
    private String email;

    @ManyToMany
    private List<Role> roles;
}
