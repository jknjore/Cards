package com.logicea.cards.responses;

import lombok.Data;

//Token response
@Data
public class AuthenticationResponse {
    private boolean success;
    private String message;
    private String token;
    private Long expires;
    private String refreshToken;
    private Long refreshExpiry;
}
