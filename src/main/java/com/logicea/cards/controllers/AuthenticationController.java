package com.logicea.cards.controllers;

import com.logicea.cards.entities.CardUser;
import com.logicea.cards.helpers.Endpoints;
import com.logicea.cards.responses.AuthenticationResponse;
import com.logicea.cards.models.AuthenicationRequest;
import com.logicea.cards.responses.Response;
import com.logicea.cards.services.interfaces.IAuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="1. Authentication", description = "Registration and authentication of users")
@RestController
@RequestMapping(Endpoints.AUTHENTICATION_BASE)
public class AuthenticationController {
    @Autowired
    IAuthenticationService iAuthenticationService;

    //Add a new user
   @PostMapping(value = Endpoints.REGISTER,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response register(@Valid @RequestBody CardUser user)
   {
        return iAuthenticationService.addUser(user);
   }

   //Authenticate user and return token
    @PostMapping(value = Endpoints.AUTHENTICATE,consumes = "application/json", produces = "application/json")
    public AuthenticationResponse authenticate(@Valid @RequestBody AuthenicationRequest authenicationRequest)
    {
        return iAuthenticationService.authenticateUser(authenicationRequest);
    }
}
