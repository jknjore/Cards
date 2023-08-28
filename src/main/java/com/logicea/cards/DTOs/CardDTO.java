package com.logicea.cards.DTOs;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardDTO {

    @NotNull
    @NotEmpty
    private String name;

    private String description;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$",message = "Color should conform to a 6 alphanumeric characters prefixed with a '#'")
    private String color;

    private String status="To Do";
}
