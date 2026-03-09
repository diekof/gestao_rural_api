package com.seuprojeto.agro.stage2;

import static org.assertj.core.api.Assertions.assertThat;

import com.seuprojeto.agro.auth.dto.AuthResponse;
import com.seuprojeto.agro.auth.dto.LoginRequest;
import com.seuprojeto.agro.common.Role;
import com.seuprojeto.agro.crop.dto.CropCreateRequest;
import com.seuprojeto.agro.crop.dto.CropResponse;
import com.seuprojeto.agro.farm.dto.FarmCreateRequest;
import com.seuprojeto.agro.farm.dto.FarmResponse;
import com.seuprojeto.agro.field.dto.FieldCreateRequest;
import com.seuprojeto.agro.field.dto.FieldResponse;
import com.seuprojeto.agro.security.AbstractIntegrationTest;
import com.seuprojeto.agro.season.dto.SeasonCreateRequest;
import com.seuprojeto.agro.season.dto.SeasonResponse;
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

class Stage2IntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateFarm() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = bearer(token);
        FarmCreateRequest request = new FarmCreateRequest("Fazenda Teste", "Produtor", new BigDecimal("100.00"), "Lucas do Rio Verde", "MT", null, null, null);

        ResponseEntity<FarmResponse> response = restTemplate.exchange("/api/farms", HttpMethod.POST, new HttpEntity<>(request, headers), FarmResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().tenantId().toString()).isEqualTo("11111111-1111-1111-1111-111111111111");
    }

    @Test
    void shouldCreateField() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = bearer(token);
        FieldCreateRequest request = new FieldCreateRequest(UUID.fromString("21111111-1111-1111-1111-111111111111"), "Talhão Teste", "TT-01", new BigDecimal("50.00"), "ARGILOSO", null, null);

        ResponseEntity<FieldResponse> response = restTemplate.exchange("/api/fields", HttpMethod.POST, new HttpEntity<>(request, headers), FieldResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldBlockFieldCreationWhenSumExceedsFarmArea() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = bearer(token);
        FieldCreateRequest request = new FieldCreateRequest(UUID.fromString("21111111-1111-1111-1111-111111111111"), "Talhão Excedente", "EX-01", new BigDecimal("500.00"), "ARGILOSO", null, null);

        ResponseEntity<String> response = restTemplate.exchange("/api/fields", HttpMethod.POST, new HttpEntity<>(request, headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldCreateCrop() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = bearer(token);
        CropCreateRequest request = new CropCreateRequest("Algodão Teste", "FIBRA", 180, new BigDecimal("20.00"), "ton/ha", null);

        ResponseEntity<CropResponse> response = restTemplate.exchange("/api/crops", HttpMethod.POST, new HttpEntity<>(request, headers), CropResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void shouldCreateSeason() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = bearer(token);
        SeasonCreateRequest request = new SeasonCreateRequest(
                UUID.fromString("31111111-1111-1111-1111-111111111111"),
                UUID.fromString("41111111-1111-1111-1111-111111111111"),
                "Safra Teste",
                2026,
                LocalDate.of(2026, 1, 10),
                null,
                new BigDecimal("200.00"),
                new BigDecimal("12000.00"),
                null,
                null
        );

        ResponseEntity<SeasonResponse> response = restTemplate.exchange("/api/seasons", HttpMethod.POST, new HttpEntity<>(request, headers), SeasonResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void shouldBlockSeasonWhenPlantedAreaExceedsFieldArea() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = bearer(token);
        SeasonCreateRequest request = new SeasonCreateRequest(
                UUID.fromString("31111111-1111-1111-1111-111111111111"),
                UUID.fromString("41111111-1111-1111-1111-111111111111"),
                "Safra Excedente",
                2027,
                LocalDate.of(2027, 1, 10),
                null,
                new BigDecimal("450.00"),
                null,
                null,
                null
        );

        ResponseEntity<String> response = restTemplate.exchange("/api/seasons", HttpMethod.POST, new HttpEntity<>(request, headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void shouldEnforceTenantIsolation() {
        String superAdmin = login("superadmin", "Admin@123");
        HttpHeaders superHeaders = bearer(superAdmin);

        TenantRequest tenantRequest = new TenantRequest("Tenant 2", "Tenant 2", "99999999000199", "tenant2@agro.local", "11988887777", "BASIC");
        ResponseEntity<TenantResponse> tenantResp = restTemplate.exchange("/api/tenants", HttpMethod.POST, new HttpEntity<>(tenantRequest, superHeaders), TenantResponse.class);
        UUID tenant2 = tenantResp.getBody().id();

        UserCreateRequest userReq = new UserCreateRequest(tenant2, "Admin 2", "admin2@agro.local", "admin2", "Senha@123", Role.TENANT_ADMIN);
        restTemplate.exchange("/api/users", HttpMethod.POST, new HttpEntity<>(userReq, superHeaders), String.class);

        String tenant2Token = login("admin2", "Senha@123");
        HttpHeaders tenant2Headers = bearer(tenant2Token);

        ResponseEntity<String> response = restTemplate.exchange("/api/farms/21111111-1111-1111-1111-111111111111", HttpMethod.GET, new HttpEntity<>(tenant2Headers), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void shouldApplyRoleAccessRuleForViewer() {
        String superAdmin = login("superadmin", "Admin@123");
        HttpHeaders superHeaders = bearer(superAdmin);
        UserCreateRequest viewerReq = new UserCreateRequest(UUID.fromString("11111111-1111-1111-1111-111111111111"), "Viewer", "viewer@demoagro.local", "viewer", "Senha@123", Role.VIEWER);
        restTemplate.exchange("/api/users", HttpMethod.POST, new HttpEntity<>(viewerReq, superHeaders), String.class);

        String viewerToken = login("viewer", "Senha@123");
        HttpHeaders viewerHeaders = bearer(viewerToken);

        FarmCreateRequest farmRequest = new FarmCreateRequest("Fazenda Viewer", "Produtor", new BigDecimal("100.00"), "Sorriso", "MT", null, null, null);
        ResponseEntity<String> postResp = restTemplate.exchange("/api/farms", HttpMethod.POST, new HttpEntity<>(farmRequest, viewerHeaders), String.class);
        ResponseEntity<String> getResp = restTemplate.exchange("/api/farms", HttpMethod.GET, new HttpEntity<>(viewerHeaders), String.class);

        assertThat(postResp.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(getResp.getStatusCode()).isEqualTo(HttpStatus.OK);
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
