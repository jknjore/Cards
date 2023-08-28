package com.logicea.cards.services.interfaces;

import com.logicea.cards.DTOs.CardDTO;
import com.logicea.cards.entities.Card;
import com.logicea.cards.responses.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public interface ICardsService {
    // Card services
    Response addCard(Card card);
    Response getCards();
    Response getCard(Long id);
    Response<Page<Card>> searchCards(String name, String color, String status, LocalDate createdOn, Pageable pageable);
    Response updateCard(CardDTO cardDTO, Long id);
    Response deleteCard(Long id);
}
