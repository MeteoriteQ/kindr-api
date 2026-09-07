package com.example.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Entity.CampaignUpdate;
public interface CampaignUpdateRepository extends JpaRepository<CampaignUpdate,String>{ List<CampaignUpdate> findByCampaignIdOrderByCreatedAtDesc(Long campaignId); }
