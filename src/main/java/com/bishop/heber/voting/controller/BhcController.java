package com.bishop.heber.voting.controller;

import com.bishop.heber.voting.dto.BhcNewsEvents;
import com.bishop.heber.voting.service.impl.BhcNewsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/public/bhc")
public class BhcController {
    private final BhcNewsService svc;
    public BhcController(BhcNewsService svc) { this.svc = svc; }

    @GetMapping("/news-events")
    public ResponseEntity<BhcNewsEvents> get() {
        return ResponseEntity.ok(svc.fetch());
    }

    // src/main/java/com/bishop/heber/voting/controller/BhcController.java
    @GetMapping("/debug")
    public Map<String, Object> debug() {
        var out = new LinkedHashMap<String, Object>();
        out.put("source", "https://bhc.edu.in/bhc/index.php");
        out.put("fetchedAt", java.time.Instant.now().toString());
        out.put("lastError", svc.getLastError()); // add a getter in the service (already in code above)
        return out;
    }

}
