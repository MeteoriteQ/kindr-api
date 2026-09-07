package com.example.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Entity.UserSession;
public interface UserSessionRepository extends JpaRepository<UserSession,String>{}
