package com.project.account.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreateDTO {

    @NotBlank(message = "can not be null!")
    @Size(min = 4, max = 50, message = "must be between 4 and 50 symbols!")
    private String username;

    @Min(value = 1000, message = "must be 4 symbols!")
    @Max(value = 9999, message = "must be 4 symbols!")
    private int pin;

    private ProfileDTO profile;
}
