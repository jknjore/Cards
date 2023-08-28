package com.logicea.cards.entities;

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
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_id")
    @Hidden
    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    private String description;

    @Pattern(regexp = "^#([A-Fa-f0-9]{6})$",message = "Color should conform to a 6 alphanumeric characters prefixed with a '#'")
    private String color;

    @Hidden
    private String status="To Do";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Hidden
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JsonIgnore
    private CardUser user;

    @Hidden
    private LocalDateTime createdOn= LocalDateTime.now();

    @Hidden
    @JsonIgnore
    private boolean deleted=false;
}
