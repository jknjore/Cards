package com.logicea.cards.services;

import com.logicea.cards.helpers.Configs;
import com.logicea.cards.entities.CardUser;
import com.logicea.cards.helpers.Utils;
import com.logicea.cards.repositories.UsersRepository;
import com.logicea.cards.responses.AuthenticationResponse;
import com.logicea.cards.jwt.security.MyUserDetailsService;
import com.logicea.cards.jwt.services.TokenUtil;
import com.logicea.cards.models.AuthenicationRequest;
import com.logicea.cards.responses.Response;
import com.logicea.cards.services.interfaces.IAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements IAuthenticationService
{
    private final AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    Configs configs;

    @Override
    public Response addUser(CardUser user)
    {
            user.setPassword(Utils.encodePassword(user.getPassword()));
            CardUser cardUser = usersRepository.save(user);
            return new Response(true, "User Added successfully", null);
    }

    @Override
    public AuthenticationResponse authenticateUser(AuthenicationRequest authenicationRequest) {
        AuthenticationResponse response = new AuthenticationResponse();
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenicationRequest.getEmail(), authenicationRequest.getPassword()));
                final UserDetails userDetails = userDetailsService.loadUserByUsername(authenicationRequest.getEmail());
                final String jwt = tokenUtil.generateToken(new HashMap<>(),userDetails, configs.getJwt_token_expiry_minutes());
                final String jwtRefresh = tokenUtil.generateRefreshToken(userDetails, configs.getJwt_refresh_expiry_minutes());
                response.setSuccess(true);
                response.setMessage("Authentication was successful.");
                response.setToken(jwt);
                response.setExpires(configs.getJwt_token_expiry_minutes()*3600);
                response.setRefreshToken(jwtRefresh);
                response.setRefreshExpiry(configs.getJwt_refresh_expiry_minutes()*3600);
            } catch (BadCredentialsException e) {
                response.setMessage(String.format("Could not authenticate user/channel because (of) %s", e.getMessage()));
                throw new BadCredentialsException(String.format("Could not authenticate user/channel because (of) %s", e.getMessage()),e);
            }

        return response;
    }
}
