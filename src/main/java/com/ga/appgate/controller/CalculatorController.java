package com.ga.appgate.controller;

import com.ga.appgate.model.OperandRequest;
import com.ga.appgate.security.JwtTokenUtil;
import com.ga.appgate.service.OperandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController()
public class CalculatorController {

    private final OperandService operandService;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public CalculatorController(OperandService operandService,
                                JwtTokenUtil jwtTokenUtil) {
        this.operandService = operandService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping(value = "/operations/operands")
    public ResponseEntity<Void> addOperand(
            @RequestHeader("Authorization") String token,
            @RequestBody OperandRequest request) {
        String jwtId = jwtTokenUtil.getJwtId(token);
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        operandService.addOperand(username, jwtId, request.getValue());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/operations/{operation}")
    public ResponseEntity<Double> calculate(
            @RequestHeader("Authorization") String token,
            @PathVariable Integer operation) {
        if(operation < 1 || operation > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        String jwtId = jwtTokenUtil.getJwtId(token);
        String username = jwtTokenUtil.getUsernameFromToken(token.substring(7));
        Double result = operandService.executeOperation(username, jwtId, operation);
        return ResponseEntity.ok(result);
    }
}
