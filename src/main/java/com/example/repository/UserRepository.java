package com.example.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Entity.User;
public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEmailIgnoreCase(String email);
    default Optional<User> findByEmail(String email){return findByEmailIgnoreCase(email);}
    default boolean existsByEmail(String email){return existsByEmailIgnoreCase(email);}
}
