package com.ga.appgate.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@RequiredArgsConstructor
public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -2460665931931940136L;

    @Getter
    private final String token;
}
