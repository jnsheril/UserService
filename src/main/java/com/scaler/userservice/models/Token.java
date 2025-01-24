package com.scaler.userservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jdk.jfr.Enabled;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import java.util.Date;
import java.util.Optional;

@Setter
@Getter
@Entity

public class Token extends BaseModel{
    private String value;
    private Date expiryAt;

    @ManyToOne
    private User user;
}
