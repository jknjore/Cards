package com.logicea.cards.helpers;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class Utils {
    public static String encodePassword(final String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static Sort.Direction getSortDirection(String s)
    {
        if(s.equalsIgnoreCase("asc"))
        {
            return Sort.Direction.ASC;
        }
        return Sort.Direction.DESC;
    }
}
