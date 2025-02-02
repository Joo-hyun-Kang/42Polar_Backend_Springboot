package com._polar._polar_backend_spring.v1.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
public class SpringValidationResponse {
    private ArrayList<String> messages;
    private String error;
    private int statusCode;
}
