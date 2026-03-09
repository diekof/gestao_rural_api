package com.seuprojeto.agro.stage4;

import static org.assertj.core.api.Assertions.assertThat;

import com.seuprojeto.agro.ai.dto.ProductivityForecastRequest;
import com.seuprojeto.agro.ai.dto.ProductivityForecastResponse;
import com.seuprojeto.agro.ai.dto.RiskAnalysisRequest;
import com.seuprojeto.agro.ai.dto.RiskAnalysisResponse;
import com.seuprojeto.agro.auth.dto.AuthResponse;
import com.seuprojeto.agro.auth.dto.LoginRequest;
import com.seuprojeto.agro.dashboard.dto.OverviewResponse;
import com.seuprojeto.agro.satellite.dto.NdviHistoryPointResponse;
import com.seuprojeto.agro.satellite.dto.SatelliteImageCreateRequest;
import com.seuprojeto.agro.satellite.dto.SatelliteImageResponse;
import com.seuprojeto.agro.security.AbstractIntegrationTest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class Stage4IntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldExecuteAiAndSatelliteAndDashboardEndpoints() {
        String token = login("admindemo", "Admin@123");
        HttpHeaders headers = bearer(token);

        ProductivityForecastRequest pfReq = new ProductivityForecastRequest(
                UUID.fromString("51111111-1111-1111-1111-111111111111"),
                UUID.fromString("31111111-1111-1111-1111-111111111111"),
                new BigDecimal("120"),
                new BigDecimal("160"),
                new BigDecimal("0.78"),
                new BigDecimal("98000")
        );
        ResponseEntity<ProductivityForecastResponse> pfResp = restTemplate.exchange("/api/ai/productivity-forecast", HttpMethod.POST,
                new HttpEntity<>(pfReq, headers), ProductivityForecastResponse.class);

        RiskAnalysisRequest riskReq = new RiskAnalysisRequest(
                UUID.fromString("51111111-1111-1111-1111-111111111111"),
                UUID.fromString("31111111-1111-1111-1111-111111111111"),
                new BigDecimal("0.40"), new BigDecimal("0.30"), new BigDecimal("0.50"), new BigDecimal("0.20")
        );
        ResponseEntity<RiskAnalysisResponse> riskResp = restTemplate.exchange("/api/ai/risk-analysis", HttpMethod.POST,
                new HttpEntity<>(riskReq, headers), RiskAnalysisResponse.class);

        SatelliteImageCreateRequest imageReq = new SatelliteImageCreateRequest(
                UUID.fromString("31111111-1111-1111-1111-111111111111"),
                LocalDateTime.now().minusDays(1),
                "SENTINEL-2",
                "https://example.com/images/1.tif",
                new BigDecimal("12.5"),
                new BigDecimal("0.67"),
                "{\"scene\":\"A1\"}"
        );
        ResponseEntity<SatelliteImageResponse> imageResp = restTemplate.exchange("/api/satellite/images", HttpMethod.POST,
                new HttpEntity<>(imageReq, headers), SatelliteImageResponse.class);

        ResponseEntity<String> listResp = restTemplate.exchange("/api/satellite/images?page=0&size=10", HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        ResponseEntity<List<NdviHistoryPointResponse>> ndviResp = restTemplate.exchange("/api/satellite/ndvi-history?fieldId=31111111-1111-1111-1111-111111111111",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });

        ResponseEntity<OverviewResponse> overviewResp = restTemplate.exchange("/api/dashboard/overview", HttpMethod.GET,
                new HttpEntity<>(headers), OverviewResponse.class);

        assertThat(pfResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(pfResp.getBody()).isNotNull();
        assertThat(riskResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(imageResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ndviResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(ndviResp.getBody()).isNotEmpty();
        assertThat(overviewResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(overviewResp.getBody().totalFarms()).isGreaterThan(0);
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
