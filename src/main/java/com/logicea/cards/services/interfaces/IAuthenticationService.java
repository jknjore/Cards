package com.logicea.cards.services.interfaces;

import com.logicea.cards.entities.CardUser;
import com.logicea.cards.responses.AuthenticationResponse;
import com.logicea.cards.models.AuthenicationRequest;
import com.logicea.cards.responses.Response;
import org.springframework.stereotype.Component;

@Component
public interface IAuthenticationService {
    // Users Authentication services
    Response addUser(CardUser user);
    AuthenticationResponse authenticateUser(AuthenicationRequest authenicationRequest);
}
