package com.logicea.cards.controllers;

import com.logicea.cards.DTOs.CardDTO;
import com.logicea.cards.entities.Card;
import com.logicea.cards.entities.CardUser;
import com.logicea.cards.helpers.Endpoints;
import com.logicea.cards.helpers.Utils;
import com.logicea.cards.jwt.security.UserSession;
import com.logicea.cards.responses.Response;
import com.logicea.cards.services.interfaces.ICardsService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Tag(name="2. Cards Operations")
@RestController
@RequestMapping(Endpoints.CARDS_BASE)
@SecurityRequirement(name = "BearerToken")
public class CardsControler {
    @Autowired
    ICardsService iCardsService;

    @Autowired
    UserSession userSession;

    //Add a new Card
    @PostMapping(value = Endpoints.ADD_CARD,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response addCard(@Valid @RequestBody Card card)
    {
        CardUser user = userSession.getCurrentUser();

        card.setUser(user);
        return iCardsService.addCard(card);
    }

    //Get all cards to Admin; Get user assigned cards to member
    @GetMapping(value = Endpoints.GET_CARDS, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getCards()
    {
        return iCardsService.getCards();
    }

    //Get a single card
    @GetMapping(value = Endpoints.GET_CARD, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response getCard(@PathVariable("id") Long id)
    {
        return iCardsService.getCard(id);
    }

    //Search for a card using card id
    @GetMapping(value = Endpoints.SEARCH_CARDS, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response searchCards(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) LocalDate createdOn,
            @RequestParam(required = false,defaultValue = "0") int page,
            @RequestParam(required = false,defaultValue = "10") int size,
            @RequestParam(required = false,defaultValue = "name:asc") String[] sort
    )
    {
        List<Sort.Order> orders = new ArrayList<Sort.Order>();

            for (String sortOrder : sort) {
                sortOrder =sortOrder.trim();
                if (sortOrder.contains(":")) {
                String[] _sort = sortOrder.split(":");
                orders.add(new Sort.Order(Utils.getSortDirection(_sort[1]), _sort[0]));
            }
        }

        Pageable pageable = PageRequest.of(page,size,Sort.by(orders));
        return iCardsService.searchCards( name,  color,  status, createdOn, pageable);
    }

    //Update card data,  exclusive of creation date and card user
    @PutMapping(value = Endpoints.UPDATE_CARD,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateCard(@Valid @RequestBody CardDTO cardDTO, @PathVariable("id") Long id)
    {
        return iCardsService.updateCard(cardDTO,id);
    }

    //Delete a single card by id
    @DeleteMapping(value = Endpoints.DELETE_CARD, produces = MediaType.APPLICATION_JSON_VALUE)
    public Response deleteCard(@PathVariable("id") Long id)
    {
        return iCardsService.deleteCard(id);
    }
}
