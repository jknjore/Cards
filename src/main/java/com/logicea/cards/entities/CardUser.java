package com.logicea.cards.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.logicea.cards.models.Role;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "users", indexes = @Index(name = "email_INDEX", columnList = "email"),
        uniqueConstraints = @UniqueConstraint(name = "email_UNiQUE", columnNames = "email")
)
public class CardUser  implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @Hidden
    private Long id;

    @NotNull
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @Hidden
    private LocalDateTime createdOn= LocalDateTime.now();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Hidden
    @JsonIgnore
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @ToString.Exclude
    private List<Card> cards;

    @Override
    @Hidden
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @Hidden
    public String getUsername() {
        return email;
    }

    @Override
    @Hidden
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Hidden
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Hidden
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Hidden
    public boolean isEnabled() {
        return true;
    }
}
