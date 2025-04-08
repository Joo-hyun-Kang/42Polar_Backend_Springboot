package com._polar._polar_backend_spring.v1.mentors.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestEmailDto {
    @NotNull
    @NotBlank
    @Email
    private String email;
}
