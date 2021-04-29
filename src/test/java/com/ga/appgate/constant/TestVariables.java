package com.ga.appgate.constant;

import com.ga.appgate.data.entity.Operand;
import com.ga.appgate.model.OperandRequest;
import lombok.experimental.UtilityClass;

import java.util.Random;
import java.util.UUID;

@UtilityClass
public class TestVariables {

    public static final String USERNAME = "username";
    public static final String VALID_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImV4cCI6MTYxOTU4NTk4MCwiaWF0IjoxNjE5NTgyMzgwLCJqdGkiOiJiNzAyZjFhOS1jZGI5LTRmY2UtYmFjZS02N2QzYzlhNDE3ODQifQ.ndVh5_2bwjanLZc5-7KYj8IPG-CwZczm4lpbXZkf1SJMyLsdO1zaxsGe0uAaNnC3niCzTuttEJTFeM5pePL0gw";
    public static final String JWT_TOKEN = UUID.randomUUID().toString();
    public static final String SESSION_ID = UUID.randomUUID().toString();

    public static Operand buildOperand(double value) {
        final Random random = new Random();
        return Operand.builder().id(random.nextInt())
                .sessionId(UUID.randomUUID().toString())
                .username("an username")
                .value(value)
                .build();
    }

    public static OperandRequest buildOperandRequest() {
        return OperandRequest.builder().value(8.0).build();
    }
}
