package com.logicea.cards.helpers;

public class Endpoints {
    //authentication
    public static final String AUTHENTICATION_BASE = "/auth/user";
    public static final String REGISTER = "/register";
    public static final String AUTHENTICATE = "/authenticate";

    //cards
    public static final String CARDS_BASE = "/cards";
    public static final String ADD_CARD = "/add-card";
    public static final String GET_CARD = "/get-card/{id}";
    public static final String GET_CARDS = "/get-cards";
    public static final String SEARCH_CARDS = "/search-cards";
    public static final String UPDATE_CARD = "/update-card/{id}";
    public static final String DELETE_CARD = "/delete-card/{id}";
}
