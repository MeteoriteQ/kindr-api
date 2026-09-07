package com.example.repository;
import java.util.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import com.example.Entity.Campaign;
public interface CampaignRepository extends JpaRepository<Campaign,Long>{
 List<Campaign> findByStatusNotOrderByCreatedAtDesc(String status);
 List<Campaign> findByOwnerIdAndStatusNotOrderByCreatedAtDesc(String ownerId,String status);
 List<Campaign> findByCategoryIgnoreCaseAndStatusNotOrderByCreatedAtDesc(String category,String status);
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select c from Campaign c where c.id=:id") Optional<Campaign> findByIdForUpdate(@Param("id") Long id);
}
