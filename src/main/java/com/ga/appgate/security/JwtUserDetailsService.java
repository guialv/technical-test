package com.ga.appgate.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("testuser".equals(username)) {
            return new User(username, "$2a$10$lTAWFTyWYm3CzPnn16ujiOUFZ1eM3WyIVMot0FX6vhkVgOeCQA5ym", new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Datos de acceso incorrectos");
        }
    }
}
