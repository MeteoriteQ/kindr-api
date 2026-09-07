package com.example.repository;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Entity.Donation;
public interface DonationRepository extends JpaRepository<Donation,String>{
 Optional<Donation> findByPaymentId(String paymentId);
 List<Donation> findByDonorIdOrderByCreatedAtDesc(String donorId);
 List<Donation> findTop15ByCampaignIdOrderByCreatedAtDesc(Long campaignId);
 List<Donation> findByCampaignIdIn(List<Long> campaignIds);
}
