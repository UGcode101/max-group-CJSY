package org.launchcode.threemix.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SessionStorage<T> {

    private final Map<String, T> storage = new ConcurrentHashMap<>();

    public void setValue(String sessionId, T value) {
        System.out.println("Setting value for " + sessionId);
        storage.put(sessionId, value);
    }

    public T getValue(String sessionId) {
        return storage.get(sessionId);
    }
}