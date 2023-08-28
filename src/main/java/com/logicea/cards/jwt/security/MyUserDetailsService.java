package com.logicea.cards.jwt.security;


import com.logicea.cards.entities.CardUser;
import com.logicea.cards.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<CardUser> user = usersRepository.findByEmail(email);
        return user.map(channels -> new User(user.get().getEmail(), channels.getPassword(), new ArrayList<>())).orElseGet(() -> new User(null, null, new ArrayList<>()));
    }
}
