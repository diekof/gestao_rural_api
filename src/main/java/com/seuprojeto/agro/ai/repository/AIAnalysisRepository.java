package com.seuprojeto.agro.ai.repository;

import com.seuprojeto.agro.ai.domain.AIAnalysis;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AIAnalysisRepository extends JpaRepository<AIAnalysis, UUID> {
}
