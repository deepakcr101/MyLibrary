package com.deepak.library.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testAdminAccess() {
        // 1. Create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. Create the Request Entity (Body + Headers)
        String body = "{\"title\":\"Dune\",\"authorName\":\"Frank Herbert\"}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // 3. Use .withBasicAuth() to attach credentials and send the POST
        var response = restTemplate
                .withBasicAuth("admin", "adminpass")
                .postForEntity("/api/books", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void testUserAccessForbidden() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{\"title\":\"Dune\",\"authorName\":\"Frank Herbert\"}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        // Sending as a regular user who shouldn't have POST access
        var response = restTemplate
                .withBasicAuth("user", "userpass")
                .postForEntity("/api/books", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
