package com.ga.appgate.service;

import com.ga.appgate.data.entity.Operand;
import com.ga.appgate.data.repository.OperandRepository;
import com.ga.appgate.model.OperationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class OperandService {

    private final OperandRepository repository;

    @Autowired
    public OperandService(OperandRepository repository) {
        this.repository = repository;
    }

    public void addOperand(String username, String sessionId, Double value) {
        Operand operand = Operand.builder().username(username).sessionId(sessionId).value(value).build();
        repository.save(operand);
    }

    public Double executeOperation(String username, String sessionId, int operation) {
        // Get unused operands from database by session ID
        List<Operand> unusedOperands = repository.findByUsernameAndSessionIdAndUsed(username, sessionId, false);
        Optional<Double> result;
        try {
            result = calculate(unusedOperands, operation);
        }
        finally {
            updateOperandsAsUsed(unusedOperands);
        }
        return processResult(username, sessionId, result);
    }

    private Optional<Double> calculate(List<Operand> operandList, int operation) {
        Stream<Double> doubleStream = operandList.stream().map(Operand::getValue);
        switch (operation) {
            case OperationConstants.ADDITION:
                return doubleStream.reduce(Double::sum);
            case OperationConstants.SUBTRACTION:
                return doubleStream.reduce((a, b) -> a - b);
            case OperationConstants.MULTIPLICATION:
                return doubleStream.reduce((a, b) -> a * b);
            case OperationConstants.DIVISION:
                return doubleStream.reduce((a, b) -> a / b);
            case OperationConstants.EMPOWERMENT:
                return doubleStream.reduce(Math::pow);
            default:
                throw new IllegalArgumentException();
        }
    }

    private void updateOperandsAsUsed(List<Operand> usedOperands) {
        usedOperands.forEach(operand -> {
            operand.setUsed(true);
            repository.save(operand);
        });
    }

    private Double processResult(String username, String sessionId, Optional<Double> result) {
        if (result.isPresent()) {
            Double resultValue = result.get();
            if (Double.isFinite(result.get()) && !Double.isNaN(result.get())) {
                Operand operand = Operand.builder().username(username).sessionId(sessionId).value(resultValue).build();
                repository.save(operand);
            }
            return resultValue;
        } else {
            return 0.0;
        }
    }
}
