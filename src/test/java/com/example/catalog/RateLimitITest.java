package com.example.catalog;

import com.example.catalog.interceptors.RateLimit;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RateLimitITest {

    @Autowired
    private TestRestTemplate restTemplate;

    private static final String API_ENDPOINT = "/";
    private static final String INTERNAL_ENDPOINT = "/internal";
    private static final String XRateLimitRetryAfterSecondsHeader = "X-Rate-Limit-Retry-After-Seconds";
    private static final String XRateLimitRemaining = "X-Rate-Limit-Remaining";

    private RateLimit rateLimit;
    private HttpServletRequest request;
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        rateLimit = new RateLimit();
        rateLimit.rateLimitAlgo = "fixed";
        rateLimit.rateLimitRPM = 10;

        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void testFixedRateLimitAllowsRequests() throws Exception {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getRequestURI()).thenReturn("/api");

        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimit.preHandle(request, response, null), "Expected request to be allowed");
        }
    }

    @Test
    void testSlidingRateLimitAllowsRequests() throws Exception {
        rateLimit.rateLimitAlgo = "moving";
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getRequestURI()).thenReturn("/api");

        for (int i = 0; i < 5; i++) {
            assertTrue(rateLimit.preHandle(request, response, null), "Expected request to be allowed");
        }
    }

    @Test
    void testInternalEndpointBypassesRateLimit() throws Exception {
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getRequestURI()).thenReturn("/internal");

        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimit.preHandle(request, response, null), "Expected internal endpoint to bypass rate limit");
        }
    }

    @Test
    void testInvalidRateLimitAlgo() throws Exception {
        rateLimit.rateLimitAlgo = "invalid";
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");
        when(request.getRequestURI()).thenReturn("/api");

        // Simulate a request and expect it to pass as a fallback
        assertTrue(rateLimit.preHandle(request, response, null), "Expected request to pass with invalid algorithm");
    }



    @Test
    public void testRateLimiterEnforcesLimits() throws InterruptedException {
        int allowedRequests = 10;
        int extraRequests = 5;

        for (int i = 0; i < allowedRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)), "Expected status code to be 200 for the first 10 requests");

            String remainingRequests = String.valueOf(allowedRequests - (i + 1));
            assertEquals(remainingRequests, response.getHeaders().get(XRateLimitRemaining).get(0), "Expected " + XRateLimitRemaining + " header to be " + remainingRequests + " after " + i + 1 + " requests");
        }

        for (int i = 0; i < extraRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(API_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(429)));
            int retryAfter = Integer.parseInt(response.getHeaders().get(XRateLimitRetryAfterSecondsHeader).get(0));
            assertTrue(retryAfter > 0);
        }
    }

    @Test
    public void testRateLimiterBypassesInternalEndpoint() {
        int totalRequests = 15;

        for (int i = 0; i < totalRequests; i++) {
            ResponseEntity<String> response = restTemplate.getForEntity(INTERNAL_ENDPOINT, String.class);
            assertTrue(response.getStatusCode().equals(HttpStatusCode.valueOf(200)));
            assertFalse(response.getHeaders().containsKey(XRateLimitRemaining));
        }
    }
}
