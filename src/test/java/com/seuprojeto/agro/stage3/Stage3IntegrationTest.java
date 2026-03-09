package com.seuprojeto.agro.stage3;

import static org.assertj.core.api.Assertions.assertThat;

import com.seuprojeto.agro.agriculturaloperation.domain.OperationType;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationCreateRequest;
import com.seuprojeto.agro.agriculturaloperation.dto.AgriculturalOperationResponse;
import com.seuprojeto.agro.auth.dto.AuthResponse;
import com.seuprojeto.agro.auth.dto.LoginRequest;
import com.seuprojeto.agro.common.Role;
import com.seuprojeto.agro.financialentry.domain.FinancialCategory;
import com.seuprojeto.agro.financialentry.domain.FinancialEntryType;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryCreateRequest;
import com.seuprojeto.agro.financialentry.dto.FinancialEntryResponse;
import com.seuprojeto.agro.machine.domain.MachineType;
import com.seuprojeto.agro.machine.dto.MachineCreateRequest;
import com.seuprojeto.agro.machine.dto.MachineResponse;
import com.seuprojeto.agro.machinerecord.domain.MachineRecordType;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordCreateRequest;
import com.seuprojeto.agro.machinerecord.dto.MachineRecordResponse;
import com.seuprojeto.agro.security.AbstractIntegrationTest;
import com.seuprojeto.agro.tenant.dto.TenantRequest;
import com.seuprojeto.agro.tenant.dto.TenantResponse;
import com.seuprojeto.agro.user.dto.UserCreateRequest;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class Stage3IntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateStage3Resources() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = bearer(token);

        MachineCreateRequest machineRequest = new MachineCreateRequest("TR-777", "Trator Teste", MachineType.TRACTOR,
                "Valtra", "T250", 2022, null, new BigDecimal("100.00"), null);
        ResponseEntity<MachineResponse> machineResp = restTemplate.exchange("/api/machines", HttpMethod.POST,
                new HttpEntity<>(machineRequest, headers), MachineResponse.class);

        AgriculturalOperationCreateRequest operationRequest = new AgriculturalOperationCreateRequest(
                UUID.fromString("51111111-1111-1111-1111-111111111111"),
                UUID.fromString("31111111-1111-1111-1111-111111111111"),
                OperationType.SPRAYING,
                null,
                LocalDate.of(2025, 11, 1),
                new BigDecimal("120.00"),
                new BigDecimal("18000.00"),
                null,
                "Aplicação de defensivos"
        );
        ResponseEntity<AgriculturalOperationResponse> operationResp = restTemplate.exchange("/api/agricultural-operations", HttpMethod.POST,
                new HttpEntity<>(operationRequest, headers), AgriculturalOperationResponse.class);

        MachineRecordCreateRequest machineRecordRequest = new MachineRecordCreateRequest(
                machineResp.getBody().id(), operationResp.getBody().id(), MachineRecordType.USAGE,
                LocalDate.of(2025, 11, 1), new BigDecimal("6.50"), BigDecimal.ZERO, "Uso em pulverização"
        );
        ResponseEntity<MachineRecordResponse> recordResp = restTemplate.exchange("/api/machine-records", HttpMethod.POST,
                new HttpEntity<>(machineRecordRequest, headers), MachineRecordResponse.class);

        FinancialEntryCreateRequest financialRequest = new FinancialEntryCreateRequest(
                UUID.fromString("51111111-1111-1111-1111-111111111111"), operationResp.getBody().id(),
                FinancialEntryType.EXPENSE, FinancialCategory.FUEL, null,
                LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 10), null,
                new BigDecimal("5000.00"), "Diesel para operação", "NF-999"
        );
        ResponseEntity<FinancialEntryResponse> financialResp = restTemplate.exchange("/api/financial-entries", HttpMethod.POST,
                new HttpEntity<>(financialRequest, headers), FinancialEntryResponse.class);

        assertThat(machineResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(operationResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(recordResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(financialResp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldEnforceTenantIsolationForStage3Resources() {
        String superAdmin = login("superadmin", "Admin@123");
        HttpHeaders superHeaders = bearer(superAdmin);

        TenantRequest tenantRequest = new TenantRequest("Tenant Stage3", "Tenant Stage3", "88999999000199", "tenantstage3@agro.local", "11980000000", "BASIC");
        ResponseEntity<TenantResponse> tenantResp = restTemplate.exchange("/api/tenants", HttpMethod.POST, new HttpEntity<>(tenantRequest, superHeaders), TenantResponse.class);

        UserCreateRequest userReq = new UserCreateRequest(tenantResp.getBody().id(), "Admin Stage3", "adminstage3@agro.local", "adminstage3", "Senha@123", Role.TENANT_ADMIN);
        restTemplate.exchange("/api/users", HttpMethod.POST, new HttpEntity<>(userReq, superHeaders), String.class);

        String tenant2Token = login("adminstage3", "Senha@123");
        HttpHeaders tenant2Headers = bearer(tenant2Token);

        ResponseEntity<String> response = restTemplate.exchange("/api/machines/71111111-1111-1111-1111-111111111111", HttpMethod.GET,
                new HttpEntity<>(tenant2Headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private String login(String username, String password) {
        ResponseEntity<AuthResponse> response = restTemplate.postForEntity("/api/auth/login", new LoginRequest(username, password), AuthResponse.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody().accessToken();
    }

    private HttpHeaders bearer(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
