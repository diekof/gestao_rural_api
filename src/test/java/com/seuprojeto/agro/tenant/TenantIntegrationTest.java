package com.seuprojeto.agro.tenant;

import static org.assertj.core.api.Assertions.assertThat;

import com.seuprojeto.agro.auth.dto.AuthResponse;
import com.seuprojeto.agro.auth.dto.LoginRequest;
import com.seuprojeto.agro.security.AbstractIntegrationTest;
import com.seuprojeto.agro.tenant.dto.TenantRequest;
import com.seuprojeto.agro.tenant.dto.TenantResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class TenantIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateTenantAsSuperAdmin() {
        String token = login("superadmin", "Admin@123");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        TenantRequest request = new TenantRequest("Agro Norte", "Norte", "98765432000199", "norte@agro.local", "11911112222", "BASIC");

        ResponseEntity<TenantResponse> response = restTemplate.exchange("/api/tenants", HttpMethod.POST, new HttpEntity<>(request, headers), TenantResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().nome()).isEqualTo("Agro Norte");
    }

    private String login(String user, String pass) {
        ResponseEntity<AuthResponse> loginResp = restTemplate.postForEntity("/api/auth/login", new LoginRequest(user, pass), AuthResponse.class);
        return loginResp.getBody().accessToken();
    }
}
