package com.radiuk.securityexample.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters long")
    private String fullname;

    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 100, message = "Username must be between 2 and 100 characters long")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 100, message = "Password must be between 6 and 100 characters long")
    private String password;

    @NotBlank(message = "Email is required")
    @Email(message = "Incorrect email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+\\d{1,3}\\(\\d{1,4}\\)\\d{7,10}$", message = "Incorrect phone format. Correct format:+...(...)........")
    private String phone;
}
