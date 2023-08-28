package com.logicea.cards.helpers;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Data
public class Configs {

    @Value("${jwt.expiry.token.minutes}")
    private Long jwt_token_expiry_minutes;

    @Value("${jwt.expiry.refresh.minutes}")
    private Long jwt_refresh_expiry_minutes;

    @Value("${jwt.secret-key}")
    private String jwt_secret_key;
}
