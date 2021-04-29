package com.ga.appgate.service;

import com.ga.appgate.constant.TestVariables;
import com.ga.appgate.data.entity.Operand;
import com.ga.appgate.data.repository.OperandRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ga.appgate.constant.TestVariables.JWT_TOKEN;
import static com.ga.appgate.constant.TestVariables.USERNAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperandServiceTest {

    @InjectMocks
    OperandService service;

    @Mock
    private OperandRepository repository;

    @Test
    void shouldSaveNewOperandWithGivenDataWhenAddOperand() {
        // Given
        final double operandValue = 8.0;
        final ArgumentCaptor<Operand> operandCaptor = ArgumentCaptor.forClass(Operand.class);

        // When
        service.addOperand(USERNAME, JWT_TOKEN, operandValue);

        // Then
        verify(repository).save(operandCaptor.capture());
        final Operand operand = operandCaptor.getValue();
        assertEquals(USERNAME, operand.getUsername());
        assertEquals(JWT_TOKEN, operand.getSessionId());
        assertEquals(operandValue, operand.getValue());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnZeroWhenNoOperandsFound() {
        // When
        when(repository.findByUsernameAndSessionIdAndUsed(USERNAME, JWT_TOKEN, false)).thenReturn(Collections.emptyList());
        final Double result = service.executeOperation(USERNAME, JWT_TOKEN, 1);

        // Then
        verify(repository).findByUsernameAndSessionIdAndUsed(USERNAME, JWT_TOKEN, false);
        assertEquals(0, result);
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldThrowExceptionWhenOperationCodeNotSupported() {
        // Given
        final List<Operand> operandList = List.of(TestVariables.buildOperand(1),
                TestVariables.buildOperand(2));
        int notSupportedOperation = 8;
        final ArgumentCaptor<Operand> operandCaptor = ArgumentCaptor.forClass(Operand.class);

        // When
        when(repository.findByUsernameAndSessionIdAndUsed(USERNAME, JWT_TOKEN, false)).thenReturn(operandList);

        // Then
        assertThrows(IllegalArgumentException.class, () -> service.executeOperation(USERNAME, JWT_TOKEN, notSupportedOperation));
        verify(repository).findByUsernameAndSessionIdAndUsed(USERNAME, JWT_TOKEN, false);
        verify(repository, times(2)).save(operandCaptor.capture());
        final List<Operand> values = operandCaptor.getAllValues();
        values.forEach(operand -> assertTrue(operand.isUsed()));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldReturnInfiniteWhenDivisionByZero() {
        // Given
        final List<Operand> operandList = List.of(TestVariables.buildOperand(1),
                TestVariables.buildOperand(0));

        // When
        when(repository.findByUsernameAndSessionIdAndUsed(USERNAME, JWT_TOKEN, false)).thenReturn(operandList);
        final Double result = service.executeOperation(USERNAME, JWT_TOKEN, 4);

        // Then
        assertTrue(Double.isInfinite(result));
    }

    @Test
    void shouldReturnNaNWhenOperationResultIsTooLarge() {
        // Given
        final List<Operand> operandList = List.of(TestVariables.buildOperand(1234),
                TestVariables.buildOperand(520));

        // When
        when(repository.findByUsernameAndSessionIdAndUsed(USERNAME, JWT_TOKEN, false)).thenReturn(operandList);
        final Double result = service.executeOperation(USERNAME, JWT_TOKEN, 5);

        // Then
        assertTrue(Double.isInfinite(result));
    }

    @ParameterizedTest
    @MethodSource("getExpectedValuesByOperation")
    void shouldReturnResultAndSaveCorrectlyWhenCalculationIsOk(int operation, Double value1, Double value2, Double expectedResult) {
        // Given
        final List<Operand> operandList = List.of(TestVariables.buildOperand(value1),
                TestVariables.buildOperand(value2));
        final ArgumentCaptor<Operand> operandCaptor = ArgumentCaptor.forClass(Operand.class);

        // When
        when(repository.findByUsernameAndSessionIdAndUsed(USERNAME, JWT_TOKEN, false)).thenReturn(operandList);
        final Double result = service.executeOperation(USERNAME, JWT_TOKEN, operation);

        // Then
        verify(repository).findByUsernameAndSessionIdAndUsed(USERNAME, JWT_TOKEN, false);
        verify(repository, times(3)).save(operandCaptor.capture());
        assertEquals(expectedResult, result);
        final List<Operand> values = operandCaptor.getAllValues();
        assertEquals(2, values.stream().filter(Operand::isUsed).count());
        final Stream<Operand> unusedOperandStream = values.stream().filter(operand -> !operand.isUsed());
        final List<Operand> unusedOperandList = unusedOperandStream.collect(Collectors.toList());
        assertEquals(1, unusedOperandList.size());
        assertEquals(expectedResult, unusedOperandList.get(0).getValue());
        verifyNoMoreInteractions(repository);
    }

    private static Stream<Arguments> getExpectedValuesByOperation() {
        return Stream.of(Arguments.of(1, 8.0, 2.0, 10.0),
                Arguments.of(2, 8.0, 2.0, 6.0),
                Arguments.of(3, 8.0, 2.0, 16.0),
                Arguments.of(4, 8.0, 2.0, 4.0),
                Arguments.of(5, 8.0, 3.0, 512.0)
        );
    }
}