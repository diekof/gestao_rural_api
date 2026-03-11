package com.seuprojeto.agro.fuelsupply.repository;

import com.seuprojeto.agro.fuelsupply.domain.FuelSupply;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FuelSupplyRepository extends JpaRepository<FuelSupply, UUID>, JpaSpecificationExecutor<FuelSupply> {
}

