package com.ga.appgate.controller;

import com.ga.appgate.model.OperandRequest;
import com.ga.appgate.security.JwtTokenUtil;
import com.ga.appgate.service.OperandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.ga.appgate.constant.TestVariables.JWT_TOKEN;
import static com.ga.appgate.constant.TestVariables.SESSION_ID;
import static com.ga.appgate.constant.TestVariables.USERNAME;
import static com.ga.appgate.constant.TestVariables.buildOperandRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTest {

    @Mock
    private OperandService operandService;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @InjectMocks
    CalculatorController controller;

    @Test
    void shouldInvokeServiceAndReturnOkWhenAddOperand() {
        // Given
        OperandRequest request = buildOperandRequest();
        
        // When
        when(jwtTokenUtil.getJwtId(JWT_TOKEN)).thenReturn(SESSION_ID);
        when(jwtTokenUtil.getUsernameFromToken(anyString())).thenReturn(USERNAME);
        ResponseEntity<Void> responseEntity = controller.addOperand(JWT_TOKEN, request);

        // Then
        verify(jwtTokenUtil).getJwtId(JWT_TOKEN);
        verify(jwtTokenUtil).getUsernameFromToken(anyString());
        verify(operandService).addOperand(USERNAME, SESSION_ID, request.getValue());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verifyNoMoreInteractions(operandService, jwtTokenUtil);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    void shouldReturnCalculationResultWhenSupportedOperation(int operation) {
        // Given
        final Double expectedResponse = 18.0;

        // When
        when(jwtTokenUtil.getJwtId(JWT_TOKEN)).thenReturn(SESSION_ID);
        when(jwtTokenUtil.getUsernameFromToken(anyString())).thenReturn(USERNAME);
        when(operandService.executeOperation(USERNAME, SESSION_ID, operation)).thenReturn(expectedResponse);
        ResponseEntity<Double> responseEntity = controller.calculate(JWT_TOKEN, operation);

        // Then
        verify(jwtTokenUtil).getJwtId(JWT_TOKEN);
        verify(jwtTokenUtil).getUsernameFromToken(anyString());
        verify(operandService).executeOperation(USERNAME, SESSION_ID, operation);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verifyNoMoreInteractions(jwtTokenUtil, operandService);
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, 0, 6})
    void shouldReturnBadRequestWhenOperationNotSupported(int operation) {
        // When
        ResponseEntity<Double> responseEntity = controller.calculate(JWT_TOKEN, operation);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        verifyNoInteractions(jwtTokenUtil, operandService);
    }
}