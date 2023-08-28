package com.logicea.cards.repositories;

import com.logicea.cards.entities.CardUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<CardUser,Long> {
    Optional<CardUser> findByEmail(String email);
}
