package com.ga.appgate.data.repository;

import com.ga.appgate.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
