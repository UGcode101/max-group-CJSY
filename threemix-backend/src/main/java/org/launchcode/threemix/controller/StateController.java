package org.launchcode.threemix.controller;

import jakarta.servlet.http.HttpSession;
import org.launchcode.threemix.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StateController {

    @Autowired
    private StateService stateService;

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/generateState")
    public String generateState(HttpSession session) {
        return stateService.generateState(session.getId());
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping(value = "/validateState")
    public boolean validateState(@RequestParam String state, HttpSession session) {
        return stateService.validateState(session.getId(), state);
    }
}