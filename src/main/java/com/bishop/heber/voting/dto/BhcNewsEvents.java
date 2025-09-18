package com.bishop.heber.voting.dto;
import java.time.Instant;
import java.util.List;
public record BhcNewsEvents(
        String source,
        Instant fetchedAt,
        List<BhcItem> news,
        List<BhcItem> events
) {}
