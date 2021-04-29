package com.ga.appgate.security;

import com.ga.appgate.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.ga.appgate.data.entity.User> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            return new User(username, optionalUser.get().getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Datos de acceso incorrectos");
        }
    }
}
