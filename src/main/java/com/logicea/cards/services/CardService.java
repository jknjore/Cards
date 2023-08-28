package com.logicea.cards.services;

import com.logicea.cards.DTOs.CardDTO;
import com.logicea.cards.entities.Card;
import com.logicea.cards.entities.CardUser;
import com.logicea.cards.helpers.AppConstants;
import com.logicea.cards.helpers.searchspec.FiltersSpecification;
import com.logicea.cards.helpers.searchspec.SearchCriteria;
import com.logicea.cards.helpers.searchspec.SearchOperation;
import com.logicea.cards.jwt.security.UserSession;
import com.logicea.cards.models.Role;
import com.logicea.cards.repositories.CardsRepository;
import com.logicea.cards.responses.Response;
import com.logicea.cards.services.interfaces.ICardsService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CardService implements ICardsService
{
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CardsRepository cardsRepository;

    @Autowired
    private UserSession userSession;

    @Override
    public Response addCard(Card card) {
        if(!(Arrays.asList(AppConstants.cardStatus).contains(card.getStatus())))
        {
            return new Response(false,"Card status can only be 'To Do', 'In Progress' or 'Done'",card);
        }
        Card userCard = cardsRepository.save(card);
        return new Response(true, "Card created successfully", null);
    }

    @Override
    public Response getCards() {
        CardUser user = userSession.getCurrentUser();
        List<Card> cardList = new ArrayList<>();

        if(user.getRole().equals(Role.MEMBER))
        {
            cardList = user.getCards();
        }
        else if(user.getRole().equals(Role.ADMIN))
        {
            cardList = cardsRepository.findAll();
        }

        Response response = new Response(true,"Cards fetched successfully",cardList);
        return  response;
    }

    @Override
    public Response getCard(Long id) {
        CardUser user = userSession.getCurrentUser();
        Card card = null;

        if(user.getRole().equals(Role.MEMBER))
        {
            card = user.getCards().stream().filter(c->c.getId() == id).findAny().orElse(null);
        }
        else if(user.getRole().equals(Role.ADMIN))
        {
            card = cardsRepository.findById(id).orElse(null);
        }

        Response response = new Response(true,"Card fetched successfully",card);
        return  response;
    }


    @Override
    public Response<Page<Card>> searchCards(String name, String color, String status, LocalDate createdOn, Pageable pageable) {
        String q="";
        CardUser user = userSession.getCurrentUser();

        FiltersSpecification<Card> filter = new FiltersSpecification<>();

        if(user.getRole().equals(Role.MEMBER))
            filter.add(new SearchCriteria("user", user, SearchOperation.EQUAL));

        if(name != null)
        filter.add(new SearchCriteria("name", name, SearchOperation.EQUAL));

        if(color != null)
            filter.add(new SearchCriteria("color", color, SearchOperation.EQUAL));

        if(status != null)
            filter.add(new SearchCriteria("status", status, SearchOperation.EQUAL));

        if(createdOn != null)
            filter.add(new SearchCriteria("createdOn", createdOn.atStartOfDay(), SearchOperation.GREATER_THAN_EQUAL));

        if(createdOn != null)
            filter.add(new SearchCriteria("createdOn", createdOn.atStartOfDay().plusDays(1), SearchOperation.LESS_THAN));

        Page<Card> cards =  cardsRepository.findAll(filter,pageable);


        Response response = new Response(true,"Cards fetched successfully",cards);
        return response;
    }

    @Override
    public Response updateCard(CardDTO cardDTO,Long id) {
        CardUser user = userSession.getCurrentUser();
        Long card_id = id;

        Card editCard =  null;
        if(user.getRole().equals(Role.MEMBER))
        {
            editCard = user.getCards().stream().filter(c->c.getId() == card_id).findAny().orElse(null);
        }
        else if(user.getRole().equals(Role.ADMIN))
        {
            editCard = cardsRepository.findById(card_id).orElse(null);
        }

        if(editCard == null)
        {
            return new Response(false,"Card to edit was not found",null);
        }

        if(!(Arrays.asList(AppConstants.cardStatus).contains(cardDTO.getStatus())))
        {
            return new Response(false,"Card status can only be 'To Do', 'In Progress' or 'Done'",null);
        }

        editCard.setName(cardDTO.getName());
        editCard.setDescription(cardDTO.getDescription());
        editCard.setColor(cardDTO.getColor());
        editCard.setStatus(cardDTO.getStatus());

        Card updatedCard =  cardsRepository.save(editCard);

        //Log Update
        System.out.println("User "+user.getId()+" updated card "+editCard.getId()+" at "+LocalDateTime.now());

        Response response = new Response(true,"Card updated successfully",updatedCard);
        return  response;
    }

    @Override
    public Response deleteCard(Long id) {
        CardUser user = userSession.getCurrentUser();
        Card card = null;

        if(user.getRole().equals(Role.MEMBER))
        {
            card = user.getCards().stream().filter(c->c.getId() == id).findAny().orElse(null);
        }
        else if(user.getRole().equals(Role.ADMIN))
        {
            card = cardsRepository.findById(id).orElse(null);
        }

        if(card == null)
        {
            return new Response(false,"Card to delete was not found",card);
        }

        //best option is to update deleted = true
        cardsRepository.delete(card);

        //Log deletion
        System.out.println("User "+user.getId()+" deleted card "+id+" at "+LocalDateTime.now());

        Response response = new Response(true,"Card deleted successfully",card);
        return  response;
    }
}
