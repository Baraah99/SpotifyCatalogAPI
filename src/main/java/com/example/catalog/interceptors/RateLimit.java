package com.example.catalog.interceptors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimit implements HandlerInterceptor {

    @Value("${rate-limit.algo}")
    public String rateLimitAlgo;

    @Value("${rate-limit.rpm}")
    public int rateLimitRPM;

    private final ConcurrentHashMap<String, Object> clientRequests = new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();

        // Skip rate limiting for /internal endpoint
        if ("/internal".equals(request.getRequestURI())) {
            return true;
        }

        boolean isAllowed;
        if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            isAllowed = isAllowedFixed(clientIp);
        } else {
            isAllowed = isAllowedSliding(clientIp);
        }

        if (!isAllowed) {
            response.setHeader("X-Rate-Limit-Remaining", "0");
            response.setHeader("X-Rate-Limit-Retry-After-Seconds", "60"); // Example value
            response.setStatus(429); // Too Many Requests
            return false;
        }

        response.setHeader("X-Rate-Limit-Remaining", Integer.toString(getRemainingRequests(clientIp)));
        return true;
    }

    private boolean isAllowedFixed(String clientIp) {
        long currentTime = System.currentTimeMillis();
        RateLimitState state = (RateLimitState) clientRequests.computeIfAbsent(clientIp, k -> new RateLimitState());

        synchronized (state) {
            // Reset count if the current window has passed
            if (currentTime - state.lastResetTime >= 60000) {
                state.requestCount = 0;
                state.lastResetTime = currentTime;
            }

            if (state.requestCount < rateLimitRPM) {
                state.requestCount++;
                return true;
            }
            return false;
        }
    }

    public boolean isAllowedSliding(String clientIp) {
        long currentTime = System.currentTimeMillis();
        CircularBuffer buffer = (CircularBuffer) clientRequests.computeIfAbsent(clientIp, k -> new CircularBuffer(rateLimitRPM));

        synchronized (buffer) {
            // Remove requests older than 1 minute
            buffer.removeOldEntries(currentTime - 60000);

            if (buffer.size() < rateLimitRPM) {
                buffer.add(currentTime); // Record the new request
                return true;
            }
            return false;
        }
    }

    private int getRemainingRequests(String clientIp) {
        if ("fixed".equalsIgnoreCase(rateLimitAlgo)) {
            RateLimitState state = (RateLimitState) clientRequests.get(clientIp);
            return state == null ? rateLimitRPM : Math.max(0, rateLimitRPM - state.requestCount);
        } else {
            CircularBuffer buffer = (CircularBuffer) clientRequests.get(clientIp);
            return buffer == null ? rateLimitRPM : Math.max(0, rateLimitRPM - buffer.size());
        }
    }

    private static class RateLimitState {
        int requestCount = 0;
        long lastResetTime = System.currentTimeMillis();
    }

    private static class CircularBuffer {
        private final long[] buffer;
        private int head;
        private int size;

        public CircularBuffer(int capacity) {
            this.buffer = new long[capacity];
            this.head = 0;
            this.size = 0;
        }

        public void add(long timestamp) {
            buffer[head] = timestamp;
            head = (head + 1) % buffer.length;
            if (size < buffer.length) {
                size++;
            }
        }

        public void removeOldEntries(long threshold) {
            while (size > 0 && buffer[(buffer.length + head - size) % buffer.length] < threshold) {
                size--;
            }
        }

        public int size() {
            return size;
        }
    }
}