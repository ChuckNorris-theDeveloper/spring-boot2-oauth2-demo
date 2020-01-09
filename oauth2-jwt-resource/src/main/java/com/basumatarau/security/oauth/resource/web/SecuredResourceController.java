package com.basumatarau.security.oauth.resource.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class SecuredResourceController {

    @GetMapping(value = "/public")
    public ResponseEntity<String> getUnprotected() {
        return ResponseEntity.ok("unprotected resource.");
    }

    @GetMapping(value = "/secured")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> getProtected() {
        return ResponseEntity.ok("protected resource.");
    }
}
