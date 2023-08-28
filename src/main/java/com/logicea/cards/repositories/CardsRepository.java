package com.logicea.cards.repositories;

import com.logicea.cards.entities.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface CardsRepository extends JpaRepository<Card,Long>, JpaSpecificationExecutor<Card> {
    @Query(value = "select * from cards where user_id = ?1", nativeQuery = true )
    Page<Card> searchUserCards(Long userid, String name, String color, String status, LocalDateTime createdOn, Pageable pageable);
}
