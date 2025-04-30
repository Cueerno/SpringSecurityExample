package com.radiuk.securityexample.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAuthDTO {

    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 100, message = "Username must be between 2 and 100 characters long")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters long")
    private String password;
}
