package com.logicea.cards.models;

import lombok.Data;

@Data
public class AuthenicationRequest {
    private String email;
    private String password;
}
