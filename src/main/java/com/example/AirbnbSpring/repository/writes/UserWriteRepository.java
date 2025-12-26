package com.example.AirbnbSpring.repository.writes;

import com.example.AirbnbSpring.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWriteRepository extends JpaRepository<User,Long> {


   Optional<User> findByEmail(String email);
   User findById(Long id);
}
