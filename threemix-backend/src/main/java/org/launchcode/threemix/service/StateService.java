package org.launchcode.threemix.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StateService {

    private ConcurrentHashMap<String, String> stateMap = new ConcurrentHashMap<>();

    public String generateState(String sessionId) {
        String state = generateRandomState();
        stateMap.put(sessionId, state);
        return state;
    }

    public boolean validateState(String sessionId, String state) {
        String storedState = stateMap.remove(sessionId);
        return storedState != null && storedState.equals(state);
    }

    private String generateRandomState() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] stateBytes = new byte[16];
        secureRandom.nextBytes(stateBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(stateBytes);
    }
}