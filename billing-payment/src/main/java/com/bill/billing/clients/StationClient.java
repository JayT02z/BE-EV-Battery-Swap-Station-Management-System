package com.bill.billing.clients;

import com.bill.billing.clients.request.StationRequest;
import com.bill.billing.clients.url.StationUrls;
import com.bill.billing.model.event.DTO.StationDTO;
import com.bill.billing.model.event.DTO.StationSwapSummaryDTO;
import com.bill.billing.model.response.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StationClient {

    private final RestTemplate restTemplate;
    private final SimpleJwtTokenGenerator jwtTokenGenerator;
    private final ObjectMapper objectMapper = new ObjectMapper(); // Dùng để convert LinkedHashMap -> DTO

    @Value("${jwt.admin.email}")
    private String adminEmail;

    @Value("${jwt.admin.role}")
    private String adminRole;

    /**
     * Lấy driver theo batteryId
     */
    public StationDTO getStationByStationCode(String stationCode) {
        String url = String.format(StationUrls.GET_STATION_BY_CODE, stationCode);
        StationRequest request = new StationRequest();
        request.setCode(stationCode);
        HttpEntity<StationRequest> entity =
                new HttpEntity<>(request, createHeaders());
        ResponseEntity<ResponseData<StationDTO>> response =
                restTemplate.exchange(
                        url,
                        HttpMethod.POST,
                        entity,
                        new ParameterizedTypeReference<ResponseData<StationDTO>>() {}
                );
        if (response.getBody() == null || response.getBody().getData() == null) {
            throw new RuntimeException("Không lấy được dữ liệu từ: " + url);
        }
        return objectMapper.convertValue(response.getBody().getData(), StationDTO.class);
    }

    public List<StationSwapSummaryDTO> getAllSwapSummary() {
        String url = String.format(StationUrls.GET_SWAP_REPORT);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<ResponseData<List<StationSwapSummaryDTO>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                createHttpEntity(),
                new ParameterizedTypeReference<ResponseData<List<StationSwapSummaryDTO>>>() {}
        );

        if (response.getBody() == null || response.getBody().getData() == null) {
            throw new RuntimeException("Không lấy được dữ liệu từ: " + url);
        }

        return response.getBody().getData();
    }


    /**
     * Tạo HttpEntity với Authorization header
     */
    private HttpEntity<Void> createHttpEntity() {
        return new HttpEntity<>(createHeaders());
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtTokenGenerator.generateToken1Min(adminEmail, adminRole));
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
