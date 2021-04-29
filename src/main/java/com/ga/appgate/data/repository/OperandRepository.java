package com.ga.appgate.data.repository;

import com.ga.appgate.data.entity.Operand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperandRepository extends JpaRepository<Operand, Integer> {

    List<Operand> findByUsernameAndSessionIdAndUsed(String username, String sessionId, boolean used);
}
