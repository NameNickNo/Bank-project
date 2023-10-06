package com.project.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    @NotBlank(message = "can not be null!")
    @Size(min = 4, max = 50, message = "must be between 4 and 50 symbols!")
    private String firstName;

    @NotBlank(message = "can not be null!")
    @Size(min = 4, max = 50, message = "must be between 4 and 50 symbols!")
    private String lastName;

    @NotBlank(message = "can not be null!")
    @DateTimeFormat
    private LocalDate birthDate;
}
