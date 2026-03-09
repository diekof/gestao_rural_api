package com.seuprojeto.agro.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.seuprojeto.agro.auth.dto.AuthResponse;
import com.seuprojeto.agro.auth.dto.LoginRequest;
import com.seuprojeto.agro.common.Role;
import com.seuprojeto.agro.security.AbstractIntegrationTest;
import com.seuprojeto.agro.user.dto.UserCreateRequest;
import com.seuprojeto.agro.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class UserIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateUserAsTenantAdminInOwnTenant() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        UserCreateRequest request = new UserCreateRequest(null, "Operador", "operador@demoagro.local", "operador", "Senha@123", Role.OPERATOR);

        ResponseEntity<UserResponse> response = restTemplate.exchange("/api/users", HttpMethod.POST, new HttpEntity<>(request, headers), UserResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().tenantId().toString()).isEqualTo("11111111-1111-1111-1111-111111111111");
    }

    @Test
    void tenantAdminCannotAccessTenantEndpoints() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        ResponseEntity<String> response = restTemplate.exchange("/api/tenants", HttpMethod.GET, new HttpEntity<>(headers), String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    private String login(String user, String pass) {
        ResponseEntity<AuthResponse> loginResp = restTemplate.postForEntity("/api/auth/login", new LoginRequest(user, pass), AuthResponse.class);
        return loginResp.getBody().accessToken();
    }
}
