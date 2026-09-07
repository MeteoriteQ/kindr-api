package com.example.repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import com.example.Entity.Payment;
import jakarta.persistence.LockModeType;
public interface PaymentRepository extends JpaRepository<Payment,String>{
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select p from Payment p where p.id=:id") Optional<Payment> findByIdForUpdate(@Param("id") String id);
 Optional<Payment> findByProviderPaymentId(String providerPaymentId);
}
