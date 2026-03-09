package com.seuprojeto.agro.dashboard.service;

import com.seuprojeto.agro.agriculturaloperation.domain.OperationStatus;
import com.seuprojeto.agro.dashboard.dto.FinancialSummaryResponse;
import com.seuprojeto.agro.dashboard.dto.OperationsSummaryResponse;
import com.seuprojeto.agro.dashboard.dto.OverviewResponse;
import com.seuprojeto.agro.dashboard.dto.ProductionSummaryResponse;
import com.seuprojeto.agro.dashboard.repository.DashboardRepository;
import com.seuprojeto.agro.exception.UnauthorizedException;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryStatus;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryType;
import com.seuprojeto.agro.security.AuthenticatedUser;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final DashboardRepository repository;

    public DashboardService(DashboardRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public OverviewResponse overview(UUID tenantIdRequest) {
        UUID tenantId = tenantFilter(tenantIdRequest);
        BigDecimal revenue = nvl(repository.sumFinancialByType(tenantId, FinancialEntryType.REVENUE));
        BigDecimal expense = nvl(repository.sumFinancialByType(tenantId, FinancialEntryType.EXPENSE));
        return new OverviewResponse(
                repository.countFarms(tenantId),
                repository.countFields(tenantId),
                repository.countActiveSeasons(tenantId),
                repository.countOperationsCurrentMonth(tenantId),
                revenue,
                expense,
                revenue.subtract(expense)
        );
    }

    @Transactional(readOnly = true)
    public FinancialSummaryResponse financialSummary(UUID tenantIdRequest) {
        UUID tenantId = tenantFilter(tenantIdRequest);
        BigDecimal revenue = nvl(repository.sumFinancialByType(tenantId, FinancialEntryType.REVENUE));
        BigDecimal expense = nvl(repository.sumFinancialByType(tenantId, FinancialEntryType.EXPENSE));

        return new FinancialSummaryResponse(
                revenue,
                expense,
                nvl(repository.sumFinancialByStatus(tenantId, FinancialEntryStatus.PENDING)),
                nvl(repository.sumFinancialByStatus(tenantId, FinancialEntryStatus.PAID)),
                revenue.subtract(expense),
                repository.financialByCategory(tenantId).stream()
                        .map(r -> new FinancialSummaryResponse.CategoryAmountResponse(r.getCategoria().name(), nvl(r.getTotal())))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public ProductionSummaryResponse productionSummary(UUID tenantIdRequest) {
        UUID tenantId = tenantFilter(tenantIdRequest);
        Object[] totals = (Object[]) repository.productionTotals(tenantId);
        BigDecimal area = nvl((BigDecimal) totals[0]);
        BigDecimal expected = nvl((BigDecimal) totals[1]);
        BigDecimal actual = nvl((BigDecimal) totals[2]);

        return new ProductionSummaryResponse(
                area,
                expected,
                actual,
                area.compareTo(BigDecimal.ZERO) > 0 ? expected.divide(area, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO,
                area.compareTo(BigDecimal.ZERO) > 0 ? actual.divide(area, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO,
                repository.productionByCrop(tenantId).stream()
                        .map(r -> new ProductionSummaryResponse.CropProductionResponse(r.getCropName(), nvl(r.getPlantedArea()), nvl(r.getExpectedProduction()), nvl(r.getActualProduction())))
                        .toList()
        );
    }

    @Transactional(readOnly = true)
    public OperationsSummaryResponse operationsSummary(UUID tenantIdRequest) {
        UUID tenantId = tenantFilter(tenantIdRequest);
        Object[] totals = (Object[]) repository.operationTotals(tenantId);

        return new OperationsSummaryResponse(
                repository.countOperations(tenantId),
                repository.countOperationsByStatus(tenantId, OperationStatus.PLANNED),
                repository.countOperationsByStatus(tenantId, OperationStatus.IN_PROGRESS),
                repository.countOperationsByStatus(tenantId, OperationStatus.COMPLETED),
                repository.countOperationsByStatus(tenantId, OperationStatus.CANCELED),
                nvl((BigDecimal) totals[0]),
                nvl((BigDecimal) totals[1]),
                nvl((BigDecimal) totals[2]),
                repository.operationsByType(tenantId).stream()
                        .map(r -> new OperationsSummaryResponse.OperationTypeSummaryResponse(r.getTipo().name(), r.getTotal(), nvl(r.getArea()), nvl(r.getCost())))
                        .toList()
        );
    }

    private UUID tenantFilter(UUID tenantIdRequest) {
        AuthenticatedUser user = getCurrentUser();
        return user.isSuperAdmin() ? tenantIdRequest : user.tenantId();
    }

    private AuthenticatedUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthenticatedUser authUser)) {
            throw new UnauthorizedException("Unauthenticated");
        }
        return authUser;
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
