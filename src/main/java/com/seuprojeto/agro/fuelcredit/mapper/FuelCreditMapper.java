package com.seuprojeto.agro.fuelcredit.mapper;

import com.seuprojeto.agro.fuelcredit.domain.FuelCredit;
import com.seuprojeto.agro.fuelcredit.domain.FuelCreditStatus;
import com.seuprojeto.agro.fuelcredit.dto.FuelCreditCreateRequest;
import com.seuprojeto.agro.fuelcredit.dto.FuelCreditResponse;
import com.seuprojeto.agro.fuelcredit.dto.FuelCreditUpdateRequest;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

@Component
public class FuelCreditMapper {

    public FuelCredit toEntity(FuelCreditCreateRequest request, BigDecimal initialBalance) {
        FuelCredit entity = new FuelCredit();
        entity.setUserId(request.userId());
        entity.setCreditLimit(request.creditLimit());
        entity.setBalance(initialBalance);
        entity.setStatus(request.status() != null ? request.status() : FuelCreditStatus.ACTIVE);
        return entity;
    }

    public void updateEntity(FuelCredit entity, FuelCreditUpdateRequest request) {
        if (request.creditLimit() != null) {
            entity.setCreditLimit(request.creditLimit());
        }
        if (request.status() != null) {
            entity.setStatus(request.status());
        }
    }

    public FuelCreditResponse toResponse(FuelCredit entity) {
        BigDecimal consumed = entity.getCreditLimit().subtract(entity.getBalance());
        String userName = entity.getUser() != null ? entity.getUser().getNome() : null;
        return new FuelCreditResponse(
                entity.getId(),
                entity.getTenantId(),
                entity.getUserId(),
                userName,
                entity.getStatus(),
                entity.getCreditLimit(),
                entity.getBalance(),
                consumed,
                toInstant(entity.getLastRechargeAt()),
                toInstant(entity.getCreatedAt()),
                toInstant(entity.getUpdatedAt()),
                entity.getCreatedBy(),
                entity.getUpdatedBy()
        );
    }

    private Instant toInstant(OffsetDateTime source) {
        return source != null ? source.toInstant() : null;
    }
}

