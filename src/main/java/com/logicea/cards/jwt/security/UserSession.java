package com.logicea.cards.jwt.security;

import com.logicea.cards.entities.CardUser;
import com.logicea.cards.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserSession {
    @Autowired
    private UsersRepository usersRepository;

    public CardUser getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Optional<CardUser> user = usersRepository.findByEmail(auth.getName());
        return user.get();
    }
}
